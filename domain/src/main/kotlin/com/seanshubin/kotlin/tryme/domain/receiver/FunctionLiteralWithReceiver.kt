package com.seanshubin.kotlin.tryme.domain.receiver

fun main(args: Array<String>) {
    val result =
        html {
            head {
                title { +"HTML encoding with Kotlin" }
            }
            body {
                h1 { +"HTML encoding with Kotlin" }
                p { +"this format can be used as an alternative markup to HTML" }

                // an element with attributes and text content
                a(href = "http://jetbrains.com/kotlin") { +"Kotlin" }

                // mixed content
                p {
                    +"This is some"
                    b { +"mixed" }
                    +"text. For more see the"
                    a(href = "http://jetbrains.com/kotlin") { +"Kotlin" }
                    +"project"
                }
                p { +"some text" }

                // content generated from command-line arguments
                p {
                    +"Command line arguments were:"
                    ul {
                        for (arg in args)
                            li { +arg }
                    }
                }
            }
        }
    println(result)
}

fun html(init: Html.() -> Unit): Html {
    val html = Html()
    html.init()
    return html
}
