package me.aroze.colornames

import com.github.ajalt.colormath.model.RGB
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * Builder for loading and caching color names, giving you a [ColorNames] instance
 */
class ColorNameBuilder {
    private val colorNames = ArrayList<NamedColor>()

    /**
     * Loads the color names from the bundled colornames.csv file
     */
    fun loadDefaults(): ColorNameBuilder {
        val inputStream = javaClass.getResourceAsStream("/colornames.csv")
            ?: throw IllegalStateException("colornames.csv not found")

        colorNames.addAll(parseColorCSV(inputStream.reader(StandardCharsets.UTF_8)))
        return this
    }

    fun addColor(color: NamedColor): ColorNameBuilder {
        colorNames.add(color)
        return this
    }

    fun addColors(colors: List<NamedColor>): ColorNameBuilder {
        colorNames.addAll(colors)
        return this
    }

    fun addColors(vararg colors: NamedColor): ColorNameBuilder {
        colorNames.addAll(colors)
        return this
    }

    fun addColors(colors: List<Pair<String, String>>): ColorNameBuilder {
        colorNames.addAll(colors.map { (name, hex) -> NamedColor.fromHex(name, hex) })
        return this
    }

    fun addColors(vararg colors: Pair<String, String>): ColorNameBuilder {
        colorNames.addAll(colors.map { (name, hex) -> NamedColor.fromHex(name, hex) })
        return this
    }

    fun addColors(reader: InputStreamReader): ColorNameBuilder {
        colorNames.addAll(parseColorCSV(reader))
        return this
    }

    fun addColors(file: File): ColorNameBuilder {
        colorNames.addAll(parseColorCSV(file.inputStream().reader(StandardCharsets.UTF_8)))
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

    private fun parseColorCSV(reader: InputStreamReader): List<NamedColor> {
        return BufferedReader(reader, 32768).use { buffered ->
            buffered.lineSequence()
                .drop(1)
                .map { line ->
                    val commaIndex = line.indexOf(',')
                    val name = line.substring(0, commaIndex)
                    val hex = line.substring(commaIndex + 1)

                    val lab = RGB(hex)
                        .toLAB()

                    NamedColor(name, lab.l, lab.a, lab.b)
                }
                .toList()
        }
    }
}