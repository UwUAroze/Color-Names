package me.aroze.colornames

import com.github.ajalt.colormath.model.RGB
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import kotlin.system.measureNanoTime

class ColorNameBuilder {
    private val colorNames = ArrayList<CachedColor>()

    fun loadDefaults(): ColorNameBuilder {
        val inputStream = javaClass.getResourceAsStream("/colornames.csv")
            ?: throw IllegalStateException("colornames.csv not found")

        colorNames.addAll(parseColorCSV(inputStream.reader(StandardCharsets.UTF_8)))
        return this
    }

    fun build(): ColorNamesTree {
        return ColorNamesTree(colorNames)
    }

    private fun parseColorCSV(reader: InputStreamReader): List<CachedColor> {
        return BufferedReader(reader, 32768).use { buffered ->
            buffered.lineSequence()
                .drop(1)
                .map { line ->
                    val commaIndex = line.indexOf(',')
                    val name = line.substring(0, commaIndex)
                    val hex = line.substring(commaIndex + 1)

                    val lab = RGB(hex)
                        .toLAB()

                    CachedColor(name, lab.l, lab.a, lab.b)
                }
                .toList()
        }
    }
}
