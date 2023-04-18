package lab.maxb.dark_api.mocks

import lab.maxb.dark_api.domain.model.randomUUID
import lab.maxb.dark_api.domain.service.ImageService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO

@Primary
@Service
class MockImageService : ImageService {
    private val storage = mutableMapOf<String, ByteArray>()
    override fun getUrl(name: String) = name

    @Throws(IOException::class)
    override fun save(file: MultipartFile): String {
        val name: String = file.originalFilename!!.randomFileName
        storage[name] = file.inputStream.readBytes()
        return name
    }

    @Throws(IOException::class)
    override fun save(bufferedImage: BufferedImage, originalFileName: String): String {
        val bytes: ByteArray = bufferedImage.toByteArray(originalFileName.extension)
        val name: String = originalFileName.randomFileName
        storage[name] = bytes
        return name
    }

    override fun get(name: String) = storage[name]?.inputStream()

    override fun exists(name: String) = storage.contains(name)

    @Throws(IOException::class)
    override fun delete(name: String) {
        if (name.isBlank())
            throw IOException("invalid file name")
        storage.remove(name)
    }

    val String.extension get() = StringUtils.getFilenameExtension(this) ?: ""

    val String.randomFileName get() = "$randomUUID.$extension"

    @Throws(IOException::class)
    fun BufferedImage.toByteArray(format: String): ByteArray = ByteArrayOutputStream().use {
        ImageIO.write(this, format, it)
        it.flush()
        it.toByteArray()
    }
}
