package mercado_livro.security

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasRole('ROLE_ADMIN') || #id.toString() == authentication.principal.id.toString()")
annotation class UserCanOnlyAccessOwnResource
