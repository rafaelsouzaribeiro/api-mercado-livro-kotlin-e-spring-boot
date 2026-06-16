package mercado_livro.expection

class NotFoundException(
        override val message:String,
        val errorCode:String
    ):Exception()