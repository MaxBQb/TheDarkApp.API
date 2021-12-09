package lab.maxb.dark_api.DB

import lab.maxb.dark_api.DB.DAO.RecognitionTaskDAO
import lab.maxb.dark_api.DB.DAO.UserCredentialsDAO
import lab.maxb.dark_api.DB.DAO.UserDAO
import lab.maxb.dark_api.Model.RecognitionTask
import lab.maxb.dark_api.Model.User
import lab.maxb.dark_api.Model.UserCredentials
import lab.maxb.dark_api.Security.Services.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class DBPrepopulator @Autowired constructor(
    private val recognitionTaskDAO: RecognitionTaskDAO,
    private val userDAO: UserDAO,
    private val authService: AuthService,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        val user = userDAO.save(User(
            "Max",
            12,
            authService.signup(AuthService.AuthRequest(
                "User",
                "123"
            ))!!.id
        ))

        userDAO.save(User(
            "ModeratorZou",
            id=authService.signup(AuthService.AuthRequest(
                "Moderator",
                "321"
            ))!!.id
        ))

        userDAO.save(User(
            "Admin",
            id=authService.signup(AuthService.AuthRequest(
                "Admin",
                "111"
            ))!!.id
        ))


        recognitionTaskDAO.save(RecognitionTask(
            setOf("стул", "кресло", "диван"),
            listOf("image1", "image2"),
            user,
            true
        ))
        recognitionTaskDAO.save(RecognitionTask(
            setOf("стол", "стол на трёх ножках", "столешница"),
            listOf("image3", "image4"),
            user,
            false
        ))
        recognitionTaskDAO.save(RecognitionTask(
            setOf("стул1", "кресло1", "диван1"),
            listOf("image11", "image21"),
            user,
            true
        ))
        recognitionTaskDAO.save(RecognitionTask(
            setOf("стол2", "стол на трёх ножках2", "столешница2"),
            listOf("image32", "image42"),
            user,
            false
        ))
    }
}