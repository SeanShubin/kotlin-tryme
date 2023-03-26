package com.seanshubin.kotlin.tryme.domain.dependency

data class Module(
    val path: ModulePath,
    val dependsOn:List<ModulePath>
) {
    val simpleName:String get() = path.simpleName
    fun linkName(makeLink:(ModulePath)->String?):String = path.linkName(makeLink)
    fun toObject():Map<String, Any> = mapOf(
        "path" to path.toObject(),
        "singleList" to dependsOn.map{it.toObject()}
    )
}
