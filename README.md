# VKE

Учебное Android-приложение VK Education.

## Практическое задание 8: сеть и Retrofit

Каталог загружается с `http://185.103.109.134/catalog` через Retrofit 3 и конвертер kotlinx.serialization. Hilt создаёт `OkHttpClient`, `Retrofit` и `CatalogApi`; `AppRepositoryImpl` преобразует DTO в доменные модели. Моковый каталог и искусственная задержка удалены.

- JSON каталога содержит `id`, `name`, `description`, `category`, `iconUrl`. Серверные UUID сохраняются как `String` в моделях, аргументах навигации и ViewModel.
- Список показывает загрузку, полученные приложения, пустой каталог или ошибку с кнопкой «Повторить». Ошибки HTTP, соединения и JSON не приводят к падению; отмена корутины сохраняется.
- Для HTTP из условия в `network_security_config.xml` разрешён открытый трафик только к `185.103.109.134`.
- `CatalogApi.getAppDetails(id)` реализует `GET /{id}` из условия. При проверке 7 ноября 2026 года этот адрес возвращал **404**, в том числе для UUID из каталога. Рабочий адрес сервера — `GET /catalog/{id}`; для него добавлен `getCatalogAppDetails(id)`, который используется карточкой.
- Оба запроса подробностей возвращают `AppDetailsDto`; маппер учитывает реальные поля `developer`, `ageRating`, `size`, `screenshotUrlList`. `size` уже задан в МБ и не делится повторно на число байтов.
- По дополнительному запросу карточка подключена к серверу: загружаются название, разработчик, категория, возраст, размер, иконка, скриншоты и полное описание выбранного приложения. Моки удалены.
- Карточка показывает загрузку и ошибку с повтором; раскрытое описание можно свернуть. Другие приложения берутся из каталога без выбранного ID. Их ошибка или медленная загрузка не мешает просмотру карточки.

### Самопроверка сети

```sh
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest
```

`CatalogApiTest` использует MockWebServer и JSON по реальному контракту: проверяет GET-маршруты, UUID, десериализацию, маппинг, пустой ответ, nullable-поля, неизвестные поля, ошибки HTTP и некорректный JSON. `AppListViewModelTest` и `DetailsViewModelTest` проверяют загрузку, повтор после ошибки, защиту от параллельных запросов и отмену корутины. Для карточки дополнительно проверяется независимая загрузка других приложений. Эти тесты не требуют доступа к учебному серверу.

Ручная проверка:

1. Открыть приложение с интернетом: появляются `QuickNote`, `Calmalist` и остальные записи сервера с описаниями и иконками.
2. Запустить приложение без сети: появляется сообщение об ошибке и кнопка «Повторить».
3. Включить сеть и нажать «Повторить»: каталог загружается.
4. Открыть QuickNote: в карточке должны появиться QuickSoft Inc., размер 8.2 МБ, скриншоты и описание сервера. Раскрыть и свернуть описание.
5. Пересоздать Activity или повернуть устройство, вернуться к списку и открыть Calmalist: карточка показывает данные другого приложения по его UUID.
6. Открыть карточку без сети, затем восстановить сеть и нажать «Повторить»: появляются данные сервера.

`HiltNavigationTest` проверяет последний сценарий через production-граф и живой каталог, поэтому требует интернета и доступного учебного сервера. Тест проверяет серверные данные двух разных карточек. `DetailsScreenTest` отдельно проверяет повтор загрузки и раскрытие описания без сети.

Проверено 7 ноября 2026 года: debug APK и тестовый APK собраны, все 22 unit-теста и 4 инструментальных теста на эмуляторе Pixel 10 Pro прошли, включая загрузку двух разных карточек, восстановление Activity, повтор загрузки и раскрытие описания. `lintDebug`: 0 ошибок; предупреждения касаются версий зависимостей, target SDK и существующих ресурсов.

## Практическое задание 7: внедрение зависимостей

В приложении используется Hilt с генерацией кода через KSP.

- `VkeApplication` с `@HiltAndroidApp` создаёт контейнер приложения и зарегистрирован в манифесте.
- `MainActivity` помечена `@AndroidEntryPoint`.
- `RepositoryModule.bindAppRepository()` связывает интерфейс `AppRepository` с `AppRepositoryImpl` через `@Binds`. `@Singleton` сохраняет один репозиторий для приложения.
- `AppListViewModel` получает `AppRepository` через `@Inject constructor` и `@HiltViewModel`.
- `DetailsViewModel` получает репозиторий из Hilt, а `appId` — через assisted injection из ключа Navigation 3.
- `AppNav` получает обе ViewModel через `hiltViewModel()`. `rememberViewModelStoreNavEntryDecorator()` сохраняет их привязку к соответствующей записи навигации.
- Ручной `ServiceLocator` удалён.

Схема зависимостей: `hiltViewModel()` → ViewModel → `AppRepository` → `AppRepositoryImpl`.

### Самопроверка

Для Gradle закреплена Amazon Corretto JDK 21 в `gradle/gradle-daemon-jvm.properties`. Gradle использует подходящую локальную установку или автоматически скачивает её по ссылкам из этого файла. Также требуется Android SDK 37 (путь в `local.properties` или `ANDROID_HOME`).

В этой конфигурации Android SDK сборка с GraalVM 21.0.7 падает на `jlink` при создании `androidJdkImage`; сообщение об ошибке configuration cache является следствием этого сбоя. Выбор Corretto задаётся критериями JVM демона, которые [имеют приоритет над `JAVA_HOME` и `org.gradle.java.home`](https://docs.gradle.org/current/userguide/gradle_daemon.html#sec:daemon_jvm_criteria). Configuration cache остаётся включённым.

Для повторной генерации настройки JVM:

```sh
./gradlew updateDaemonJvm --jvm-version=21 --jvm-vendor=amazon
```

```sh
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleDebugAndroidTest
```

На запущенном эмуляторе или подключённом устройстве:

```sh
./gradlew :app:connectedDebugAndroidTest
```

`HiltNavigationTest` проверяет загрузку списка и деталей через настоящую DI-конфигурацию, восстановление деталей после пересоздания Activity и возврат к списку. `@HiltAndroidTest` не нужен: тест запускает обычное приложение с его production-графом.

Для ручной проверки откройте приложение, дождитесь списка, выберите карточку, поверните устройство на экране деталей и вернитесь назад. Оба экрана должны показать данные без ошибок создания ViewModel.

Подход к передаче аргументов соответствует [примеру Navigation 3 с Hilt](https://developer.android.com/guide/navigation/navigation-3/recipes/passingarguments); настройка плагина и KSP — [документации Hilt](https://dagger.dev/hilt/gradle-setup).
