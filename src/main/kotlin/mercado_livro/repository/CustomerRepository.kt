package mercado_livro.repository

import mercado_livro.model.BookModel
import mercado_livro.model.CustomerModel
import org.springframework.data.repository.CrudRepository

interface CustomerRepository : CrudRepository<CustomerModel,String>{
    fun findByNameContaining(name:String):List<CustomerModel>
    fun existsByEmail(value: String): Boolean
}

