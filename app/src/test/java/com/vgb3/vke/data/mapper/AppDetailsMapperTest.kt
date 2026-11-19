package com.vgb3.vke.data.mapper

import com.vgb3.vke.testutil.TestData.details
import com.vgb3.vke.testutil.TestData.detailsDto
import org.junit.Assert.assertEquals
import org.junit.Test

class AppDetailsMapperTest {
    @Test
    fun dtoToDomainMapsEveryFieldIncludingRenamedProperties() {
        assertEquals(details, detailsDto.toDomain())
    }

    @Test
    fun dtoToDomainNormalizesAllMissingOptionalFields() {
        assertEquals(
            details.copy(iconApp = "", screenshotItems = emptyList(), description = ""),
            detailsDto.copy(iconUrl = null, screenshotUrlList = null, description = null).toDomain(),
        )
    }

    @Test
    fun dtoToDomainPreservesExplicitEmptyValues() {
        assertEquals(
            details.copy(iconApp = "", screenshotItems = emptyList(), description = ""),
            detailsDto.copy(iconUrl = "", screenshotUrlList = emptyList(), description = "").toDomain(),
        )
    }

    @Test
    fun dtoToDomainPreservesScreenshotOrderAndDuplicates() {
        val screenshots = listOf("second.png", "first.png", "second.png")

        assertEquals(details.copy(screenshotItems = screenshots), detailsDto.copy(screenshotUrlList = screenshots).toDomain())
    }

    @Test
    fun dtoToDomainPreservesZeroAgeSizeAndUnicodeText() {
        assertEquals(
            details.copy(age = 0, appSize = 0.0, name = "Заметки 📝", description = " Текст\n "),
            detailsDto.copy(ageRating = 0, size = 0.0, name = "Заметки 📝", description = " Текст\n ").toDomain(),
        )
    }

    @Test
    fun domainToDtoMapsEveryFieldIncludingRenamedProperties() {
        assertEquals(detailsDto, details.toDto())
    }

    @Test
    fun domainToDtoPreservesEmptyValuesWithoutConvertingToNull() {
        assertEquals(
            detailsDto.copy(iconUrl = "", screenshotUrlList = emptyList(), description = ""),
            details.copy(iconApp = "", screenshotItems = emptyList(), description = "").toDto(),
        )
    }

    @Test
    fun domainToDtoPreservesScreenshotOrderAndDuplicates() {
        val screenshots = listOf("second.png", "first.png", "second.png")

        assertEquals(detailsDto.copy(screenshotUrlList = screenshots), details.copy(screenshotItems = screenshots).toDto())
    }

    @Test
    fun domainToDtoPreservesAgeFractionalSizeAndUnicodeText() {
        assertEquals(
            detailsDto.copy(ageRating = 18, size = 1234.567, developer = "Разработчик 📝", category = " Игры "),
            details.copy(age = 18, appSize = 1234.567, developer = "Разработчик 📝", category = " Игры ").toDto(),
        )
    }

    @Test
    fun domainToDtoAndBackPreservesCompleteDomainModel() {
        assertEquals(details, details.toDto().toDomain())
        val empty = details.copy(iconApp = "", screenshotItems = emptyList(), description = "")
        assertEquals(empty, empty.toDto().toDomain())
    }
}
