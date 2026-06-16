package mercado_livro.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import mercado_livro.validation.EmailAvailable

data class PostCustomerRequest(
    @field:NotEmpty(message = "Nome não por ser vazio")
    var name:String,
    @field:Email(message = "Email inválido")
    @EmailAvailable
    var email:String
)
