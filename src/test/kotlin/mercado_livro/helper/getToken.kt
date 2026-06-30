package mercado_livro.helper

import mercado_livro.model.CustomerModel
import mercado_livro.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class Token{
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    fun getAccessToken(customer: CustomerModel): String {

        val loginRequest = mapOf("email" to customer.email, "password" to customer.password)
        val result = mockMvc.perform(
            post("/login")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        val token = result.response.getHeader("Authorization") ?: ""

        return token
    }
}
