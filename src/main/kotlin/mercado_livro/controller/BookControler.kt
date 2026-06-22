package mercado_livro.controller

import jakarta.validation.Valid
import mercado_livro.controller.response.BookResponse
import mercado_livro.extension.toBookModel
import mercado_livro.extension.toResponse
import mercado_livro.controller.request.PostBookRequest
import mercado_livro.controller.request.PutBookRequest
import mercado_livro.controller.response.PageResponse
import mercado_livro.extension.toPageResponse
import mercado_livro.service.BookService
import mercado_livro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("book")
class BookControler(
    private val customerService: CustomerService,
    private val bookService: BookService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody @Valid requestBody: PostBookRequest){
        val cutomerId = customerService.getById(requestBody.customerId)
        bookService.create(requestBody.toBookModel(cutomerId))
    }

    @GetMapping
    fun findAll(@PageableDefault(page = 0, size = 10) pageable: Pageable): PageResponse<BookResponse> {
        return bookService.findAll(pageable).map { it.toResponse() }.toPageResponse()
    }

    @GetMapping("/active")
    fun findActive(@PageableDefault(page = 0, size = 10) pageable: Pageable):Page<BookResponse>{
        return bookService.findActive(pageable).map { it.toResponse() }
    }

    @GetMapping("{id}")
    fun findById(@PathVariable id:UUID):BookResponse =
        bookService.findById(id).toResponse()

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id:UUID){
        bookService.deleteById(id)
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id:UUID,@RequestBody @Valid request: PutBookRequest){
        val book = bookService.findById(id)
        bookService.update(request.toBookModel(book))
    }

}