package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api

interface ConstantPoolApi {
    val tag: ConstantPoolTag
    fun entriesConsumed(): UShort
    interface ClassApi : ConstantPoolApi {
        val name:String
    }
    interface FieldrefApi : ConstantPoolApi {

    }
    interface MethodrefApi : ConstantPoolApi {}
    interface InterfaceMethodrefApi : ConstantPoolApi {}
    interface StringApi : ConstantPoolApi {}
    interface IntegerApi : ConstantPoolApi {}
    interface FloatApi : ConstantPoolApi {}
    interface LongApi : ConstantPoolApi {}
    interface DoubleApi : ConstantPoolApi {}
    interface NameAndTypeApi : ConstantPoolApi {}
    interface Utf8Api : ConstantPoolApi {}
    interface MethodHandleApi : ConstantPoolApi {}
    interface MethodTypeApi : ConstantPoolApi {}
    interface DynamicApi : ConstantPoolApi {}
    interface InvokeDynamicApi : ConstantPoolApi {}
    interface ModuleApi : ConstantPoolApi {}
    interface PackageApi : ConstantPoolApi {}
}