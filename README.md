# Hello Spring — REST API для управления аккаунтами

Учебный REST API на Spring Boot для работы с сущностью `Account` (аккаунт пользователя).

## Технологии

| Технология       | Версия |
|------------------|--------|
| Java             | 21     |
| Spring Boot      | 3.5.11 |
| Spring Data JPA  | —      |
| H2 Database      | —      |
| Lombok           | —      |
| Maven            | —      |

## Структура проекта

```
src/main/java/Start/
├── Application.java              # Точка входа
├── controller/
│   ├── AccountController.java    # REST-контроллер
│   └── dto/
│       ├── AccountRequestDTO.java
│       └── AccountResponseDTO.java
├── entity/
│   └── Account.java              # JPA-сущность
├── exception/
│   └── AccountNotFoundException.java
├── repository/
│   └── AccountRepository.java
└── service/
    └── AccountService.java
```

## Модель данных

**Account**

| Поле      | Тип     | Описание          |
|-----------|---------|-------------------|
| id        | Long    | Идентификатор (PK)|
| name      | String  | Имя пользователя  |
| email     | String  | Email             |
| bill      | Integer | Баланс счёта      |

## API

Базовый URL: `http://localhost:8081`

### Проверка работы сервера

```
GET /hello
```
Ответ: `Hello Spring !`

---

### Создать аккаунт

```
POST /accounts
Content-Type: application/json
```

Тело запроса:
```json
{
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "bill": 5000
}
```

Ответ `200 OK`:
```json
1
```
*(возвращает id созданного аккаунта)*

---

### Получить аккаунт по ID

```
GET /accounts/{id}
```

Ответ `200 OK`:
```json
{
  "accountId": 1,
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "bill": 5000
}
```

Если аккаунт не найден — `500` с сообщением `Account not found with id: {id}`.

---

### Получить все аккаунты

```
GET /accounts
```

Ответ `200 OK`:
```json
[
  {
    "accountId": 1,
    "name": "Иван Иванов",
    "email": "ivan@example.com",
    "bill": 5000
  }
]
```

---

### Удалить аккаунт

```
DELETE /accounts/{id}
```

Ответ `200 OK` — возвращает удалённый аккаунт:
```json
{
  "accountId": 1,
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "bill": 5000
}
```

## Запуск

### Локально (Maven)

```bash
./mvnw spring-boot:run
```

### Docker Compose

```bash
docker compose up --build
```

Данные H2 базы сохраняются в Docker volume `h2-data` между перезапусками.

## H2 Console

Браузерный интерфейс базы данных доступен по адресу:

```
http://localhost:8081/h2-console
```

| Параметр   | Значение              |
|------------|-----------------------|
| JDBC URL   | `jdbc:h2:~/test`      |
| Username   | `sa`                  |
| Password   | *(пусто)*             |

> При запуске через Docker используйте JDBC URL: `jdbc:h2:/data/test`

## Тесты

```bash
./mvnw test
```
