package lab.maxb.dark_api

import lab.maxb.dark_api.application.request.AuthRequest
import lab.maxb.dark_api.application.request.toDomain
import lab.maxb.dark_api.domain.exceptions.AccessDeniedException
import lab.maxb.dark_api.domain.exceptions.NotFoundException
import lab.maxb.dark_api.domain.exceptions.ValidationError
import lab.maxb.dark_api.domain.model.*
import lab.maxb.dark_api.domain.service.AuthService
import lab.maxb.dark_api.domain.service.ImagesService
import lab.maxb.dark_api.domain.service.TasksService
import lab.maxb.dark_api.domain.service.UserService
import lab.maxb.dark_api.infrastracture.configuration.db.DatabaseFiller
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.awt.image.BufferedImage

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DarkApiApplicationTests @Autowired constructor(
    private val authService: AuthService,
    private val imagesService: ImagesService,
    private val userService: UserService,
    private val tasksService: TasksService,
    private val fillerConfig: DatabaseFiller.Properties,
) {
    lateinit var image: String
    lateinit var user1: User
    lateinit var user2: User

    @BeforeAll
    fun setUp() {
        image = imagesService.save(
            BufferedImage(
                1, 1,
                BufferedImage.TYPE_INT_RGB
            ), "img"
        )
        user1 = User("User1")
        user1 = authService.signup(UserCredentials(user1.name, "123", user1)).user
        user2 = User("User2")
        user2 = authService.signup(UserCredentials(user2.name, "123", user2)).user
    }

    @Test
    fun hasAdmin() {
        assertEquals(
            authService.login(
                AuthRequest(
                    fillerConfig.admin.login,
                    fillerConfig.admin.password,
                ).toDomain()
            ).role, Role.ADMINISTRATOR
        )
    }

    @Test
    fun taskCreationSuccess() {
        val task = RecognitionTask(setOf("empty"), listOf(image), user1)
        val saved = tasksService.add(task)
        val taskExpected = task.copy(id = saved.id)
        assertEquals(saved, taskExpected)
        val taskStored = tasksService.getTask(ShortUserCredentials(user1, Role.MODERATOR), saved.id)
        assertEquals(taskExpected, taskStored)
    }

    @Test
    fun taskCreationError() {
        val validTask1 = RecognitionTask(setOf("empty"), MutableList(RecognitionTask.MAX_IMAGES_COUNT) { image }, user1)
        val invalidTask = RecognitionTask(setOf("empty"), MutableList(RecognitionTask.MAX_IMAGES_COUNT+1) { image }, user1)
        val validTask2 = RecognitionTask(setOf("empty"), MutableList(RecognitionTask.MAX_IMAGES_COUNT-1) { image }, user1)
        tasksService.add(validTask1)
        assertThrows<ValidationError> { tasksService.add(invalidTask) }
        tasksService.add(validTask2)
    }

    @Test
    fun taskSolutionSuccess() {
        val answer = "empty"
        val task = RecognitionTask(setOf(answer), listOf(image), user1, true)
        val saved = tasksService.add(task)
        val rating = user2.rating
        assertEquals(true, tasksService.solve(ShortUserCredentials(user2), saved.id, answer))
        assertEquals(false, tasksService.solve(ShortUserCredentials(user2), saved.id, answer))
        val user2New = userService.getUser(user2.id)
        assertEquals(user2New.rating, rating + 1)
    }

    @Test
    fun taskSolutionError() {
        val task = RecognitionTask(setOf("empty"), listOf(image), user1, true)
        val saved = tasksService.add(task)
        assertEquals(false, tasksService.solve(ShortUserCredentials(user2), saved.id, "hello"))
        assertThrows<AccessDeniedException> {
            tasksService.solve(ShortUserCredentials(user1), saved.id, "hello")
        }
    }

    @Test
    fun taskAccepted() {
        val task = RecognitionTask(setOf("empty"), listOf(image), user1)
        val saved = tasksService.add(task)
        tasksService.mark(saved.id, true)
        val taskStored = tasksService.getTask(ShortUserCredentials(user1, Role.MODERATOR), saved.id)
        assertEquals(true, taskStored.reviewed)
    }

    @Test
    fun taskAcceptedThenDeclined() {
        val task = RecognitionTask(setOf("empty"), listOf(image), user1)
        val saved = tasksService.add(task)
        tasksService.mark(saved.id, true)
        tasksService.mark(saved.id, false)
        val taskStored = tasksService.getTask(ShortUserCredentials(user1, Role.MODERATOR), saved.id)
        assertEquals(false, taskStored.reviewed)
    }

    @Test
    fun taskDeclined() {
        val image2 = imagesService.save(
            BufferedImage(
                1, 1,
                BufferedImage.TYPE_INT_RGB
            ), "img"
        )
        val task = RecognitionTask(setOf("empty"), listOf(image2), user1)
        val saved = tasksService.add(task)
        tasksService.mark(saved.id, false)
        assertThrows<NotFoundException> {
            tasksService.getTask(ShortUserCredentials(user1, Role.MODERATOR), saved.id)
        }
        assertEquals(false, imagesService.exists(image2))
    }
}
