package com.seanshubin.kotlin.tryme.domain.logger

class EmitStub : (String) -> Unit {
    var invocations = mutableListOf<String>()
    override fun invoke(s: String) {
        invocations.add(s)
    }
}
