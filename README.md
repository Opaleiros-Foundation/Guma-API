# GUMA API

# By chatGTP muitos erros

## Descrição
A **GUMA API** é uma interface para revisão de tarefas e gerenciamento de cursos e atividades no Canvas. Ela fornece endpoints para verificar, recuperar e gerenciar tarefas acadêmicas, além de permitir a interação com o sistema de monitoramento Spring Boot Actuator.

## Documentação
A API segue o padrão **OpenAPI 3.1.0**.

- **Versão:** 1.0
- **Termos de Serviço:** [Opaleiros Foundation](https://github.com/Opaleiros-Foundation)
- **Licença:** [MIT](https://opensource.org/license/mit/)
- **Contato:** Matheus Victor ([GitHub](https://github.com/Opaleiros-Foundation))

## Servidor
- **Base URL:** `http://localhost:8080`

## Endpoints

### Chat

- **POST `/chat`** - Verifica uma tarefa.
    - **Descrição:** Gera um prompt para verificar uma submissão.
    - **Requisição:** `application/json` ou `multipart/form-data`
    - **Body:**
      ```json
      {
        "subject": "Database 2",
        "professor": "John",
        "heading": "Do A great database"
      }
      ```
    - **Resposta:** Stream de eventos contendo a resposta da verificação.

- **GET `/chat/{lessonId}`** - Obtém uma revisão de tarefa.
    - **Descrição:** Retorna a resposta de uma tarefa através do `lessonId`.
    - **Resposta:**
      ```json
      {
        "lessonId": "123",
        "subject": "Database 2",
        "professor": "John",
        "feedback": "Good job!",
        "model": "GPT-4",
        "responseDate": "2024-06-30"
      }
      ```

### Canvas

- **GET `/canvas/courses`** - Obtém todos os cursos matriculados.
    - **Autenticação:** `access_token` no cabeçalho.
    - **Resposta:**
      ```json
      [
        {
          "id": 1,
          "name": "Software Engineering",
          "account_id": 1001,
          "uuid": "abc-123",
          "course_code": "SE-101"
        }
      ]
      ```

- **GET `/canvas/assignments/{courseId}`** - Obtém todas as tarefas de um curso.
    - **Autenticação:** `access_token` no cabeçalho.
    - **Resposta:**
      ```json
      [
        {
          "id": 10,
          "name": "Database Project",
          "description": "Design a relational database",
          "due_at": "2024-07-10T23:59:59Z"
        }
      ]
      ```

- **GET `/canvas/assignments/{courseId}/{assignmentId}`** - Obtém uma tarefa específica de um curso.
    - **Autenticação:** `access_token` no cabeçalho.
    - **Resposta:**
      ```json
      {
        "id": 10,
        "name": "Database Project",
        "description": "Design a relational database",
        "due_at": "2024-07-10T23:59:59Z"
      }
      ```

### Actuator

- **GET `/actuator`** - Retorna os links do Spring Boot Actuator.
- **GET `/actuator/health`** - Verifica o status de saúde da API.

## Modelos de Dados

### `ChatDTO`
```json
{
  "subject": "Database 2",
  "professor": "John",
  "heading": "Do A great database"
}
```

### `LessonReviews`
```json
{
  "lessonId": "123",
  "subject": "Database 2",
  "professor": "John",
  "feedback": "Good job!",
  "model": "GPT-4",
  "responseDate": "2024-06-30"
}
```

### `Course`
```json
{
  "id": 1,
  "name": "Software Engineering",
  "account_id": 1001,
  "uuid": "abc-123",
  "course_code": "SE-101"
}
```

### `Assignment`
```json
{
  "id": 10,
  "name": "Database Project",
  "description": "Design a relational database",
  "due_at": "2024-07-10T23:59:59Z"
}
```

## Como Usar

1. Clone o repositório:
   ```sh
   git clone https://github.com/Opaleiros-Foundation/guma-api.git
   ```
2. Instale as dependências e execute o servidor.
3. Use ferramentas como **Postman** ou **cURL** para testar os endpoints.

## Licença

Esta API está licenciada sob a licença [MIT](https://opensource.org/license/mit/).

