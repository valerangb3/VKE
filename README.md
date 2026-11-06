# VKE

Учебное Android-приложение VK Education.

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
