package mercado_livro.model

import jakarta.persistence.*
import mercado_livro.enums.BookStatus
import mercado_livro.enums.CustomerStatus
import org.hibernate.annotations.JdbcTypeCode
import java.sql.Types

@Entity(name = "customer")
data class CustomerModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    @JdbcTypeCode(Types.VARCHAR)
    var id: String? = null,
    @Column(nullable = false)
    var name:String,
    @Column(nullable = false, unique = true)
    var email:String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus?=null,
)
