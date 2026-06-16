package mercado_livro.controller.response

import mercado_livro.enums.CustomerStatus

data class CustomerResponse(
    var id: String? = null,
    var name:String,
    var email:String,
    var status: CustomerStatus?=null,
)
