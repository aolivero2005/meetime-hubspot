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


# Para possíveis melhorias

1. ### **Validação e Segurança**
Atualmente, valores sensíveis como `clientId`, `secretClientId` e outros dados de configuração estão diretamente 
utilizando `@Value`. Uma melhoria seria usar um cofre de segredos (como AWS Secrets Manager, Vault, ou Spring 
Cloud Vault) para armazenar essas credenciais de forma segura.

Garantir validação adequada em endpoints e serviços, como no `HubSpotContactRequest` (e.g., formatar e validar o email) 
usando anotações do Jakarta Bean Validation como `@Valid` e `@Email`.

Implementar algo, como rate limiting, para proteger os endpoints, especialmente os que recebem Webhooks, contra ataques 
como DDoS ou spam.

2. ### **Eficiência e Escalabilidade**

Caso os tokens de autenticação sejam reutilizáveis por determinados períodos, um cache poderia ser implementado para 
evitar múltiplas trocas de código/token desnecessárias. Utilizar frameworks como Spring Cache com Redis pode ajudar.

Processamento paralelo de eventos recebidos no método `webhookReceived`, utilizando algo como `CompletableFuture` no 
Spring, pode ser uma solução para lidar com grandes volumes de eventos.

3. ### **Observabilidade e Transparência**

Adicione valores únicos (como trace IDs ou request IDs) ao log para rastrear requisições de ponta a ponta

Bibliotecas como Micrometer (já integrada ao Spring Boot) para expor métricas de performance das APIs, como tempos de resposta, número de erros, etc.

4. ### **Manutenção**

Separação de Configurações por Ambiente, garantir que todas as variáveis configuráveis (como redirects Uri, clientId) 
estejam separadas para ambientes como produção, staging e desenvolvimento.

Testes unitários para cada método.

Testes de integração para fluxos que dependem da API do HubSpot.

Simular Webhooks para garantir que o comportamento esperado acontece conforme os eventos recebidos.












