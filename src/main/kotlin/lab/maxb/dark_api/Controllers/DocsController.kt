package lab.maxb.dark_api.Controllers

import lab.maxb.dark_api.Security.Services.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView


@Controller
class DocsController {
    @GetMapping("docs/")
    fun redirectToDocs(attributes: RedirectAttributes)
        = RedirectView("/docs/swagger-ui.html").also {
            attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView")
            attributes.addAttribute("attribute", "redirectWithRedirectView")
        }
}