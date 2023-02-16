package lab.maxb.dark_api.application.rest.controllers

import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


fun saveFile(uploadDir: String,
             fileName: String,
             multipartFile: MultipartFile) {
    val uploadPath = Paths.get(uploadDir)
    if (!Files.exists(uploadPath))
        Files.createDirectories(uploadPath)

    multipartFile.inputStream.use { inputStream ->
        Files.copy(
            inputStream,
            uploadPath.resolve(fileName),
            StandardCopyOption.REPLACE_EXISTING
        )
    }
}