package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.impl

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.AccessFlag
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.bytesToHex
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.FormatUtil.indent
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.AttributeApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.FieldApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.JvmClassApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.MethodApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.util.ByteUtil.intToBytes

data class JvmClass(
    override val magic: UInt,
    override val minorVersion: UShort,
    override val majorVersion: UShort,
    override val constantPool: List<ConstantPoolApi>,
    override val accessFlags: Set<AccessFlag>,
    override val thisClass: ConstantPoolApi.ClassApi,
    override val superClass: ConstantPoolApi.ClassApi?,
    override val interfaces: List<ConstantPoolApi.ClassApi>,
    override val fields: List<FieldApi>,
    override val methods: List<MethodApi>,
    override val attributes: List<AttributeApi>
): JvmClassApi {
    override fun lines(): List<String> {
        return listOf(
            "magic: ${bytesToHex(intToBytes(magic.toInt()))}",
            "minorVersion: $minorVersion",
            "majorVersion: $majorVersion",
            "accessFlags: $accessFlags",
            "thisClass: ${thisClass.line()}",
            "superClass: ${superClass?.line() ?: "null"}",
        ) + constantPoolLines() + interfaceLines() + fieldLines() + methodLines() + attributeLines()
    }
    private fun constantPoolLines(): List<String> {
        val header = "constant pool(${constantPool.size}):"
        val body = constantPool.map { it.line() }.map(indent)
        return listOf(header) + body
    }

    private fun interfaceLines(): List<String> {
        val header = "interfaces(${interfaces.size}):"
        val body = interfaces.map { it.line() }.map(indent)
        return listOf(header) + body
    }

    private fun methodLines(): List<String> {
        val header = "methods(${methods.size}):"
        val body = methods.flatMapIndexed { index, method ->
            listOf("method[$index]:") + method.lines().map(indent)
        }.map(indent)
        return listOf(header) + body
    }

    private fun fieldLines(): List<String> {
        val header = "fields(${fields.size}):"
        val body = fields.flatMapIndexed { index, field ->
            listOf("field[$index]:") + field.lines()
        }.map(indent)
        return listOf(header) + body
    }

    private fun attributeLines(): List<String> {
        val header = "attributes(${attributes.size}):"
        val body = attributes.flatMapIndexed { index, attribute ->
            listOf("attribute[$index]:") + attribute.lines()
        }.map(indent)
        return listOf(header) + body
    }
}