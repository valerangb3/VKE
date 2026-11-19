package com.vgb3.vke.data.mapper

import com.vgb3.vke.data.dto.AppDto
import com.vgb3.vke.domain.model.App
import com.vgb3.vke.testutil.TestData.app
import com.vgb3.vke.testutil.TestData.appDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppMapperTest {
    @Test
    fun dtoToDomainMapsAllAvailableFields() {
        assertEquals(app, appDto.toDomain())
    }

    @Test
    fun dtoToDomainReplacesMissingDescriptionAndIconWithEmptyStrings() {
        assertEquals(
            app.copy(description = "", iconUrl = ""),
            appDto.copy(description = null, iconUrl = null).toDomain(),
        )
    }

    @Test
    fun dtoToDomainPreservesEmptyRequiredAndOptionalStrings() {
        assertEquals(App("", "", "", ""), AppDto("", "", "", "", "").toDomain())
    }

    @Test
    fun dtoToDomainPreservesUnicodeAndWhitespace() {
        assertEquals(
            app.copy(title = " Заметки 📝 ", description = "Первая строка\nВторая строка"),
            appDto.copy(name = " Заметки 📝 ", description = "Первая строка\nВторая строка").toDomain(),
        )
    }

    @Test
    fun dtoToDomainDoesNotDependOnDtoOnlyCategory() {
        assertEquals(app, appDto.copy(category = null).toDomain())
        assertEquals(app, appDto.copy(category = "Другая категория").toDomain())
    }

    @Test
    fun domainToDtoMapsAllAvailableFieldsWithoutInventingCategory() {
        assertEquals(appDto.copy(category = null), app.toDto())
        assertNull(app.toDto().category)
    }

    @Test
    fun domainToDtoKeepsEmptyValuesAsEmptyStrings() {
        assertEquals(
            AppDto(id = "", name = "", description = "", iconUrl = ""),
            App("", "", "", "").toDto(),
        )
    }

    @Test
    fun domainToDtoPreservesUnicodeAndWhitespace() {
        assertEquals(
            appDto.copy(category = null, name = " Заметки 📝 ", description = "Строка\nСтрока"),
            app.copy(title = " Заметки 📝 ", description = "Строка\nСтрока").toDto(),
        )
    }

    @Test
    fun domainToDtoAndBackPreservesDomainModel() {
        assertEquals(app, app.toDto().toDomain())
    }

    @Test
    fun domainToDtoAfterNullableDtoMappingProducesNormalizedDto() {
        val source = AppDto(id = "optional", name = "Без описания")

        assertEquals(source.copy(description = "", iconUrl = ""), source.toDomain().toDto())
    }
}
