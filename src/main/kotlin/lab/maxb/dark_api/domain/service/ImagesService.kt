package lab.maxb.dark_api.domain.service

import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream


interface ImagesService {
    fun getUrl(name: String): String

    fun get(name: String): InputStream

    fun exists(name: String): Boolean

    @Throws(IOException::class)
    fun save(file: MultipartFile): String

    @Throws(IOException::class)
    fun save(bufferedImage: BufferedImage, originalFileName: String): String

    fun delete(name: String)
}