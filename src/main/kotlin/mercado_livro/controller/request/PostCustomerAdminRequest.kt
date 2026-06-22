package mercado_livro.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import mercado_livro.enums.Roles
import mercado_livro.validation.EmailAvailable

data class PostCustomerAdminRequest(
    @field:NotEmpty(message = "Nome não por ser vazio")
    var name:String,
    @field:Email(message = "Email inválido")
    @EmailAvailable
    var email:String,
    @field:NotEmpty(message = "Senha deve ser informada")
    var password:String,
    var roles:Set<Roles>?=null,
)
