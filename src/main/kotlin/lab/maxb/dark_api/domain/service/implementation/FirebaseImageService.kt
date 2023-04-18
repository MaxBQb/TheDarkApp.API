package lab.maxb.dark_api.domain.service.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import lab.maxb.dark_api.domain.model.randomUUID
import lab.maxb.dark_api.domain.service.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.channels.Channels
import javax.imageio.ImageIO


@Profile("!test")
@Service
class FirebaseImageService @Autowired constructor(
    private var properties: Properties,
    private var firebaseCredential: FirebaseCredential,
) : ImageService {
    private val bucket get() = StorageClient.getInstance().bucket()

    @EventListener
    fun init(event: ApplicationReadyEvent?) {
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(
                ObjectMapper().writeValueAsString(firebaseCredential).byteInputStream())
            )
            .setStorageBucket(properties.bucketName)
            .build()
        FirebaseApp.initializeApp(options)
    }

    override fun getUrl(name: String) =
        properties.imageUrl.format(name)

    @Throws(IOException::class)
    override fun save(file: MultipartFile): String {
        val name: String = file.originalFilename!!.randomFileName
        bucket.create(name, file.bytes, file.contentType)
        return name
    }

    @Throws(IOException::class)
    override fun save(bufferedImage: BufferedImage, originalFileName: String): String {
        val bytes: ByteArray = bufferedImage.toByteArray(originalFileName.extension)
        val name: String = originalFileName.randomFileName
        bucket.create(name, bytes)
        return name
    }

    override fun get(name: String) = bucket.get(name)?.reader()?.let {
        Channels.newInputStream(it)
    }

    override fun exists(name: String) = bucket.get(name) != null

    @Throws(IOException::class)
    override fun delete(name: String) {
        if (name.isBlank())
            throw IOException("invalid file name")
        val blob = bucket[name] ?: throw IOException("file not found")
        blob.delete()
    }

    @Profile("!test")
    @ConfigurationProperties(prefix = "firebase")
    @ConstructorBinding
    data class Properties(
        val bucketName: String,
        val imageUrl: String
    )

    val String.extension get() = StringUtils.getFilenameExtension(this) ?: ""

    val String.randomFileName get()
    = "$randomUUID.$extension"

    @Throws(IOException::class)
    fun BufferedImage.toByteArray(format: String): ByteArray = ByteArrayOutputStream().use {
        ImageIO.write(this, format, it)
        it.flush()
        it.toByteArray()
    }
}

@Profile("!test")
@ConfigurationProperties(prefix = "firebase.credential")
@ConstructorBinding
data class FirebaseCredential(
    val type: String,
    val project_id: String,
    val private_key_id: String,
    val private_key: String,
    val client_email: String,
    val client_id: String,
    val auth_uri: String,
    val token_uri: String,
    val auth_provider_x509_cert_url: String,
    val client_x509_cert_url: String,
)