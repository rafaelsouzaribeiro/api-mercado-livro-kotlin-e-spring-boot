package mercado_livro.controller.mapper

import mercado_livro.controller.request.PostPurchaseRequest
import mercado_livro.model.PurchaseModel
import mercado_livro.service.BookService
import mercado_livro.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {
    fun toModel(request:PostPurchaseRequest):PurchaseModel{
        val customer=customerService.getById(request.customerId)
        val books = bookService.fidAllByIds(request.bookIds)
        return PurchaseModel(
            customer = customer,
            books = books,
            price=books.sumOf { it.price }
        )
    }
}