# 📚 Mercado Livro API

API desenvolvida com **Kotlin + Spring Boot + Spring Security + JWT**, utilizando JPA e MySQL, com separação de perfis (dev e prod).

---

# 🚀 Tecnologias utilizadas

- Kotlin
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Bean Validation
- Jackson

---

# ⚙️ Configuração da aplicação

## 📁 `application.yml`

Crie em:
<br />
<br />
src/main/resources/application.yml

```yaml
spring:
  profiles:
    active: dev

  application:
    name: demo

  jpa:
    hibernate:
      ddl-auto: update

  jackson:
    default-property-inclusion: non_null

jwt:
  secret: 7c34debd-b72f-4941-96cb-89aaeafc029d
  expiration: 3600000
```

Crie em:

src/main/resources/application-dev.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mercadolivro?createDatabaseIfNotExist=true
    username: root
    password: root
```

Crie em:

src/main/resources/application-prod.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mercadolivroprod?createDatabaseIfNotExist=true
    username: root
    password: root
```

📌 Endpoint: Criar Customer

➤ URL

POST /customer

```json
{
"name": "João Silva",
"email": "joao@email.com",
"password": "123456"
}
```

📌 Endpoint: Login

➤ URL

POST /login

```json
{
"email": "joao@email.com",
"password": "123456"
}
```

Pegue do header do login o token e acesse as outras rotas.

[![Token](http://rafael-developer.com/wp-content/uploads/2026/06/Captura-de-Tela-2026-06-23-as-05.49.32.png)]


Para acessar conteúdo ADMIN mude o usuário CUSTOMER no banco para ADMIN