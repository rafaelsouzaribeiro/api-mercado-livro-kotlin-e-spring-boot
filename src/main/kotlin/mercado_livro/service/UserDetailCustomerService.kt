package mercado_livro.service

import mercado_livro.expection.AuthenticationException
import mercado_livro.repository.CustomerRepository
import mercado_livro.security.UserCustomDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailCustomerService(
    private val customerRepository: CustomerRepository
):UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
       val customer = customerRepository.findById(id).orElseThrow{AuthenticationException("Falha ao autenticar","999")}
       return UserCustomDetails(customer)
    }
}