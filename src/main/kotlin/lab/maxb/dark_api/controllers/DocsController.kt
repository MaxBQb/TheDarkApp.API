package lab.maxb.dark_api.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView


@Controller
class DocsController {
    @GetMapping("docs")
    fun redirectToDocs(attributes: RedirectAttributes)
        = RedirectView("/docs/swagger-ui/index.html")
}