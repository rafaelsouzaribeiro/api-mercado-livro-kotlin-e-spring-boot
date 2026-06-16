package mercado_livro.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import mercado_livro.service.CustomerService

class EmailAvailableValidator(
    var customerService: CustomerService
):ConstraintValidator<EmailAvailable,String> {
    override fun isValid(value: String?, p1: ConstraintValidatorContext?): Boolean {
        if(value.isNullOrEmpty()){
            return false
        }

        return customerService.emailAvailable(value)
    }

}
