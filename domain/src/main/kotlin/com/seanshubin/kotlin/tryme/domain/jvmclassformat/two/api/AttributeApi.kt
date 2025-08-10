package com.seanshubin.kotlin.tryme.domain.jvmclassformat.two.api

interface AttributeApi {
    val name: ConstantPoolApi.Utf8Api
    interface ConstantValueApi : AttributeApi
    interface CodeApi : AttributeApi
    interface StackMapTableApi : AttributeApi
    interface ExceptionsApi : AttributeApi
    interface InnerClassesApi : AttributeApi
    interface EnclosingMethodApi : AttributeApi
    interface SyntheticApi : AttributeApi
    interface SignatureApi : AttributeApi
    interface SourceFileApi : AttributeApi
    interface SourceDebugExtensionApi : AttributeApi
    interface LineNumberTableApi : AttributeApi
    interface LocalVariableTableApi : AttributeApi
    interface LocalVariableTypeTableApi : AttributeApi
    interface DeprecatedApi : AttributeApi
    interface RuntimeVisibleAnnotationsApi : AttributeApi
    interface RuntimeInvisibleAnnotationsApi : AttributeApi
    interface RuntimeVisibleParameterAnnotationsApi : AttributeApi
    interface RuntimeInvisibleParameterAnnotationsApi : AttributeApi
    interface RuntimeVisibleTypeAnnotationsApi : AttributeApi
    interface RuntimeInvisibleTypeAnnotationsApi : AttributeApi
    interface AnnotationDefaultApi : AttributeApi
    interface BootstrapMethodsApi : AttributeApi
    interface MethodParametersApi : AttributeApi
    interface ModuleApi : AttributeApi
    interface ModulePackagesApi : AttributeApi
    interface ModuleMainClassApi : AttributeApi
    interface NestHostApi : AttributeApi
    interface NestMembersApi : AttributeApi
    interface RecordApi : AttributeApi
    interface PermittedSubclassesApi : AttributeApi
}
