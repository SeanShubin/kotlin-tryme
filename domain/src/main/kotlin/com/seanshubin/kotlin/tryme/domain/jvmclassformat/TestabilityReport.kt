package com.seanshubin.kotlin.tryme.domain.jvmclassformat

import java.nio.file.Path

data class TestabilityReport(
    val testability:String,
    val path: Path,
    val className:String,
    val isOnBlackList: List<MethodCalls>,
    val whiteListed: List<MethodCalls>,
    val unknown: List<MethodCalls>
) {
    data class MethodKey(
        val className: String,
        val methodName: String,
        val methodDescriptor: String){
        fun patternMatchId():String {
            return "$className:$methodName:$methodDescriptor"
        }
    }
    data class MethodCalls(
        val caller: MethodKey,
        val called: List<MethodKey>
    )
    companion object {
        fun fromJvmClass(
            path:Path,
            jvmClass: JvmClass,
            isOnBlacklist: (String)->Boolean,
            isOnWhitelist: (String)->Boolean
        ): TestabilityReport {
            val blackListed = mutableListOf<MethodCalls>()
            val whiteListed = mutableListOf<MethodCalls>()
            val unknown = mutableListOf<MethodCalls>()
            val callingClassName = jvmClass.thisClass.name.raw.value
            jvmClass.methods.forEach { method ->
                val callingMethodName = method.name.raw.value
                val callingMethodDescriptor = method.descriptor.raw.value
                val callingMethodKey = MethodKey(callingClassName, callingMethodName, callingMethodDescriptor)
                val currentMethodBlackListed = mutableListOf<MethodKey>()
                val currentMethodWhiteListed = mutableListOf<MethodKey>()
                val currentMethodUnknown = mutableListOf<MethodKey>()
                val codeBlock = method.codeAttribute?.codeBlock
                codeBlock?.opCodes?.forEach { opCode ->
                    when (opCode.code) {
                        Code.invokestatic, Code.getstatic -> {
                            opCode as OpCodeEntry.ConstantPoolIndex
                            val constantPoolEntry = opCode.constantPoolEntry
                            constantPoolEntry as ConstantPoolEntry.ConstantPoolEntryFieldMethodInterfaceMethodRef
                            val calledClassName = constantPoolEntry.classEntry.name.raw.value
                            val calledMethodName = constantPoolEntry.nameAndTypeEntry.nameEntry.raw.value
                            val calledMethodDescriptor = constantPoolEntry.nameAndTypeEntry.descriptorEntry.raw.value
                            val calledMethodKey = MethodKey(calledClassName, calledMethodName, calledMethodDescriptor)
                            when {
                                isOnBlacklist(calledMethodKey.patternMatchId()) -> {
                                    currentMethodBlackListed.add(calledMethodKey)
                                }

                                isOnWhitelist(calledMethodKey.patternMatchId()) -> {
                                    currentMethodWhiteListed.add(calledMethodKey)
                                }

                                else -> {
                                    currentMethodUnknown.add(calledMethodKey)
                                }
                            }
                        }

                        else -> {
                            // do nothing
                        }
                    }
                }
                if(currentMethodBlackListed.isNotEmpty() ) {
                    blackListed.add(MethodCalls(callingMethodKey, currentMethodBlackListed))
                }
                if(currentMethodWhiteListed.isNotEmpty()) {
                    whiteListed.add(MethodCalls(callingMethodKey , currentMethodWhiteListed))
                }
                if(currentMethodUnknown.isNotEmpty()) {
                    unknown.add(MethodCalls(callingMethodKey , currentMethodUnknown))
                }
            }
            val testability = if(blackListed.isNotEmpty()) {
                "not testable"
            } else if(unknown.isNotEmpty()) {
                "unknown testability"
            } else {
                "testable"
            }
            return TestabilityReport(testability, path, callingClassName, blackListed, whiteListed, unknown)
        }
    }
}