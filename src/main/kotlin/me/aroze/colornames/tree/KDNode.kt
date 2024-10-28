package me.aroze.colornames.tree

import me.aroze.colornames.CachedColor

/**
 * Represents a node in a K-D tree for cached CIELAB colors and respective names
 */
class KDNode(
        val l: Float,
        val a: Float,
        val b: Float,
        val color: CachedColor,
        var left: KDNode? = null,
        var right: KDNode? = null
)
