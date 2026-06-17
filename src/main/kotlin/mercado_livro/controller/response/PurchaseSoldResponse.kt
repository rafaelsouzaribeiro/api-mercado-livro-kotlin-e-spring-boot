package mercado_livro.controller.response

import mercado_livro.model.BookModel
import mercado_livro.model.CustomerModel
import java.math.BigDecimal
import java.time.LocalDateTime

data class PurchaseSoldResponse (
    val id:String?=null,
    val customer: CustomerModel,
    val nfe:String?=null,
    val price:BigDecimal,
    val books:List<BookModel>,
    val createdAt: LocalDateTime
)
