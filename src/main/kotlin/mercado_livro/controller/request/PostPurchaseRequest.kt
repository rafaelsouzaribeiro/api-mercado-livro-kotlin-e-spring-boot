package mercado_livro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class PostPurchaseRequest (
    @field:NotNull
    @JsonAlias("customer_id")
    val customerId:UUID,

    @field:NotNull
    @JsonAlias("book_ids")
    val bookIds:Set<UUID>,
)



