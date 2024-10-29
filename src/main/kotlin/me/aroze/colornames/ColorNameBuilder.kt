package me.aroze.colornames

import com.github.ajalt.colormath.model.RGB
import jdk.internal.vm.vector.VectorSupport.test
import java.awt.Color
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
     * Loads the color names from the bundled colornames.csv file. This file contains ~30,000 color names and hex values,
     * it can be found in the [color names repository](https://github.com/UwUAroze/Color-Names/blob/master/src/main/resources/colornames.csv)
     *
     * @return this builder
     */
    fun loadDefaults(): ColorNameBuilder {
        val inputStream = javaClass.getResourceAsStream("/colornames.csv")
            ?: throw IllegalStateException("colornames.csv not found")

        colorNames.addAll(parseColorCSV(inputStream.reader(StandardCharsets.UTF_8)))
        return this
    }

    /**
     * Adds a [NamedColor] to the builder, which will be included in the final [ColorNames] instance
     *
     * @param color the [NamedColor] to add
     * @return this builder
     */
    fun addColor(color: NamedColor): ColorNameBuilder {
        colorNames.add(color)
        return this
    }

    /**
     * Adds a hex color to the builder, which will be included in the final [ColorNames] instance
     *
     * @param name the name of the color
     * @param hex the hex value of the color (e.g. "#FF0000" or "FF0000")
     * @return this builder
     */
    fun addColor(name: String, hex: String): ColorNameBuilder {
        colorNames.add(NamedColor(name, hex))
        return this
    }

    /**
     * Adds an RGB color to the builder, which will be included in the final [ColorNames] instance
     *
     * @param name the name of the color
     * @param r the red component (0-255)
     * @param g the green component (0-255)
     * @param b the blue component (0-255)
     * @return this builder
     */
    fun addColor(name: String, r: Int, g: Int, b: Int): ColorNameBuilder {
        colorNames.add(NamedColor(name, r, g, b))
        return this
    }

    /**
     * Adds a [Color] to the builder, which will be included in the final [ColorNames] instance
     *
     * @param name the name of the color
     * @param color the [Color] object
     * @return this builder
     */
    fun addColor(name: String, color: java.awt.Color): ColorNameBuilder {
        colorNames.add(NamedColor(name, color))
        return this
    }

    /**
     * Adds a hex color to the builder, which will be included in the final [ColorNames] instance
     *
     * @param colors key-value pair of the name and hex value
     * @return this builder
     */
    fun addColor(colors: Pair<String, String>): ColorNameBuilder {
        colorNames.add(NamedColor(colors.first, colors.second))
        return this
    }

    /**
     * Adds a [Color] to the builder, which will be included in the final [ColorNames] instance
     *
     * @param colors the list of [NamedColor] to add
     * @return this builder
     */
    fun addColors(vararg colors: NamedColor): ColorNameBuilder {
        colorNames.addAll(colors)
        return this
    }

    /**
     * Adds a set of hex values to the builder, which will be included in the final [ColorNames] instance
     *
     * @param colors list of key-value pairs of the name and hex value
     * @return this builder
     */
    fun addColors(colors: List<Pair<String, String>>): ColorNameBuilder {
        colorNames.addAll(colors.map { (name, hex) -> NamedColor(name, hex) })
        return this
    }

    /**
     * Adds a set of hex values to the builder, which will be included in the final [ColorNames] instance
     *
     * @param colors list of key-value pairs of the name and hex value
     * @return this builder
     */
    fun addColors(vararg colors: Pair<String, String>): ColorNameBuilder {
        colorNames.addAll(colors.map { (name, hex) -> NamedColor(name, hex) })
        return this
    }

    /**
     * Adds hex values from a CSV file to the builder, which will be included in the final [ColorNames] instance.
     *
     * The CSV file should have the format "name,hex", with the first line being the header and the following lines
     * containing the name and hex value.
     *
     * Example format: [colornames.csv from color names repository](https://github.com/UwUAroze/Color-Names/blob/master/src/main/resources/colornames.csv)
     *
     * @param reader the reader for the CSV file
     * @return this builder
     */
    fun addColors(reader: InputStreamReader): ColorNameBuilder {
        colorNames.addAll(parseColorCSV(reader))
        return this
    }

    /**
     * Adds hex values from a CSV file to the builder, which will be included in the final [ColorNames] instance.
     *
     * The CSV file should have the format "name,hex", with the first line being the header and the following lines
     * containing the name and hex value.
     *
     * Example format: [colornames.csv from color names repository](https://github.com/UwUAroze/Color-Names/blob/master/src/main/resources/colornames.csv)
     * @param file the CSV file
     * @return this builder
     */
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
