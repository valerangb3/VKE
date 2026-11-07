package com.vgb3.vke.data.network

import com.vgb3.vke.data.mapper.toDomain
import com.vgb3.vke.data.repository.AppRepositoryImpl
import com.vgb3.vke.di.NetworkModule
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class CatalogApiTest {

    private lateinit var server: MockWebServer
    private lateinit var api: CatalogApi

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        api = NetworkModule.provideRetrofit(
            NetworkModule.provideOkHttpClient(),
            NetworkModule.provideJson(),
        ).newBuilder()
            .baseUrl(server.url("/"))
            .build()
            .create(CatalogApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun catalogIsLoadedThroughRepositoryAndKeepsServerIds() = runBlocking {
        server.enqueue(jsonResponse(fixture("catalog.json")))

        val apps = AppRepositoryImpl(api).getApps()

        assertEquals(listOf(APP_ID, "82ea6f63-2b2a-43e1-b54c-ec62e6e27229"), apps.map { it.id })
        assertEquals("QuickNote", apps.first().title)
        assertEquals("Лёгкое приложение для заметок и напоминаний", apps.first().description)
        assertTrue(apps.first().iconUrl.startsWith("https://fastly.picsum.photos/"))
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/catalog", request.path)
    }

    @Test
    fun catalogAcceptsMissingOptionalFieldsNullsAndNewServerFields() = runBlocking {
        server.enqueue(jsonResponse("""
            [
              {"id":"first", "name":"Первое", "newField":true},
              {"id":"second", "name":"Второе", "description":null, "iconUrl":null}
            ]
        """.trimIndent()))

        val apps = AppRepositoryImpl(api).getApps()

        assertEquals(listOf("", ""), apps.map { it.description })
        assertEquals(listOf("", ""), apps.map { it.iconUrl })
    }

    @Test
    fun emptyCatalogIsSuccessful() = runBlocking {
        server.enqueue(jsonResponse("[]"))

        assertTrue(AppRepositoryImpl(api).getApps().isEmpty())
    }

    @Test
    fun assignmentDetailsRouteUsesUuidAndMapsSizeInMegabytes() = runBlocking {
        server.enqueue(jsonResponse(fixture("app-details.json")))

        val details = api.getAppDetails(APP_ID).toDomain()

        assertEquals(APP_ID, details.id)
        assertEquals("QuickNote", details.name)
        assertEquals("QuickSoft Inc.", details.developer)
        assertEquals("Производительность", details.category)
        assertEquals(3, details.age)
        assertEquals(8.2, details.appSize, 0.0)
        assertEquals(5, details.screenshotItems.size)
        assertTrue(details.description.startsWith("QuickNote —"))
        val request = server.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/$APP_ID", request.path)
    }

    @Test
    fun actualServerDetailsRouteIsAvailableSeparately() = runBlocking {
        server.enqueue(jsonResponse(fixture("app-details.json")))

        assertEquals(APP_ID, api.getCatalogAppDetails(APP_ID).id)
        assertEquals("/catalog/$APP_ID", server.takeRequest().path)
    }

    @Test
    fun httpFailureIsPropagatedInsteadOfReturningMockCatalog() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(503))

        val exception = try {
            AppRepositoryImpl(api).getApps()
            error("Expected HTTP failure")
        } catch (exception: HttpException) {
            exception
        }

        assertEquals(503, exception.code())
    }

    @Test
    fun malformedCatalogIsPropagatedAsSerializationFailure() = runBlocking {
        server.enqueue(jsonResponse("""[{"id":"broken"}]"""))

        try {
            AppRepositoryImpl(api).getApps()
            error("Expected serialization failure")
        } catch (_: SerializationException) {
            // name обязателен: некорректный ответ не превращается в пустой каталог.
        }
    }

    @Test
    fun repositoryLoadsSelectedAppDetailsFromWorkingServerRoute() = runBlocking {
        server.enqueue(jsonResponse(fixture("app-details.json")))

        val details = AppRepositoryImpl(api).getAppDetails(APP_ID)

        assertEquals(APP_ID, details.id)
        assertEquals("QuickNote", details.name)
        assertEquals("QuickSoft Inc.", details.developer)
        assertEquals(8.2, details.appSize, 0.0)
        assertEquals(5, details.screenshotItems.size)
        assertEquals("/catalog/$APP_ID", server.takeRequest().path)
    }

    @Test
    fun missingAppIsAnErrorInsteadOfMockDetails() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404))

        val exception = try {
            AppRepositoryImpl(api).getAppDetails(APP_ID)
            error("Expected HTTP failure")
        } catch (exception: HttpException) {
            exception
        }

        assertEquals(404, exception.code())
        assertEquals(1, server.requestCount)
    }

    @Test
    fun otherAppsComeFromCatalogAndExcludeSelectedApp() = runBlocking {
        server.enqueue(jsonResponse(fixture("catalog.json")))

        val apps = AppRepositoryImpl(api).getSimilarApps(APP_ID)

        assertEquals(1, apps.size)
        assertEquals("82ea6f63-2b2a-43e1-b54c-ec62e6e27229", apps.single().id)
        assertEquals("Calmalist", apps.single().name)
        assertTrue(apps.single().iconUrl.startsWith("https://fastly.picsum.photos/"))
        assertEquals("/catalog", server.takeRequest().path)
    }

    private fun fixture(name: String): String = checkNotNull(javaClass.getResource("/$name")).readText()

    private fun jsonResponse(body: String): MockResponse = MockResponse()
        .setHeader("Content-Type", "application/json")
        .setBody(body)

    private companion object {
        const val APP_ID = "fa2e31b8-1234-4cf7-9914-108a170a1b01"
    }
}
