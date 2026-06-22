package mercado_livro.extension

import mercado_livro.controller.request.*
import mercado_livro.controller.response.BookResponse
import mercado_livro.controller.response.CustomerResponse
import mercado_livro.enums.BookStatus
import mercado_livro.enums.CustomerStatus
import mercado_livro.model.BookModel
import mercado_livro.model.CustomerModel
import mercado_livro.controller.response.PurchaseSoldResponse
import mercado_livro.model.PurchaseModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(
        name=this.name,
        email=this.email,
        status = CustomerStatus.ACTIVE,
        password = this.password
    )
}

fun PostCustomerAdminRequest.toCustomerAdminModel():CustomerModel{
    return CustomerModel(
        name=this.name,
        email=this.email,
        status = CustomerStatus.ACTIVE,
        password = this.password,
        roles = this.roles
    )
}

fun PutCustomerAdminRequest.toCustomerModel(customer: CustomerModel): CustomerModel {
    return CustomerModel(
        id=customer.id,
        name=this.name,
        email=this.email,
        status =this.status ?: customer.status,
        password = customer.password,
        roles = this.roles
    )
}

fun PutCustomerRequest.toCustomerModel(customer: CustomerModel): CustomerModel {
    return CustomerModel(
        id=customer.id,
        name=this.name,
        email=this.email,
        status =this.status ?: customer.status,
        password = customer.password
    )
}

fun PostBookRequest.toBookModel(customerModel: CustomerModel):BookModel{
    return BookModel(
        name=this.name,
        price = this.price,
        status = BookStatus.ACTIVE,
        customer = customerModel
    )
}

fun PutBookRequest.toBookModel(book: BookModel):BookModel{
    return BookModel(
        id =book.id,
        name=this.name ?: book.name,
        price = this.price ?: book.price,
        status = book.status,
        customer = book.customer
    )
}

fun CustomerModel.toResponse(): CustomerResponse {
    return CustomerResponse(
        id=this.id,
        name=this.name,
        email=this.email,
        status=this.status
    )
}

fun BookModel.toResponse(): BookResponse {
    return BookResponse(
        id=this.id,
        name=this.name,
        status = this.status,
        price = this.price,
        customer = this.customer
    )
}

fun PurchaseModel.toResponse():PurchaseSoldResponse{
    return PurchaseSoldResponse(
        id=this.id,
        customer=this.customer,
        nfe=this.nfe,
        price=this.price,
        books = this.books,
        createdAt=this.createdAt
    )
}