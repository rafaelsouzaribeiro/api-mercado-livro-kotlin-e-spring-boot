package mercado_livro.helper

import mercado_livro.enums.CustomerStatus
import mercado_livro.enums.Roles
import mercado_livro.model.BookModel
import mercado_livro.model.CustomerModel
import mercado_livro.model.PurchaseModel
import java.math.BigDecimal
import java.util.*

 fun buildCustomers(
    id:String? = null,
    name:String = "Customer name",
    email:String="${UUID.randomUUID()}@email.com",
    password:String="password",
    roles:Set<Roles>?=null
): CustomerModel {
    return CustomerModel(
        id=id,
        name=name,
        email=email,
        status = CustomerStatus.ACTIVE,
        password=password,
        roles = roles ?: setOf(Roles.CUSTOMER)
    )
}

fun buildPurchase(
    id:UUID?=null,
    customer:CustomerModel= buildCustomers(),
    books:MutableList<BookModel> = mutableListOf(),
    nfe:String?=UUID.randomUUID().toString(),
    price: BigDecimal = BigDecimal.TEN
)= PurchaseModel(
    id=id.toString(),
    customer = customer,
    books = books,
    nfe = nfe,
    price = price
)
