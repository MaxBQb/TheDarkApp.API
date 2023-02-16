package lab.maxb.dark_api.infrastracture.datasource.local.model

import lab.maxb.dark_api.domain.model.RecognitionTask
import lab.maxb.dark_api.domain.model.randomUUID
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "recognition_task")
class RecognitionTaskLocalDTO(
    @ElementCollection
    @CollectionTable(name="recognition_task_names")
    var names: Set<String>? = null,

    @ElementCollection
    @CollectionTable(name="recognition_task_images")
    var images: MutableList<String>? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var owner: UserLocalDTO? = null,

    @OneToMany(mappedBy = "task")
    var solutions: Set<SolutionLocalDTO>? = null,

    @Column(nullable = false)
    var reviewed: Boolean = false,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = randomUUID
)

fun RecognitionTask.toLocal() = RecognitionTaskLocalDTO(
    names = names,
    images = images.toMutableList(),
    owner = owner.toLocal(),
    solutions = solutions.map { it.toLocal() }.toSet(),
    reviewed = reviewed,
    id = id,
)

fun RecognitionTaskLocalDTO.toDomain() = RecognitionTask(
    names = names ?: emptySet(),
    images = images ?: emptyList(),
    owner = owner!!.toDomain(),
    solutions = solutions?.map { it.toDomain() }?.toSet() ?: emptySet(),
    reviewed = reviewed,
    id = id,
)