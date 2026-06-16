package mercado_livro.repository

import mercado_livro.enums.BookStatus
import mercado_livro.model.BookModel
import mercado_livro.model.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<BookModel, String> {
    fun findByStatus(status: BookStatus,pageable: Pageable): Page<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>


}