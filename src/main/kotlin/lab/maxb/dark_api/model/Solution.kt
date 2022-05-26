package lab.maxb.dark_api.model

import java.util.*
import javax.persistence.*

@Entity
class Solution(
    @Column(nullable = false)
    var answer: String? = null,

    @Column(nullable = false)
    var rating: Int = 1,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable=false)
    var task: RecognitionTask? = null,

    @Id
    @GeneratedValue
    @Column(nullable = false)
    var id: UUID = randomUUID
)
