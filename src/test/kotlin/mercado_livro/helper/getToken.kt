package mercado_livro.helper

import com.fasterxml.jackson.databind.ObjectMapper
import mercado_livro.model.CustomerModel
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Component
class Token(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    fun getAccessToken(customer: CustomerModel): String {
        val loginRequest = mapOf("email" to customer.email, "password" to customer.password)

        val result = mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        return result.response.getHeader("Authorization") ?: ""
    }
}