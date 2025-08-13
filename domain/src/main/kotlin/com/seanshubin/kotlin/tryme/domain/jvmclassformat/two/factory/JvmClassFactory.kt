package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.*
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.*
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolClassApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolDoubleApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolDynamicApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolFloatApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolIntegerApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolLongApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolMethodHandleApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolMethodTypeApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolModuleApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolNameAndTypeApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolPackageApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolRefApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolStringApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.constant.factory.ConstantPoolUtf8ApiFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.field.factory.FieldFactory
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl.JvmClass
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.method.factory.MethodFactory

object JvmClassFactory {
    fun fromJvmClassInfo(jvmClassInfo: JvmClassInfo): JvmClassApi {
        val constantPoolInfoMap = jvmClassInfo.constantPool.associateBy { it.index }
        val constantPool = adaptConstantPool(jvmClassInfo.constantPool, constantPoolInfoMap)
        val constantPoolMap = constantPool.associateBy { it.index }
        val thisClass = constantPoolMap.getValue(jvmClassInfo.thisClass.index) as ConstantPoolApi.ClassApi
        val superClass =
            if (jvmClassInfo.superClass == null) null
            else constantPoolMap.getValue(jvmClassInfo.superClass.index) as ConstantPoolApi.ClassApi
        val interfaces = adaptInterfaces(jvmClassInfo.interfaces, constantPoolMap)
        val fields = adaptFields(jvmClassInfo.fields, constantPoolMap)
        val methods = adaptMethods(jvmClassInfo.methods, constantPoolMap)
        val attributes = adaptAttributes(jvmClassInfo.attributes, constantPoolMap)
        return JvmClass(
            magic = jvmClassInfo.magic,
            minorVersion = jvmClassInfo.minorVersion,
            majorVersion = jvmClassInfo.majorVersion,
            constantPool = constantPool,
            accessFlags = jvmClassInfo.accessFlags,
            thisClass = thisClass,
            superClass = superClass,
            interfaces = interfaces,
            fields = fields,
            methods = methods,
            attributes = attributes
        )
    }

    private fun adaptConstantPool(
        constantPool: List<ConstantPoolInfo>,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): List<ConstantPoolApi> {
        return constantPool.map { constantPoolInfo ->
            adaptConstant(constantPoolInfo, constantPoolInfoMap)
        }
    }

    private fun adaptConstant(
        constantPoolInfo: ConstantPoolInfo,
        constantPoolInfoMap: Map<Int, ConstantPoolInfo>
    ): ConstantPoolApi {
        val factory = constantPoolFactoryMap[constantPoolInfo.tag]
            ?: throw IllegalArgumentException("No factory for tag ${constantPoolInfo.tag} at index ${constantPoolInfo.index}")
        return factory.create(constantPoolInfo, constantPoolInfoMap)
    }

    private fun adaptInterfaces(
        interfaces: List<ConstantPoolInfoClass>,
        constantPoolMap: Map<Int, ConstantPoolApi>
    ): List<ConstantPoolApi.ClassApi> {
        return interfaces.map { adaptInterface(it, constantPoolMap) }
    }

    private fun adaptInterface(
        interfaceInfo: ConstantPoolInfoClass,
        constantPoolMap: Map<Int, ConstantPoolApi>
    ): ConstantPoolApi.ClassApi {
        return constantPoolMap.getValue(interfaceInfo.index) as ConstantPoolApi.ClassApi
    }

    private fun adaptFields(fields: List<FieldInfo>, constantPoolMap: Map<Int, ConstantPoolApi>): List<FieldApi> {
        return fields.map { fieldInfo ->
            adaptField(fieldInfo, constantPoolMap)
        }
    }

    private fun adaptField(fieldInfo: FieldInfo, constantPoolMap: Map<Int, ConstantPoolApi>): FieldApi {
        return FieldFactory.create(fieldInfo, constantPoolMap)
    }

    private fun adaptMethods(methods: List<MethodInfo>, constantPoolMap: Map<Int, ConstantPoolApi>): List<MethodApi> {
        return methods.map { methodInfo ->
            adaptMethod(methodInfo, constantPoolMap)
        }
    }

    private fun adaptMethod(methodInfo: MethodInfo, constantPoolMap: Map<Int, ConstantPoolApi>): MethodApi {
        return MethodFactory.create(methodInfo, constantPoolMap)
    }

    private fun adaptAttributes(attributes: List<Any>, constantPoolMap: Map<Int, ConstantPoolApi>): List<AttributeApi> {
        throw UnsupportedOperationException("not implemented")
    }

    val constantPoolFactoryMap = mapOf<ConstantPoolTag, ConstantPoolFactory>(
        ConstantPoolTag.UTF8 to ConstantPoolUtf8ApiFactory,
        ConstantPoolTag.INTEGER to ConstantPoolIntegerApiFactory,
        ConstantPoolTag.FLOAT to ConstantPoolFloatApiFactory,
        ConstantPoolTag.LONG to ConstantPoolLongApiFactory,
        ConstantPoolTag.DOUBLE to ConstantPoolDoubleApiFactory,
        ConstantPoolTag.CLASS to ConstantPoolClassApiFactory,
        ConstantPoolTag.STRING to ConstantPoolStringApiFactory,
        ConstantPoolTag.FIELDREF to ConstantPoolRefApiFactory,
        ConstantPoolTag.METHODREF to ConstantPoolRefApiFactory,
        ConstantPoolTag.INTERFACEMETHODREF to ConstantPoolRefApiFactory,
        ConstantPoolTag.NAMEANDTYPE to ConstantPoolNameAndTypeApiFactory,
        ConstantPoolTag.METHODHANDLE to ConstantPoolMethodHandleApiFactory,
        ConstantPoolTag.METHODTYPE to ConstantPoolMethodTypeApiFactory,
        ConstantPoolTag.DYNAMIC to ConstantPoolDynamicApiFactory,
        ConstantPoolTag.INVOKEDYNAMIC to ConstantPoolDynamicApiFactory,
        ConstantPoolTag.MODULE to ConstantPoolModuleApiFactory,
        ConstantPoolTag.PACKAGE to ConstantPoolPackageApiFactory,
    )
}
