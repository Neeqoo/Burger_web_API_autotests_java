Тестирование ручек API для Stellar [Burgers](https://stellarburgers.nomoreparties.site/).


# Задача:

**Создание пользователя:**
- создать уникального пользователя
- создать пользователя, который уже зарегистрирован
- создать пользователя и не заполнить одно из обязательных полей

**Логин пользователя:**
- логин под существующим пользователем
- логин с неверным логином и паролем

**Изменение данных пользователя:**
- с авторизацией
- без авторизации

*Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.*

**Создание заказа:**
- с авторизацией,
- без авторизации,
- с ингредиентами,
- без ингредиентов,
- с неверным хешем ингредиентов.

**Получение заказов конкретного пользователя:**
- авторизованный пользователь,
- неавторизованный пользователь.




# В проекте применены следующие технологии:

- Java 11
- Maven (4.0)
- Aspectj (1.9.7)
- Allure (2.15.0)
- JUnit 4 (4.13.2)
- RestAssured (5.5.0)
- Gson (2.8.9)
