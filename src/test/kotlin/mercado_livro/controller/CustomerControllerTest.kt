package mercado_livro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import mercado_livro.controller.request.PostCustomerRequest
import mercado_livro.controller.request.PutCustomerRequest
import mercado_livro.enums.CustomerStatus
import mercado_livro.enums.Roles
import mercado_livro.helper.buildCustomers
import mercado_livro.repository.CustomerRepository
import mercado_livro.security.UserCustomDetails
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals


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

    @BeforeEach
    fun seTup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should return all customer`() {
        val user1 = buildCustomers(
            email = "user1@email.com",
            roles = setOf(Roles.ADMIN)
        )

        val user2 = buildCustomers(
            email = "user2@email.com",
        )

        customerRepository.save(user1)
        customerRepository.save(user2)
        mockMvc.perform(
            get("/customer").with(user(UserCustomDetails(user1)))
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
        val user1 = buildCustomers(
            name = "Gustavo",
            roles = setOf(Roles.ADMIN)
        )

        val user2 = buildCustomers(
            name = "Daniel",
            roles = setOf(Roles.ADMIN)
        )

        customerRepository.save(user1)
        customerRepository.save(user2)
        mockMvc.perform(
            get("/customer?name=Gu").with(user(UserCustomDetails(user1)))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(user1.id))
            .andExpect(jsonPath("$[0].name").value(user1.name))
            .andExpect(jsonPath("$[0].email").value(user1.email))
            .andExpect(jsonPath("$[0].status").value(user1.status!!.name))
    }

    @Test
    fun `should create customer`() {
        val request = PostCustomerRequest("fake name","${Random.nextInt()}@fake.com","1234")

        mockMvc.perform(
            post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)

        val customers = customerRepository.findAll().toList()
        assertEquals(1,customers.size)
        assertEquals(request.name,customers[0].name)
        assertEquals(request.email,customers[0].email)

    }

    @Test
    fun `should throw error when customer has invalid information`() {
        val request = PostCustomerRequest("","${Random.nextInt()}@fake.com","1234")

        mockMvc.perform(
            post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid request"))
            .andExpect(jsonPath("$.internalCode").value("ML001"))

        val customers = customerRepository.findAll().toList()
        assertEquals(0,customers.size)
        assertEquals(request.name,"")


    }

    @Test
    fun `should get user by id when user has the same id`() {
        val user1 = buildCustomers(
            name = "Gustavo",
            roles = setOf(Roles.ADMIN)
        )

        customerRepository.save(user1)
        mockMvc.perform(
            get("/customer/${user1.id}").with(user(UserCustomDetails(user1)))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(user1.id))
            .andExpect(jsonPath("$.name").value(user1.name))
            .andExpect(jsonPath("$.email").value(user1.email))
            .andExpect(jsonPath("$.status").value(user1.status!!.name))
    }

    @Test
    fun `should return forbidden when user has different id`() {
        val id = UUID.randomUUID().toString()
        val user1 = buildCustomers(
            name = "Gustavo",
        )

        customerRepository.save(user1)
        mockMvc.perform(
            get("/customer/$id").with(user(UserCustomDetails(user1)))
        )
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.httpCode").value(403))
            .andExpect(jsonPath("$.message").value("Unauthorized"))
            .andExpect(jsonPath("$.internalCode").value("ML000"))

    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should get user by id when user is admin`() {
        val id = UUID.randomUUID().toString()
        val user1 = buildCustomers(
            name = "Gustavo",
        )

        customerRepository.save(user1)
        mockMvc.perform(
            get("/customer/$id")
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer {$id} not exists"))
            .andExpect(jsonPath("$.internalCode").value("ML_201"))

    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should update customer`() {
        val customer = buildCustomers()
        val request = PostCustomerRequest("fake name","${Random.nextInt()}@fake.com","1234")
        customerRepository.save(customer)

        mockMvc.perform(
            put("/customer/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNoContent)

        val customers = customerRepository.findAll().toList()
        assertEquals(1,customers.size)
        assertEquals(request.name,customers[0].name)
        assertEquals(request.email,customers[0].email)

    }

    @Test
    fun `should throw error when update customer has invalid information`() {
        val request = PutCustomerRequest(
            "",
            "${Random.nextInt()}@fake.com",
            password = "123")

        mockMvc.perform(
            put("/customer/${UUID.randomUUID()}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid request"))
            .andExpect(jsonPath("$.internalCode").value("ML001"))

        val customers = customerRepository.findAll().toList()
        assertEquals(0,customers.size)
        assertEquals(request.name,"")


    }

    @Test
    fun `should return not found when update customer not exists`() {
        val id = UUID.randomUUID()
        val request = PutCustomerRequest(
            "",
            "${Random.nextInt()}@fake.com",
            password = "123")

        mockMvc.perform(
            put("/customer/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid request"))
            .andExpect(jsonPath("$.internalCode").value("ML001"))

        val customers = customerRepository.findAll().toList()
        assertEquals(0,customers.size)
        assertEquals(request.name,"")


    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should delete customer`(){
        val customer = buildCustomers()
        customerRepository.save(customer)
        mockMvc.perform(delete("/customer/${customer.id}"))
            .andExpect(status().isNoContent)

        val customerDelete = customerRepository.findById(customer.id!!)
        assertEquals(CustomerStatus.INACTIVE,customerDelete.get().status)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should return not found when delete customer not exsts`(){
        val id = UUID.randomUUID()
        mockMvc.perform(delete("/customer/$id"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer {$id} not exists"))
            .andExpect(jsonPath("$.internalCode").value("ML_201"))
    }
}


