package mercado_livro.repository

import mercado_livro.model.PurchaseModel
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseRepository:CrudRepository<PurchaseModel,String>


