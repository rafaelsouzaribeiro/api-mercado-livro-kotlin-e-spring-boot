package mercado_livro.controller

import jakarta.validation.Valid
import mercado_livro.controller.request.PostCustomerAdminRequest
import mercado_livro.controller.request.PostCustomerRequest
import mercado_livro.controller.request.PutCustomerAdminRequest
import mercado_livro.controller.request.PutCustomerRequest
import mercado_livro.controller.response.CustomerResponse
import mercado_livro.extension.toCustomerAdminModel
import mercado_livro.extension.toCustomerModel
import mercado_livro.extension.toResponse
import mercado_livro.security.UserCanOnlyAccessAdmin
import mercado_livro.security.UserCanOnlyAccessOwnResource
import mercado_livro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("customer")
class CustomerController(
    private val customerService: CustomerService
){

    @GetMapping
    @UserCanOnlyAccessOwnResource
    fun getAll(@RequestParam name:String?): List<CustomerResponse> {
       return customerService.getAll(name).map {
           it.toResponse()
       }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody @Valid customerPost: PostCustomerRequest) {
        customerService.createCustomer(customerPost.toCustomerModel())
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    @UserCanOnlyAccessAdmin
    fun createCustomerAdmin(@RequestBody @Valid customerAdminPost: PostCustomerAdminRequest) {
        customerService.createAdminCustomer(customerAdminPost.toCustomerAdminModel())
    }

    @GetMapping("/{id}")
    @UserCanOnlyAccessOwnResource
    fun getCustomer(@PathVariable id:UUID): CustomerResponse {
       return customerService.getById(id).toResponse()
    }

    @PutMapping("{ids}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @UserCanOnlyAccessOwnResource
    fun updateCustomer(@PathVariable ids:UUID,@RequestBody @Valid customerPut: PutCustomerRequest){
        val customer = customerService.getById(ids)
        customerService.updateCustomer(customerPut.toCustomerModel(customer))
    }

    @PutMapping("/admin/{ids}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @UserCanOnlyAccessOwnResource
    fun updateAdminCustomer(@PathVariable ids:UUID,@RequestBody @Valid customerPut: PutCustomerAdminRequest){
        val customer = customerService.getById(ids)
        customerService.updateAdminCustomer(customerPut.toCustomerModel(customer))
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @UserCanOnlyAccessOwnResource
    fun deleteCustomer(@PathVariable id: UUID){
        customerService.deleteCustomer(id)
    }

}