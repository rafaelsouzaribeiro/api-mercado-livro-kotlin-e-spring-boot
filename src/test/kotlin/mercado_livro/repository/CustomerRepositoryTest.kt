package mercado_livro.repository

import io.mockk.junit5.MockKExtension
import mercado_livro.helper.buildCustomers
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun seTup()= customerRepository.deleteAll()

    @Test
    fun `should return name containing`(){
       val marcos= customerRepository.save(
            buildCustomers(name = "Marcos")
        )
        val mateus= customerRepository.save(
            buildCustomers(name = "Mateus")
        )


        val customers = customerRepository.findByNameContaining("Ma")
        assertEquals(listOf(mateus,marcos),customers)
    }

    @Nested
    inner class `exists by email`{
        @Test
        fun `should return true when email exists`() {
            val email = "rafael@test.com.br"
                customerRepository.save(
                buildCustomers(email = email)
            )
            val exists = customerRepository.existsByEmail(email)
            assertTrue(exists)
        }

        @Test
        fun `should return false when email do not exists`(){
            val email="noexists@test.com.br"
            val exists = customerRepository.existsByEmail(email)
            assertFalse(exists)
        }
    }

    @Nested
    inner class `find by email`{
        @Test
        fun `should return customer when email exists`() {
            val email = "rafael@test.com.br"
            val customer = customerRepository.save(
                buildCustomers(email = email)
            )
            val customerFind = customerRepository.findByEmail(email)
            assertNotNull(customerFind)
            assertEquals(customer,customerFind)
        }

        @Test
        fun `should return customer when email do not exists`(){
            val email="noexists@test.com.br"
            val exists = customerRepository.findByEmail(email)
            assertNull(exists)
        }
    }

}