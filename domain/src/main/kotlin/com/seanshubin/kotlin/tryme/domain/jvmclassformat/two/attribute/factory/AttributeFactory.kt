package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.attribute.factory

import com.seanshubin.kotlin.tryme.domain.jvmclassformat.one.AttributeInfo
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.AttributeApi
import com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api.ConstantPoolApi

interface AttributeFactory {
    fun create(attributeInfo: AttributeInfo, constantPoolMap:Map<Int, ConstantPoolApi>): AttributeApi
    companion object {
        val attributeFactoryMap = mapOf<String, AttributeFactory>(
            "ConstantValue" to ConstantValueAttributeFactory,
            "Code" to CodeAttributeFactory,
            "StackMapTable" to StackMapTableAttributeFactory,
            "BootstrapMethods" to BootstrapMethodsAttributeFactory,
            "NestHost" to NestHostAttributeFactory,
            "NestMembers" to NestMembersAttributeFactory,
            "PermittedSubclasses" to PermittedSubclassesAttributeFactory,
            "Exceptions" to ExceptionsAttributeFactory,
            "InnerClasses" to InnerClassesAttributeFactory,
            "EnclosingMethod" to EnclosingMethodAttributeFactory,
            "Synthetic" to SyntheticAttributeFactory,
            "Signature" to SignatureAttributeFactory,
            "Record" to RecordAttributeFactory,
            "SourceFile" to SourceFileAttributeFactory,
            "LineNumberTable" to LineNumberTableAttributeFactory,
            "LocalVariableTable" to LocalVariableTableAttributeFactory,
            "LocalVariableTypeTable" to LocalVariableTypeTableAttributeFactory,
            "SourceDebugExtension" to SourceDebugExtensionAttributeFactory,
            "Deprecated" to DeprecatedAttributeFactory,
            "RuntimeVisibleAnnotations" to RuntimeVisibleAnnotationsAttributeFactory,
            "RuntimeInvisibleAnnotations" to RuntimeInvisibleAnnotationsAttributeFactory,
            "RuntimeVisibleParameterAnnotations" to RuntimeVisibleParameterAnnotationsAttributeFactory,
            "RuntimeInvisibleParameterAnnotations" to RuntimeInvisibleParameterAnnotationsAttributeFactory,
            "RuntimeVisibleTypeAnnotations" to RuntimeVisibleTypeAnnotationsAttributeFactory,
            "RuntimeInvisibleTypeAnnotations" to RuntimeInvisibleTypeAnnotationsAttributeFactory,
            "AnnotationDefault" to AnnotationDefaultAttributeFactory,
            "MethodParameters" to MethodParametersAttributeFactory,
            "Module" to ModuleAttributeFactory,
            "ModulePackages" to ModulePackagesAttributeFactory,
            "ModuleMainClass" to ModuleMainClassAttributeFactory,
        )
    }
}
