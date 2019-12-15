package com.seanshubin.kotlin.tryme.domain.receiver

abstract class BodyTag(name: String) : TagWithText(name) {
    fun b(init: B.() -> Unit) = initTag(B(), init)
    fun p(init: P.() -> Unit) = initTag(P(), init)
    fun h1(init: H1.() -> Unit) = initTag(H1(), init)
    fun ul(init: UL.() -> Unit) = initTag(UL(), init)
    fun a(href: String, init: A.() -> Unit) {
        val a = initTag(A(), init)
        a.href = href
    }

    class Body : BodyTag("body")
    class UL : BodyTag("ul") {
        fun li(init: LI.() -> Unit) = initTag(LI(), init)
    }

    class B : BodyTag("b")
    class LI : BodyTag("li")
    class P : BodyTag("p")
    class H1 : BodyTag("h1")

    class A : BodyTag("a") {
        var href: String
            get() = attributes["href"]!!
            set(value) {
                attributes["href"] = value
            }
    }
}