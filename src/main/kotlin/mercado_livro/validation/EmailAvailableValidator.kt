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

        val customer = customerService.findByEMail(value)

        customer?.let {
            if (customer.email==value){
                return true
            }
        }

        return customerService.emailAvailable(value)
    }

}
