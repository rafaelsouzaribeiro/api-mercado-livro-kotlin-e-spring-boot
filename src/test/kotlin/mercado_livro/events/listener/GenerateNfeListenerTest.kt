package mercado_livro.events.listener

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import mercado_livro.events.PurchaseEvent
import mercado_livro.helper.buildPurchase
import mercado_livro.service.PurchaseService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class GenerateNfeListenerTest{

    @MockK
    private lateinit var purchaseService: PurchaseService
    @InjectMockKs
    private lateinit var generateNfeListener: GenerateNfeListener

    @Test
    fun `should generate nfe`(){
        val purchase= buildPurchase(nfe = null)
        val fakeNfe = UUID.randomUUID()
        val purchaseExpected= purchase.copy(nfe = fakeNfe.toString())
        mockkStatic(UUID::class)

        every { UUID.randomUUID() } returns fakeNfe
        every { purchaseService.update(purchaseExpected) } just runs
        generateNfeListener.listener(PurchaseEvent(this,purchase))

        verify(exactly = 1) { purchaseService.update(purchaseExpected)  }
    }

}