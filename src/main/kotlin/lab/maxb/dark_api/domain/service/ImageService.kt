package lab.maxb.dark_api.domain.service

import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream


interface ImageService {
    fun getUrl(name: String): String

    @Throws(IOException::class)
    fun get(name: String): InputStream?

    @Throws(IOException::class)
    fun save(file: MultipartFile): String

    @Throws(IOException::class)
    fun save(bufferedImage: BufferedImage, originalFileName: String): String

    @Throws(IOException::class)
    fun delete(name: String)
}