package mercado_livro.service

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import mercado_livro.events.PurchaseEvent
import mercado_livro.helper.buildPurchase
import mercado_livro.repository.PurchaseRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {
    @MockK
    private lateinit var purchaseRepository: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    private var purchaseEvntSlot = slot<PurchaseEvent>()

    @Test
    fun `should create purchase and publish event`(){
        val purchase = buildPurchase()

        every { purchaseRepository.save(purchase) } returns  purchase
        every { applicationEventPublisher.publishEvent(any()) } just runs

        purchaseService.create(purchase)

        verify(exactly = 1) { purchaseRepository.save(purchase)  }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEvntSlot))   }

        assertEquals(purchase,purchaseEvntSlot.captured.purchaseModel)
    }

    @Test
    fun `should update purchase`(){
        val purchase = buildPurchase()

        every { purchaseRepository.save(purchase) } returns  purchase

        purchaseService.update(purchase)

        verify(exactly = 1) { purchaseRepository.save(purchase)  }

    }
}