package mercado_livro.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import mercado_livro.enums.CustomerStatus
import mercado_livro.enums.Roles
import mercado_livro.expection.NotFoundException
import mercado_livro.model.CustomerModel
import mercado_livro.repository.CustomerRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @InjectMockKs
    private lateinit var customerService: CustomerService

    @MockK
    private lateinit var customerRepository: CustomerRepository
    @MockK
    private lateinit var bookService: BookService
    @MockK
    private lateinit var bcrypt: BCryptPasswordEncoder

    private fun buildCustomers(
        id:String? = null,
        name:String = "Customer name",
        email:String="${UUID.randomUUID()}@email.com",
        password:String="password"
    ):CustomerModel{
        return CustomerModel(
            id=id,
            name=name,
            email=email,
            status = CustomerStatus.ACTIVE,
            password=password,
            roles = setOf(Roles.CUSTOMER)
        )
    }

    @Test
    fun `should return all customers`() {
        val fackeCustomers= listOf(buildCustomers(),buildCustomers())
        every { customerRepository.findAll() } returns fackeCustomers

        val customers= customerService.getAll(null)

        assertEquals(fackeCustomers,customers)
        verify(exactly = 1) { customerRepository.findAll()  }
        verify(exactly = 0) { customerRepository.findByNameContaining(any())  }

    }

    @Test
    fun `should return customers when name is informed`() {
        val name= UUID.randomUUID().toString()

        val fackeCustomers= listOf(buildCustomers(),buildCustomers())
        every { customerRepository.findByNameContaining(name) } returns fackeCustomers

        val customers= customerService.getAll(name)

        assertEquals(fackeCustomers,customers)
        verify(exactly = 0) { customerRepository.findAll()  }
        verify(exactly = 1) { customerRepository.findByNameContaining(any())  }

    }

    @Test
    fun `should created customer and encrypt password`(){
        val initialPassword = Math.random().toString()
        val fakeCustomer = buildCustomers(password = initialPassword)
        val fakePassword = UUID.randomUUID().toString()
        val fakeCustomerEncrypt=fakeCustomer.copy(password = fakePassword)

        every { customerRepository.save(fakeCustomerEncrypt) }returns fakeCustomer
        every { bcrypt.encode(initialPassword) }returns fakePassword

        customerService.createCustomer(fakeCustomer)

        verify(exactly = 1)  { customerRepository.save(fakeCustomerEncrypt) }
        verify(exactly = 1) {bcrypt.encode(initialPassword)  }
    }

    @Test
    fun `should return customer by id`(){
        val id = UUID.randomUUID()
        val fakeCustomer = buildCustomers(id=id.toString())

        every { customerRepository.findById(id.toString()) }returns Optional.of(fakeCustomer)

        val customer = customerService.getById(id)
        assertEquals(fakeCustomer,customer)

        verify(exactly = 1) { customerRepository.findById(id.toString()) }
    }

    @Test
    fun `should throw error when customer not found`(){
        val id = UUID.randomUUID()
        every { customerRepository.findById(id.toString()) }returns Optional.empty()

        val error = assertThrows<NotFoundException> {
            customerService.getById(id)
        }

        assertEquals("Customer {${id}} not exists",error.message)
        assertEquals("ML_201",error.errorCode)
        verify(exactly = 1) { customerRepository.findById(id.toString()) }
    }

}