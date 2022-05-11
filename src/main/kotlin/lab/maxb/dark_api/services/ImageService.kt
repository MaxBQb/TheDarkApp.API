package lab.maxb.dark_api.services

import lab.maxb.dark_api.model.randomUUID
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO


interface ImageService {
    fun getImageUrl(name: String): String

    @Throws(IOException::class)
    fun save(file: MultipartFile): String

    @Throws(IOException::class)
    fun get(name: String): InputStream?

    @Throws(IOException::class)
    fun save(bufferedImage: BufferedImage, originalFileName: String): String

    @Throws(IOException::class)
    fun delete(name: String)

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