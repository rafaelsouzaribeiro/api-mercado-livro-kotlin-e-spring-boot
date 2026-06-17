package mercado_livro.controller

import mercado_livro.controller.mapper.PurchaseMapper
import mercado_livro.controller.request.PostPurchaseRequest
import mercado_livro.controller.response.PurchaseSoldResponse
import mercado_livro.extension.toResponse
import mercado_livro.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("purchase")
class PurchaseController(
    private val purchaseService: PurchaseService,
    private val purchaseMapper: PurchaseMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: PostPurchaseRequest){
        purchaseService.create(purchaseMapper.toModel(request))
    }

    @GetMapping("{id}")
    fun findPurchase(@PathVariable id:UUID): List<PurchaseSoldResponse> {
        return purchaseService.findPurchase(id.toString()).map { it.toResponse() }
    }
}
