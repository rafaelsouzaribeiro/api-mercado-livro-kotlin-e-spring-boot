package mercado_livro.expection

class AuthenticationException(
        override val message:String,
        val errorCode:String
    ):Exception()