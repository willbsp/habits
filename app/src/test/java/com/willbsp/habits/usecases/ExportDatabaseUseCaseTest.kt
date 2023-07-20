package com.willbsp.habits.usecases

import com.willbsp.habits.domain.usecase.ExportDatabaseUseCase
import com.willbsp.habits.fake.FakeDatabaseUtils
import com.willbsp.habits.fake.dao.FakeRawDao
import com.willbsp.habits.rules.TestDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class ExportDatabaseUseCaseTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var exportDatabase: ExportDatabaseUseCase
    private val databaseUtils = FakeDatabaseUtils()

    @Before
    fun setup() {
        val rawDao = FakeRawDao()
        exportDatabase =
            ExportDatabaseUseCase(databaseUtils, rawDao, testDispatcher.getDispatcher())
    }

    @Test
    fun exportDatabase_createsIdenticalFile() = runTest {
        val destination = File.createTempFile("test", ".db")
        exportDatabase(destination.outputStream())
        assertEquals(FakeDatabaseUtils.TEST_FILE_TEXT, destination.readLines()[0])
    }

}