package me.aroze.colornames

import com.github.ajalt.colormath.model.RGB
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * Builder for loading and caching color names, giving you a [ColorNames] instance
 */
class ColorNameBuilder {
    private val colorNames = ArrayList<CachedColor>()

    /**
     * Loads the color names from the bundled colornames.csv file
     */
    fun loadDefaults(): ColorNameBuilder {
        val inputStream = javaClass.getResourceAsStream("/colornames.csv")
            ?: throw IllegalStateException("colornames.csv not found")

        colorNames.addAll(parseColorCSV(inputStream.reader(StandardCharsets.UTF_8)))
        return this
    }

    /**
     * Builds the [ColorNames] instance
     *
     * @return the [ColorNames] instance
     */
    fun build(): ColorNames {
        return ColorNames(colorNames)
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
