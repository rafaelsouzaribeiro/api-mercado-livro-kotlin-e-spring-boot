package mercado_livro.model

import jakarta.persistence.*
import mercado_livro.enums.BookStatus
import mercado_livro.enums.CustomerStatus
import org.hibernate.annotations.JdbcTypeCode
import java.math.BigDecimal
import java.sql.Types
import java.time.LocalDateTime

@Entity(name = "purchase")
data class PurchaseModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    @JdbcTypeCode(Types.VARCHAR)
    var id: String? = null,
    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: CustomerModel,
    @ManyToMany
    @JoinTable(name = "purchase_book", joinColumns = [JoinColumn(name = "purchase_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")])
    val books:List<BookModel>,
    @Column
    val nfe:String?=null,
    @Column(nullable = false)
    val price:BigDecimal,
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()


)
