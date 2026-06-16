package mercado_livro.service

import mercado_livro.model.PurchaseModel
import mercado_livro.repository.PurchaseRepository
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository
) {
    fun create(purchaseModel: PurchaseModel){
        purchaseRepository.save(purchaseModel)
    }
}
