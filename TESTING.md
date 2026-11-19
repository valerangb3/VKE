# Практическое задание 10: Unit-тесты

Репозиторий: [valerangb3/VKE, ветка hw8](https://github.com/valerangb3/VKE/tree/hw8).

## Покрытие требований

Добавлено **60 новых JVM-тестов**, без новых зависимостей:

| Проверяемый компонент | Класс тестов | Новых тестов |
| --- | --- | ---: |
| Repository: каталог, карточка, похожие приложения | `AppRepositoryImplTest` | 15 |
| Мапперы App: DTO → Domain и Domain → DTO | `AppMapperTest` | 10 (5 + 5) |
| Мапперы AppDetails: DTO → Domain и Domain → DTO | `AppDetailsMapperTest` | 10 (5 + 5) |
| UseCase: каталог | `GetAppsUseCaseTest` | 5 |
| UseCase: карточка | `GetAppDetailsUseCaseTest` | 5 |
| UseCase: похожие приложения | `GetSimilarAppsUseCaseTest` | 5 |
| UseCase: последовательная загрузка рейтингов | `LoadRatingsSequentiallyUseCaseTest` | 5 |
| UseCase: параллельная загрузка рейтингов | `LoadRatingsInParallelUseCaseTest` | 5 |

Минимум 5 тестов выполнен для каждого пункта задания; каждый маппер дополнительно
проверен минимум пятью тестами в каждом направлении, каждый UseCase — пятью тестами.

## Что проверяется

**Repository.** Реальный `AppRepositoryImpl` работает с `FakeCatalogApi`. Проверяются
преобразование всех элементов, порядок каталога, пустые ответы, повторная загрузка
свежих данных, передача id в рабочий маршрут `catalog/{id}`, исключение выбранного
приложения из похожих, ошибки API и `CancellationException`. Тестовая реализация
запрещает вызов старого маршрута карточки и непредусмотренных методов.
Существующий `RatingsRepositoryTest` отдельно проверяет последовательность,
конкурентность, отмену дочерних запросов и пустой список рейтингов.

**Мапперы.** Проверяются все поля, переименования, `null`, пустые строки и списки,
Unicode, пробелы, порядок и повторы скриншотов, числовые значения, обратное
преобразование и цикл Domain → DTO → Domain. Ожидаемые модели задаются явно,
без вызова проверяемого маппера при подготовке результата.

`App` не содержит категорию списка: `App.toDto()` оставляет `category = null`.
Категория карточки хранится в `AppDetails` и сохраняется в обе стороны.
Отсутствующие опциональные значения DTO нормализуются в пустые строки/списки;
обратный маппинг сохраняет эти пустые значения, а не восстанавливает исходный `null`.
Эти правила закреплены в тестах.

**UseCase.** Добавлены пять классов с `suspend operator fun invoke` и внедрением
`AppRepository` через Hilt. Оба ViewModel теперь используют их. Тесты с
`FakeAppRepository` проверяют результаты, точные аргументы и число обращений,
отсутствие кеширования, правильный способ загрузки рейтингов, проброс ошибок
и отмены. Отдельно проверено ожидание приостановленного запроса карточки.
Существующие тесты ViewModel сохранены и адаптированы к новым конструкторам.

Новые тесты Repository и UseCase используют `runTest` и тестовые зависимости:
им не нужны интернет, Android-устройство или реальное ожидание. Существующий
`CatalogApiTest` использует локальный MockWebServer. Существующий
`RatingsBenchmarkTest` намеренно оставлен с реальными задержками (~20 секунд).

## Запуск и самопроверка

Нужны JDK 21, Android SDK с платформой, указанной в `app/build.gradle.kts`,
и настроенный `sdk.dir` в локальном `local.properties` либо `ANDROID_HOME`.
Gradle Wrapper и версии зависимостей уже зафиксированы в проекте.
Первый запуск может потребовать интернет для загрузки зависимостей.

Все JVM-тесты:

```sh
./gradlew :app:testDebugUnitTest
```

Только новые тесты задания:

```sh
./gradlew :app:testDebugUnitTest \
  --tests 'com.vgb3.vke.data.repository.AppRepositoryImplTest' \
  --tests 'com.vgb3.vke.data.mapper.*' \
  --tests 'com.vgb3.vke.domain.usecase.*'
```

Сборка приложения, проверка компиляции существующих UI-тестов и Lint:

```sh
./gradlew :app:assembleDebug :app:compileDebugAndroidTestKotlin :app:lintDebug
```

HTML-отчёт JVM-тестов: `app/build/reports/tests/testDebugUnitTest/index.html`.
JUnit XML: `app/build/test-results/testDebugUnitTest/TEST-*.xml`.
Отчёт Lint: `app/build/reports/lint-results-debug.html`.

Чек-лист:

- [x] Repository покрыт минимум пятью Unit-тестами.
- [x] Преобразования DTO ↔ Domain покрыты в обоих направлениях.
- [x] Каждый UseCase покрыт пятью Unit-тестами и используется приложением.
- [x] У новых тестов нет зависимости от внешнего сервера и случайных рейтингов.
- [x] Проверяются ошибки, отмена и пустые данные, а не только успешный ответ.

## Фактический результат

Проверено 19 ноября 2026 года на macOS arm64, Gradle 9.4.1, JVM 21:

```sh
./gradlew :app:testDebugUnitTest :app:assembleDebug :app:compileDebugAndroidTestKotlin :app:lintDebug --console=plain
```

- **98 JVM-тестов: 0 failures, 0 errors, 0 skipped** (38 существующих + 60 новых).
- `assembleDebug`: успешно, APK — `app/build/outputs/apk/debug/app-debug.apk`.
- `compileDebugAndroidTestKotlin`: успешно; UI-тесты на устройстве в рамках этого задания не запускались.
- `lintDebug`: успешно, 0 ошибок; 29 предупреждений о версиях зависимостей,
  target SDK, ресурсах и метке приложения.
- `git diff --check`: успешно.
