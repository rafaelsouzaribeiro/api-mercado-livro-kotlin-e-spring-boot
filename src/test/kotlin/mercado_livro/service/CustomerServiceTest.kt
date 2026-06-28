package mercado_livro.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import mercado_livro.enums.CustomerStatus
import mercado_livro.enums.Roles
import mercado_livro.model.CustomerModel
import mercado_livro.repository.CustomerRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.UUID
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

}