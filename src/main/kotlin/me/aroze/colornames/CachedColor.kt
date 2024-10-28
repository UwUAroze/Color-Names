package me.aroze.colornames

/**
 * Represents a cached color with values from the CIELAB color space (https://en.wikipedia.org/wiki/CIELAB_color_space)
 */
data class CachedColor(
    val name: String,

    val lightness: Float,
    val aComponent: Float,
    val bComponent: Float
)
