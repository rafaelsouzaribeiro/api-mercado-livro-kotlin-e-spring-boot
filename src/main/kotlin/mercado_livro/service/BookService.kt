package mercado_livro.service

import mercado_livro.enums.BookStatus
import mercado_livro.enums.Errors
import mercado_livro.expection.NotFoundException
import mercado_livro.model.BookModel
import mercado_livro.model.CustomerModel
import mercado_livro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BookService(
   private val bookRepository: BookRepository
) {
    fun create(toBookModel: BookModel) {
        bookRepository.save(toBookModel)
    }

    fun findAll(pageable: Pageable):Page<BookModel>{
        return bookRepository.findAll(pageable)
    }

    fun findActive(pageable: Pageable): Page<BookModel> {
        return bookRepository.findByStatus(BookStatus.ACTIVE,pageable)
    }

    fun findById(id:UUID): BookModel =
        bookRepository.findById(id.toString()).orElseThrow{
            NotFoundException(Errors.ML101.message.format(id),Errors.ML101.code)
        }

    fun deleteById(id: UUID) {
        val book = findById(id)
        book.status=BookStatus.DELETED
        bookRepository.save(book)

    }

    fun update(toBookModel: BookModel) {
        bookRepository.save(toBookModel)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        val books = bookRepository.findByCustomer(customer)

        for(book in books){
           book.status=BookStatus.DELETED
        }

        bookRepository.saveAll(books)
    }

    fun fidAllByIds(booksIds: Set<UUID>): List<BookModel> {
        val ids=booksIds.map { it.toString() }.toList()
        return bookRepository.findAllById(ids)
    }

    fun purchase(books: List<BookModel>) {
        books.map {
            it.status=  BookStatus.SOLD
        }

        bookRepository.saveAll(books)
    }

}