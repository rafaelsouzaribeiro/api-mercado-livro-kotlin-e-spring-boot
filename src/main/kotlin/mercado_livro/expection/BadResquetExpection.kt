package mercado_livro.expection

class BadResquetExpection(
        override val message:String,
        val errorCode:String
    ):Exception()