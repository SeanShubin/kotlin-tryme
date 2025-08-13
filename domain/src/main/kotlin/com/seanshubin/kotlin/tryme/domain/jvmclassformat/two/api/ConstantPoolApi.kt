package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ConstantPoolTag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.ReferenceKind

interface ConstantPoolApi {
    val tag: ConstantPoolTag
    val index: Int
    fun line():String
    fun makeLine(s:String):String {
        return "${tag.line()}[$index] $s"
    }
    interface ClassApi : ConstantPoolApi {
        val name: String
    }

    interface RefApi : ConstantPoolApi {
        val className: String
        val methodName: String
        val methodDescriptor: String
    }

    interface StringApi : ConstantPoolApi {
        val value: String
    }

    interface IntegerApi : ConstantPoolApi {
        val value: Int
    }

    interface FloatApi : ConstantPoolApi {
        val value: Float
    }

    interface LongApi : ConstantPoolApi {
        val value: Long
    }

    interface DoubleApi : ConstantPoolApi {
        val value: Double
    }

    interface NameAndTypeApi : ConstantPoolApi {
        val name: String
        val descriptor: String
    }

    interface Utf8Api : ConstantPoolApi {
        val value: String
    }

    interface MethodHandleApi : ConstantPoolApi {
        val referenceKind: ReferenceKind
        val className: String
        val methodName: String
        val methodDescriptor: String
    }

    interface MethodTypeApi : ConstantPoolApi {
        val descriptor: String
    }

    interface DynamicApi : ConstantPoolApi {
        val bootstrapMethodAttrIndex: UShort
        val name: String
        val descriptor: String
    }

    interface InvokeDynamicApi : ConstantPoolApi {
        val bootstrapMethodAttrIndex: UShort
        val name: String
        val descriptor: String
    }

    interface ModuleApi : ConstantPoolApi {
        val name: String
    }

    interface PackageApi : ConstantPoolApi {
        val name: String
    }
}
