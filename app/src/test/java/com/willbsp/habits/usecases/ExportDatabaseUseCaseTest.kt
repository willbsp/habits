package com.willbsp.habits.usecases

import com.willbsp.habits.domain.usecase.ExportDatabaseUseCase
import com.willbsp.habits.fake.dao.FakeRawDao
import com.willbsp.habits.fake.util.FakeDatabaseUtils
import com.willbsp.habits.rules.TestDispatcherRule
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.io.IOException

class ExportDatabaseUseCaseTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private lateinit var exportDatabase: ExportDatabaseUseCase
    private val databaseUtils = FakeDatabaseUtils()

    @Before
    fun setup() {
        val rawDao = FakeRawDao()
        exportDatabase =
            ExportDatabaseUseCase(databaseUtils, rawDao, testDispatcherRule.getDispatcher())
    }

    @Test
    fun exportDatabase_createsIdenticalFile() = runTest {
        val destination = File.createTempFile("test", ".db")
        exportDatabase(destination.outputStream())
        assertEquals(FakeDatabaseUtils.TEST_FILE_TEXT, destination.readLines()[0])
    }

    @Test
    fun exportDatabase_outputClosed_throwsIOException() = runTest {
        val destination = File.createTempFile("test", ".db")
        val output = destination.outputStream()
        output.close()
        assertThrows(IOException::class.java) {
            runBlocking { exportDatabase(output) }
        }
    }

}