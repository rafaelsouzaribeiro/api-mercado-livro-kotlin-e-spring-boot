package mercado_livro.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import mercado_livro.enums.CustomerStatus
import mercado_livro.enums.Roles
import mercado_livro.validation.EmailAvailable

data class PutCustomerAdminRequest(
    @field:NotEmpty(message = "Nome não por ser vazio")
    var name:String,
    @field:Email(message = "Email inválido")
    @EmailAvailable
    var email:String,
    var status: CustomerStatus?=null,
    @field:NotEmpty(message = "Senha não pode ser vazia")
    var password:String,
    var roles:Set<Roles>?=null
)
