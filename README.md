# Тестирование сетевого и локального слоёв

Учебный проект для проверки CRUD-операций со списком задач. Локальный слой использует SQLite, сетевой клиент построен на Retrofit и OkHttp. Тесты сетевого слоя используют MockWebServer, а тесты сервиса — MockK.

## Требования

- JDK 21
- Gradle Wrapper

## Ветки

- `qa-kotlin-6-1` — тесты сервиса с mock DAO и CRUD-проверки SQLite.
- `qa-kotlin-6-2` — тесты API через MockWebServer для GET, POST и DELETE.

## Запуск

```shell
./gradlew test
```

Создать Allure-отчёт в `reports`:

```shell
./gradlew exportAllureReport
```

Статический отчёт создаётся из Allure-результатов тестов. Каталог `reports` включается в репозиторий.
