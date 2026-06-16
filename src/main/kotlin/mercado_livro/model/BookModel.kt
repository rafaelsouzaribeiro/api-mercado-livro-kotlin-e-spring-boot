package mercado_livro.model

import jakarta.persistence.*
import mercado_livro.enums.BookStatus
import mercado_livro.enums.Errors
import mercado_livro.expection.BadResquetExpection
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.math.BigDecimal
import java.sql.Types

@Entity(name = "book")
data class BookModel(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    @JdbcTypeCode(Types.VARCHAR)
    var id: String? = null,
    @Column(nullable = false)
    var name:String,

    @Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "customer_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_book_customer")
    )
    @OnDelete(action = OnDeleteAction.RESTRICT)
    var customer: CustomerModel?=null
){
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status:BookStatus?=null
        set(value){
            if (field==BookStatus.CANCELED || field == BookStatus.DELETED){
                throw BadResquetExpection(Errors.ML102.message.format(field),Errors.ML102.code)
            }

            field=value
        }
    constructor(
        id: String?=null,
        name: String,
        price: BigDecimal,
        customer: CustomerModel?=null,
        status: BookStatus?=null):this(id,name,price,customer){
            this.status=status
        }

}
