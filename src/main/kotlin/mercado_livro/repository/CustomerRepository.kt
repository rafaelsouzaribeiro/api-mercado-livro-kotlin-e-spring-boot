package mercado_livro.repository

import mercado_livro.model.CustomerModel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : CrudRepository<CustomerModel,String>{
    fun findByNameContaining(name:String):List<CustomerModel>
    fun existsByEmail(value: String): Boolean
    fun findByEmail(email: String):CustomerModel?

}

