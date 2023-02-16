package lab.maxb.dark_api.infrastracture.datasource.local.model

import com.fasterxml.jackson.annotation.JsonIgnore
import lab.maxb.dark_api.domain.model.Solution
import lab.maxb.dark_api.domain.model.randomUUID
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "solution")
class SolutionLocalDTO(
    @Column(nullable = false)
    var answer: String? = null,

    @Column(nullable = false)
    var rating: Int = 1,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var user: UserLocalDTO? = null,

    @JoinColumn(name = "task", nullable = false)
    @ManyToOne(targetEntity = RecognitionTaskLocalDTO::class, fetch = FetchType.LAZY)
    @JsonIgnore
    var task: RecognitionTaskLocalDTO? = null,

    @Column(name = "task", insertable = false, updatable = false, nullable = false)
    var taskId: UUID? = null,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = randomUUID
)

fun Solution.toLocal() = SolutionLocalDTO(
    answer = answer,
    rating = rating,
    user = user.toLocal(),
    taskId = taskId,
    id = id,
)

fun SolutionLocalDTO.toDomain() = Solution(
    answer = answer!!,
    rating = rating,
    user = user!!.toDomain(),
    taskId = taskId!!,
    id = id,
)