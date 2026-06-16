package mercado_livro.controller.request

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class PostPurchaseRequest (
    @field:NotNull
    val customerId:UUID,

    @field:NotNull
    val bookIds:Set<UUID>,
)



