package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import java.io.DataInputStream
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class JvmClassTest {
    @Test
    fun testFromDataInput() {
        val filePathName = """target/classes/com/seanshubin/kotlin/tryme/domain/jvmclassformat/SampleApp.class"""
        val outputPath = Paths.get("target", "SampleApp.json")
        val filePath = Paths.get(filePathName)
        val jvmClass = Files.newInputStream(filePath).use { inputStream ->
            val dataInput = DataInputStream(inputStream)
            JvmClass.fromDataInput(dataInput)
        }
        val theObject = jvmClass.toObject()
        val json = JsonMappers.pretty.writeValueAsString(theObject)
        Files.writeString(outputPath, json)

//        jvmClass.methodInvocations().forEach(::println)
        jvmClass.allAttributes().forEach { it.process(jvmClass) }
    }
}
/*
javap -c -v target/classes/com/seanshubin/kotlin/tryme/domain/jvmclassformat/SampleApp.class
> target/SampleApp.txt

 */