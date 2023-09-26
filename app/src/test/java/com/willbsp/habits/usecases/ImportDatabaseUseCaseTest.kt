package com.willbsp.habits.usecases

import com.willbsp.habits.domain.usecase.ImportDatabaseUseCase
import com.willbsp.habits.fake.util.FakeDatabaseUtils
import com.willbsp.habits.rules.TestDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class ImportDatabaseUseCaseTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private lateinit var importDatabase: ImportDatabaseUseCase
    private val databaseUtils = FakeDatabaseUtils()

    @Before
    fun setup() {
        importDatabase = ImportDatabaseUseCase(databaseUtils, testDispatcherRule.getDispatcher())
    }

    @Test
    fun importDatabase_createsIdenticalFile() = runTest {
        val testText = "completely different text"
        val source = File.createTempFile("test", ".db")
        source.writeText(testText)
        importDatabase(source.inputStream())
        val destination = databaseUtils.getDatabasePath()
        assertEquals(testText, destination.readText())
    }

    @Test
    fun importDatabase_databaseIsInvalid_databaseRestored() = runTest {
        val initialText = databaseUtils.getDatabasePath().readText()
        databaseUtils.setDatabaseValidity(false)
        val testText = "completely different text"
        val source = File.createTempFile("test", ".db")
        source.writeText(testText)
        importDatabase(source.inputStream())
        val destination = databaseUtils.getDatabasePath()
        assertEquals(initialText, destination.readText())
    }

}