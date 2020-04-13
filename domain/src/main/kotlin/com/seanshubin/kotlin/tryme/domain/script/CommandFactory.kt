package com.seanshubin.kotlin.tryme.domain.script

import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType

object CommandFactory {
  val stringIdentity = { s: String -> s }
  val stringToPath = { s: String -> Paths.get(s) }
  val coerceFunctionMap = mapOf(
      String::class.java.name to stringIdentity,
      Path::class.java.name to stringToPath
  )

  fun fromSequence(commandSequence: List<String>): Command {
    val scriptName = commandSequence.getOrNull(0) ?: throw RuntimeException("empty command sequence")
    val namedArgumentStrings = commandSequence.drop(1)
    if (namedArgumentStrings.size % 2 != 0) throw RuntimeException("Expected even number of strings to compose name/value pairs")
    val jvmName = commandNameScriptToJvm(scriptName)
    val kClass = lookupCommandClass(jvmName)
    val commandConstructor = kClass.primaryConstructor
        ?: throw RuntimeException("No primary constructor found for class '$jvmName'")
    val arguments = composeArguments(jvmName, commandConstructor, namedArgumentStrings)
    val command = commandConstructor.call(*arguments.toTypedArray())
    return command
  }

  private fun commandNameScriptToJvm(scriptName: String): String {
    val list = scriptName.split("-")
    return list.joinToString("") { it.capitalize() }
  }

  private fun lookupCommandClass(jvmName: String): KClass<out Command> {
    val kClass = Command::class.sealedSubclasses.find { it.simpleName == jvmName }
    return kClass ?: throw RuntimeException("Class named '$jvmName' not found")
  }

  private fun composeArguments(jvmName: String, kFunction: KFunction<Command>, namedArgumentStrings: List<String>): List<Any> {
    val argumentByName = namedArgumentStrings.chunked(2).map { (scriptName, stringValue) ->
      parameterNameScriptToJvm(scriptName) to stringValue
    }.toMap()
    val parameters = kFunction.parameters
    if (parameters.size != argumentByName.size)
      throw RuntimeException("primary constructor for $jvmName requires ${parameters.size} arguments, got ${argumentByName.size}}")
    val arguments = composeArguments(jvmName, parameters, argumentByName)
    return arguments
  }

  private fun composeArguments(jvmName: String, parameters: List<KParameter>, argumentByName: Map<String, String>): List<Any> {
    fun composeArgument(index: Int, parameter: KParameter): Any {
      val stringValue = argumentByName[parameter.name]
          ?: throw RuntimeException("$jvmName[$index]: parameter ${parameter.name} not found")
      val typeName = parameter.type.javaType.typeName
      val coerceFunction = coerceFunctionMap[typeName]
          ?: throw RuntimeException("$jvmName, parameter ${parameter.name}, index $index: unsupported type $typeName")
      val typedValue = coerceFunction(stringValue)
      return typedValue
    }
    return parameters.mapIndexed(::composeArgument)
  }

  private fun parameterNameScriptToJvm(parameterName: String): String {
    val split = parameterName.split("-")
    val head = split.take(1)
    val tail = split.drop(1).map { it.capitalize() }
    val list = head + tail
    return list.joinToString("")
  }

}