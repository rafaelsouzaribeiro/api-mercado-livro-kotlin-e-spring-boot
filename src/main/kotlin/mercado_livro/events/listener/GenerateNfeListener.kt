package mercado_livro.events.listener

import mercado_livro.events.PurchaseEvent
import mercado_livro.service.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GenerateNfeListener(
    private val purchaseService: PurchaseService
) {

    @Async
    @EventListener
    fun listener(purchaseEvent: PurchaseEvent){
        val nfe = UUID.randomUUID().toString()
        val purchaseModel=purchaseEvent.purchaseModel.copy(nfe=nfe)
        purchaseService.update(purchaseModel)
    }
}