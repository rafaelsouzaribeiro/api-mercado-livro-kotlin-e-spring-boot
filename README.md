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
Para alterar o ambiente de produção(prod) para desenvolvimento(dev)

Basta mudar o profiles.active(dev ou prod)

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

![Token](http://rafael-developer.com/wp-content/uploads/2026/06/Captura-de-Tela-2026-06-23-as-05.49.32.png)


## Acesso ao perfil ADMIN

Para acessar as funcionalidades administrativas, é necessário alterar o perfil do usuário no banco de dados.

Execute a atualização na tabela `customer_role`, substituindo o valor `CUSTOMER` por `ADMIN`

## 📌 API Endpoints

### Livros (Books)

| Método     | Endpoint                        | Descrição                              |
|------------|---------------------------------|----------------------------------------|
| `GET`      | `/book`                         | Listar todos os livros                 |
| `GET`      | `/book/active`                  | Listar livros ativos                   |
| `GET`      | `/book?page=0&size=13`          | Listar livros com paginação            |
| `GET`      | `/book/{id}`                    | Buscar livro por ID                    |
| `POST`     | `/book`                         | Cadastrar novo livro                   |
| `PUT`      | `/book/{id}`                    | Atualizar livro                        |
| `DELETE`   | `/book/{id}`                    | Remover livro                          |

### Clientes (Customers)

| Método     | Endpoint                        | Descrição                              |
|------------|---------------------------------|----------------------------------------|
| `GET`      | `/customer`                     | Listar todos os clientes               |
| `GET`      | `/customer/{id}`                | Buscar cliente por ID                  |
| `POST`     | `/customer`                     | Cadastrar novo cliente                 |
| `POST`     | `/customer/admin`               | Cadastrar cliente como Admin           |
| `PUT`      | `/customer/{id}`                | Atualizar cliente                      |
| `PUT`      | `/customer/admin/{id}`          | Atualizar cliente como Admin           |
| `DELETE`   | `/customer/{id}`                | Remover cliente                        |

### Compras (Purchases)

| Método     | Endpoint                        | Descrição                              |
|------------|---------------------------------|----------------------------------------|
| `POST`     | `/purchase`                     | Realizar uma compra                    |
| `GET`      | `/purchase/{customer_id}`       | Listar compras de um cliente           |

## 📖 Documentação da API (Swagger)

A documentação da API está disponível através do Swagger.

> **Importante:** o Swagger só estará disponível quando a aplicação for executada com o profile `dev`.

Acesse a URL abaixo no navegador:

```text
http://localhost:8080/swagger-ui/index.html
```