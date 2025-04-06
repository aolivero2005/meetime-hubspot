# Em geral

Para a implementação deste exemplo e devido ao curto tempo utilizado para sua realização (apenas meio dia) foi utilizada uma arquitetura de camadas, suficiente para a realização deste exemplo, utilizando o lombok para facilitar a criação de getters e setters, bem como os construtores padrão.

No caso da integração com o HubSpot, usamos o RestTemplate para enviar e receber dados da API do HubSpot, como:

Solicitar o access token via OAuth 2.0
Criar contatos enviando um JSON com os dados do usuário
Enviar ou receber informações de webhooks

Vantagens de usar RestTemplate:

✅ Simplicidade: Fácil de configurar e usar, ideal para chamadas HTTP diretas.

✅ Integração com o Spring: Ele se integra bem com o restante da stack Spring (injeção de dependência, tratamento de exceções, etc.).

✅ Suporte completo a HTTP: Permite fazer requisições GET, POST, PUT, DELETE etc., com facilidade.

# Aplicação Spring Boot - Integração com HubSpot

Este projeto é uma aplicação Spring Boot que fornece endpoints REST para integração com o HubSpot. Ele permite gerar uma URL de autorização, tratar o callback da autorização, criar contatos e receber eventos via webhook.

## Pré-requisitos

- Java 17 ou superior
- Maven ou Gradle
- Conta no HubSpot com acesso à criação de aplicativos (para configurar client ID e client secret)
- Variáveis de ambiente ou `application.properties` configurados com as credenciais do HubSpot

## Executando a aplicação

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

# 2. Configure as variáveis necessárias
Certifique-se de que as seguintes propriedades estão configuradas corretamente no seu application.yml ou como variáveis de ambiente:

```bash
hubspot.client.id=0c76dd06-ca6b-49b2-8602-258574aba1cd
hubspot.secret_client.id=26cd2619-7e08-47c0-b41f-885a4b65c9ab
hubspot.redirect.uri=http://localhost:8080
```

# 3. Compile e execute o projeto
Se estiver usando Maven:
```bash
./mvnw spring-boot:run
```
Ou com Gradle:
```bash
./gradlew bootRun
```
A aplicação será iniciada na porta padrão 8080.

# Endpoints disponíveis

### Gera e retorna a URL de autorização do HubSpot.
GET /hubspot/authorize-url

### Endpoint chamado pelo HubSpot após o usuário autorizar o app. Recebe o code e troca pelo access token.
GET /hubspot/callback?code=XYZ

### Cria um novo contato no HubSpot.
POST /hubspot/contacts?token=ACCESS_TOKEN

Corpo da requisição:
```json
{
  "email": "usuario@exemplo.com",
  "firstName": "Nome",
  "lastName": "Sobrenome"
}
```

### Recebe eventos enviados pelo HubSpot via webhook.
POST /hubspot/webhook_received

Corpo da requisição:
```json
[
  {
    "subscriptionType": "12345",
    "objectId": "contact.creation",
    "propertyName": "Property Name",
    "propertyValue": "Property Value"
  }
]
```

# Testando a aplicação
Você pode testar os endpoints usando ferramentas como Postman, Insomnia ou cURL.

# Coleção de Postman
[HubSpot.postman_collection.json](docs/HubSpot.postman_collection.json)