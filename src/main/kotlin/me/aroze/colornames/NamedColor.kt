package me.aroze.colornames

/**
 * Represents a named color with values from the CIELAB color space (https://en.wikipedia.org/wiki/CIELAB_color_space)
 */
data class NamedColor(
    val name: String,

    val lightness: Float,
    val aComponent: Float,
    val bComponent: Float
) {
    companion object {
        @JvmStatic
        @JvmName("fromRGB")
        fun fromRGB(name: String, r: Int, g: Int, b: Int): NamedColor {
            val lab = com.github.ajalt.colormath.model.RGB(r/255f, g/255f, b/255f)
                .toLAB()

            return NamedColor(name, lab.l, lab.a, lab.b)
        }

        @JvmStatic
        @JvmName("fromHex")
        fun fromHex(name: String, hex: String): NamedColor {
            val lab = com.github.ajalt.colormath.model.RGB(hex)
                .toLAB()

            return NamedColor(name, lab.l, lab.a, lab.b)
        }

        @JvmStatic
        @JvmName("fromColor")
        fun fromColor(name: String, color: java.awt.Color): NamedColor {
            val lab = com.github.ajalt.colormath.model.RGB(color.red/255f, color.green/255f, color.blue/255f)
                .toLAB()

            return NamedColor(name, lab.l, lab.a, lab.b)
        }
    }
}