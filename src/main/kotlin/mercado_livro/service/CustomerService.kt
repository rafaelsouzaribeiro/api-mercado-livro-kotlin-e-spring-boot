package mercado_livro.service

import mercado_livro.enums.CustomerStatus
import mercado_livro.enums.Errors
import mercado_livro.enums.Roles
import mercado_livro.expection.NotFoundException
import mercado_livro.model.CustomerModel
import mercado_livro.repository.CustomerRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val bookService: BookService,
    private val bcrypt:BCryptPasswordEncoder
) {

    fun getAll(name:String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(name)
        }
        return customerRepository.findAll().toList()
    }

    fun createCustomer(customerModel: CustomerModel){
        val customerCopy = customerModel.copy(
            roles = setOf(Roles.CUSTOMER),
            password = bcrypt.encode(customerModel.password)
        )
        customerRepository.save(customerCopy)
    }

    fun getById(ids:UUID): CustomerModel {
        return customerRepository.findById(ids.toString()).orElseThrow{
            NotFoundException(
                Errors.ML201.message.format(ids),
                Errors.ML101.code)
        }
    }

    fun updateCustomer(customerModel: CustomerModel){
        if(!customerRepository.existsById(customerModel.id!!)){
            throw Exception()
        }
        customerRepository.save(customerModel)
    }

    fun deleteCustomer(id:UUID){
        val customers = getById(id)
        bookService.deleteByCustomer(customers)

        val customer = getById(id)
        customer.status=CustomerStatus.INACTIVE
        updateCustomer(customer)
    }

    fun emailAvailable(value:String): Boolean {
        return !customerRepository.existsByEmail(value)
    }

}