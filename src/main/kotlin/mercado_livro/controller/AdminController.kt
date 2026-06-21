package mercado_livro.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("admin")
class AdminController{

    @GetMapping("/report")
    fun report(): String {
       return "this is a report. only Admin can see it"
    }


}