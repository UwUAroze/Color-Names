package me.aroze.colornames

import com.github.ajalt.colormath.Color

/**
 * Represents a named color with values from the CIELAB color space (https://en.wikipedia.org/wiki/CIELAB_color_space)
 */
data class NamedColor internal constructor(val name: String) {
    /**
     * The lightness component (0-100)
     */
    var lightness: Float = 0f
        private set

    /**
     * The "a" component (-128-127)
     */
    var aComponent: Float = 0f
        private set

    /**
     * The "b" component (-128-127)
     */
    var bComponent: Float = 0f
        private set

    /**
     * Creates a new named color with the given name and CIELAB values
     *
     * @param l the lightness component (0-100)
     * @param a the a component (-128-127)
     * @param b the b component (-128-127)
     */
    constructor(name: String, l: Float, a: Float, b: Float) : this(name) {
        lightness = l
        aComponent = a
        bComponent = b
    }

    /**
     * Creates a new named color with the given name and RGB values
     *
     * @param r the red component (0-255)
     * @param g the green component (0-255)
     * @param b the blue component (0-255)
     */
    constructor(name: String, r: Int, b: Int, g: Int) : this(name) {
        val lab = com.github.ajalt.colormath.model.RGB(r/255f, g/255f, b/255f)
            .toLAB()

        lightness = lab.l
        aComponent = lab.a
        bComponent = lab.b
    }

    /**
     * Creates a new named color with the given name and hex value
     *
     * @param hex the hex color, e.g. "#FF0000" or "FF0000"
     */
    constructor(name: String, hex: String) : this(name) {
        val lab = com.github.ajalt.colormath.model.RGB(hex)
            .toLAB()

        lightness = lab.l
        aComponent = lab.a
        bComponent = lab.b
    }

    /**
     * Creates a new named color with the given name and [java.awt.Color] object
     *
     * @param color the color
     */
    constructor(name: String, color: java.awt.Color) : this(name) {
        val lab = com.github.ajalt.colormath.model.RGB(color.red/255f, color.green/255f, color.blue/255f)
            .toLAB()

        lightness = lab.l
        aComponent = lab.a
        bComponent = lab.b
    }

    /**
     * Creates a new named color with the given name and [Color] object
     *
     * @param color the color
     */
    constructor(name: String, color: Color) : this(name) {
        val lab = color.toLAB()

        lightness = lab.l
        aComponent = lab.a
        bComponent = lab.b
    }

}
