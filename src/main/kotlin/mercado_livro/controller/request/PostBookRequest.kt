package mercado_livro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.util.UUID

data class PostBookRequest (
    @field:NotEmpty(message = "Nome não por ser vazio")
    var name : String,
    @field:NotNull(message = "Preço não por ser vazio")
    var price:BigDecimal,
    @field:NotNull(message = "Customer não por ser vazio")
    @JsonAlias("customer_id")
    var customerId:UUID

)


