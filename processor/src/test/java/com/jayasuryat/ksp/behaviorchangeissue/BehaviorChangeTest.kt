package com.jayasuryat.ksp.behaviorchangeissue

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.jayasuryat.ksp.processor.TestSymbolProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class BehaviorChangeTest {

    @Rule
    @JvmField
    val temporaryFolder: TemporaryFolder = TemporaryFolder()

    private val processorProvider: SymbolProcessorProvider = TestSymbolProcessorProvider()

    @Test
    fun `should raise error for considerForDowel with inner class`() {

        val source = """
            package com.jayasuryat.ksp.behaviorchangeissue
            
            @Suppress
            class Person(
                val age : Int,    
            )
        """.trimIndent()

        val kotlinSource: SourceFile =
            SourceFile.kotlin(name = "Person.kt", contents = source)
        val result: KotlinCompilation.Result = compile(kotlinSource)

        Assert.assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    private fun compile(
        vararg sourceFiles: SourceFile,
    ): KotlinCompilation.Result {

        fun prepareCompilation(
            vararg sourceFiles: SourceFile,
        ): KotlinCompilation {
            return KotlinCompilation().apply {
                workingDir = temporaryFolder.root
                inheritClassPath = true
                symbolProcessorProviders = listOf(processorProvider)
                sources = sourceFiles.asList()
                verbose = false
                kspWithCompilation = true
            }
        }

        return prepareCompilation(*sourceFiles)
            .compile()
    }
}