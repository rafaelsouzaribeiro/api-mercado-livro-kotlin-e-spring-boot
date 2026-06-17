package mercado_livro.service

import mercado_livro.events.PurchaseEvent
import mercado_livro.model.PurchaseModel
import mercado_livro.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher:ApplicationEventPublisher
) {

    fun create(purchaseModel: PurchaseModel){
        purchaseModel.canBePurchased()

        purchaseRepository.save(purchaseModel)
        applicationEventPublisher.publishEvent(PurchaseEvent(source = this,purchaseModel))
    }

    fun update(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }

    fun findPurchase(customerId:String): List<PurchaseModel> {
        return purchaseRepository.findByCustomerId(customerId)
    }
}
