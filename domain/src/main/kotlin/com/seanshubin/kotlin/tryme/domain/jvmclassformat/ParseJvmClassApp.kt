package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import com.seanshubin.kotlin.tryme.domain.json.JsonMappers
import java.io.DataInputStream
import java.nio.file.Files
import java.nio.file.Paths

object ParseJvmClassApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputDir = Paths.get("domain", "target")
        val relativeFilePath = Paths.get("classes","com","seanshubin","kotlin","tryme","domain","jvmclassformat","SampleApp.class")
        val filePath = inputDir.resolve(relativeFilePath)
        val outputDir = Paths.get("domain", "target")
        val dataPath = outputDir.resolve("SampleApp-data.txt")
        val rawPath = outputDir.resolve("SampleApp-raw.json")
        val interpretedPath = outputDir.resolve("SampleApp-interpreted.json")
        val dataInputLines = mutableListOf<String>()
        val emit: (String) -> Unit = { dataInputLines.add(it) }
        val rawJvmClass = Files.newInputStream(filePath).use { inputStream ->
            val dataInput = DataInputStream(inputStream)
            val debugDataInput = DebugDataInput(dataInput, emit)
            RawJvmClass.fromDataInput(debugDataInput)
        }
        Files.write(dataPath, dataInputLines)
        val json = JsonMappers.pretty.writeValueAsString(rawJvmClass)
        Files.writeString(rawPath, json)
        val jvmClass = JvmClass.fromRawJvmClass(rawJvmClass)
        val jvmClassObject = jvmClass.toObject()
        val jvmClassJson = JsonMappers.pretty.writeValueAsString(jvmClassObject)
        Files.writeString(interpretedPath, jvmClassJson)
    }
}