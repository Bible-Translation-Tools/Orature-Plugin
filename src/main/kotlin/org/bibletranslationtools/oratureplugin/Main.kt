package org.bibletranslationtools.oratureplugin

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import java.io.File

class App : CliktCommand() {
    val wav: String by option(help = "Full path of the wav file to launch")
        .required()
        .validate {
            val file = File(it)
            if (!file.exists()) {
                fail("File: $it does not exist")
            }
            if (file.extension != "wav") {
                fail("File $it is not a wav file")
            }
        }

    override fun run() {
        println(wav)
    }
}

fun main(args: Array<String>) {
    App().main(args)
}
