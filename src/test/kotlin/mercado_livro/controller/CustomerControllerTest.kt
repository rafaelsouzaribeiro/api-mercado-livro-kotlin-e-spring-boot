package mercado_livro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import mercado_livro.enums.Roles
import mercado_livro.helper.Token
import mercado_livro.helper.buildCustomers
import mercado_livro.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class CustomerControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var tokenHelper: Token

    @BeforeEach
    fun seTup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Nested
    inner class Customer {
        private fun getToken(): String {
            val passwordNormal = "password123"

            val passwordEncrypt =
                org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(passwordNormal)

            val adminUser = buildCustomers(
                email = "admin@email.com",
                password = passwordEncrypt,
                roles = setOf(Roles.ADMIN)
            )

            customerRepository.save(adminUser)

            val userLogin = adminUser.copy(password = passwordNormal)
            return tokenHelper.getAccessToken(userLogin)
        }

        @Test
        fun `should return all customer`() {
            val token = getToken()
            val user1 = buildCustomers(
                email = "user1@email.com",
            )

            val user2 = buildCustomers(
                email = "user2@email.com",
            )

            customerRepository.save(user1)
            customerRepository.save(user2)
            mockMvc.perform(
                get("/customer")
                    .header("Authorization", token)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$[?(@.email == 'user1@email.com')].id").value(user1.id))
                .andExpect(jsonPath("$[?(@.email == 'user1@email.com')].name").value(user1.name))
                .andExpect(jsonPath("$[?(@.email == 'user1@email.com')].status").value(user1.status!!.name))
                .andExpect(jsonPath("$[?(@.email == 'user2@email.com')].id").value(user2.id))
                .andExpect(jsonPath("$[?(@.email == 'user2@email.com')].name").value(user2.name))
                .andExpect(jsonPath("$[?(@.email == 'user2@email.com')].status").value(user2.status!!.name))
        }

        @Test
        fun `should return when name customer`() {
            val token = getToken()
            val user1 = buildCustomers(
                name = "Gustavo"
            )

            val user2 = buildCustomers(
                name = "Daniel"
            )

            customerRepository.save(user1)
            customerRepository.save(user2)
            mockMvc.perform(
                get("/customer?name=Gu")
                    .header("Authorization", token)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(user1.id))
                .andExpect(jsonPath("$[0].name").value(user1.name))
                .andExpect(jsonPath("$[0].email").value(user1.email))
                .andExpect(jsonPath("$[0].status").value(user1.status!!.name))
        }
    }
}


