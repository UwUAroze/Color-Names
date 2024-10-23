package me.aroze.colornames

import java.io.InputStreamReader
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        val colorNames = ColorNameBuilder()
            .loadDefaults()
            .build()
    }.also { println("Loaded in $it ms") }
}

class ColorNameBuilder {

    private val colorNames = mutableListOf<ColorName>()

    fun loadDefaults(): ColorNameBuilder {
        val inputStream = javaClass.getResourceAsStream("/colornames.csv")
            ?: throw IllegalStateException("colornames.csv not found")

        colorNames.addAll(parseColorCSV(inputStream.reader()))
        return this
    }

    fun build(): ColorNames {
        return ColorNames(colorNames)
    }

    private fun parseColorCSV(reader: InputStreamReader): List<ColorName> {
        return reader.readLines()
            .drop(1)
            .map { line ->
                val (name, hex) = line
                    .replace("#", "")
                    .split(",")
                val red = hex.substring(0, 2).toInt(16)
                val green = hex.substring(2, 4).toInt(16)
                val blue = hex.substring(4, 6).toInt(16)
                ColorName(name, red, green, blue)
            }
    }

}
