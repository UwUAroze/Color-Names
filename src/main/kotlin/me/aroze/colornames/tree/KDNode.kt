package me.aroze.colornames.tree

import me.aroze.colornames.NamedColor

/**
 * Represents a node in a K-D tree for cached CIELAB colors and respective names
 */
class KDNode(
    val l: Float,
    val a: Float,
    val b: Float,
    val color: NamedColor,
    var left: KDNode? = null,
    var right: KDNode? = null
)