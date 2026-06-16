package mercado_livro.controller

import jakarta.validation.Valid
import mercado_livro.controller.response.CustomerResponse
import mercado_livro.extension.toCustomerModel
import mercado_livro.extension.toResponse
import mercado_livro.controller.request.PostCustomerRequest
import mercado_livro.controller.request.PutCustomerRequest
import mercado_livro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("customer")
class CustomerController(
    val customerService: CustomerService
){

    @GetMapping
    fun getAll(@RequestParam name:String?): List<CustomerResponse> {
       return customerService.getAll(name).map {
           it.toResponse()
       }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBustomer(@RequestBody @Valid customerPost: PostCustomerRequest) {
        customerService.createCustomer(customerPost.toCustomerModel())
    }

    @GetMapping("/{ids}")
    fun getCustomer(@PathVariable ids:UUID): CustomerResponse {
       return customerService.getById(ids).toResponse()
    }

    @PutMapping("{ids}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateCustomer(@PathVariable ids:UUID,@RequestBody @Valid customerPut: PutCustomerRequest){
        val customer = customerService.getById(ids)
        customerService.updateCustomer(customerPut.toCustomerModel(customer))
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: UUID){
        customerService.deleteCustomer(id)
    }

}