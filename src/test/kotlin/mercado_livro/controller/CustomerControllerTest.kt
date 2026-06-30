package mercado_livro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import mercado_livro.enums.Roles
import mercado_livro.helper.Token
import mercado_livro.helper.buildCustomers
import mercado_livro.repository.CustomerRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class CustomerControllerTest{
    @Autowired
    private lateinit var mockMvc:MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var  objectMapper: ObjectMapper

    @Autowired
    private lateinit var tokenHelper: Token

    @BeforeEach
    fun seTup()=customerRepository.deleteAll()

    @AfterEach
    fun tearDown()=customerRepository.deleteAll()

    @Test
    fun `should return all customer`(){
        val passwordNormal = "password123"

        val passwordEncrypt = org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(passwordNormal)

        val adminUser = buildCustomers(
            email = "admin@email.com",
            password = passwordEncrypt,
            roles = setOf(Roles.ADMIN)
        )

        val adminUser2 = buildCustomers(
            email = "admin2@email.com",
            password = passwordEncrypt,
            roles = setOf(Roles.ADMIN)
        )
        customerRepository.save(adminUser)
        customerRepository.save(adminUser2)

        val userLogin = adminUser.copy(password = passwordNormal)
        val token = tokenHelper.getAccessToken(userLogin)

        mockMvc.perform(
            get("/customer")
                .header("Authorization", token)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(adminUser.id))
            .andExpect(jsonPath("$[0].name").value(adminUser.name))
            .andExpect(jsonPath("$[0].email").value(adminUser.email))
            .andExpect(jsonPath("$[0].status").value(adminUser.status!!.name))
            .andExpect(jsonPath("$[1].id").value(adminUser2.id))
            .andExpect(jsonPath("$[1].name").value(adminUser2.name))
            .andExpect(jsonPath("$[1].email").value(adminUser2.email))
            .andExpect(jsonPath("$[1].status").value(adminUser2.status!!.name))
    }
}