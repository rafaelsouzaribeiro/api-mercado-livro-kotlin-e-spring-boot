package mercado_livro.enums

enum class Errors(val code:String,val message:String) {
    MLOOO("ML000","Unauthorized"),
    ML001("ML001","Invalid request"),
    ML002("ML002","Email already exists"),
    ML101("ML_101","Book {%s} not exists"),
    ML102("ML_102","Cannot update book with status {%s}"),
    ML_103("ML_103","You cannot sell book {%s} that are not active."),
    ML201("ML_201","Customer {%s} not exists"),
    ML301("ML_301","UUID {%s} invalid"),
    ML401("ML_401","Invalid JSON format")
}