package mercado_livro.enums

enum class Errors(val code:String,val message:String) {
    ML001("ML001","Invalid request"),
    ML101("ML_101","Book {%s} not exists"),
    ML102("ML_102","Cannot update book with status {%s}"),
    ML201("ML_201","Customer {%s} not exists"),
    ML301("ML_301","UUID {%s} invalid"),
    ML401("ML_401","Invalid JSON format")
}