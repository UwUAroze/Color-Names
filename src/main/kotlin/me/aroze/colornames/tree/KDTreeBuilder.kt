package me.aroze.colornames.tree

import me.aroze.colornames.NamedColor

/**
 * Builds a K-D tree from a list of cached colors
 */
object KDTreeBuilder {
    /**
     * Recursively builds of a K-D tree from a list of cached colors.
     *
     * @param points the list of cached colors
     * @param depth the current depth of the tree
     * @return the root node of the K-D tree
     */
    fun buildTree(points: MutableList<NamedColor>, depth: Int): KDNode? {
        if (points.isEmpty()) return null

        val axis = depth % 3
        val mid = points.size / 2

        points.sortWith(compareBy { color ->
            when(axis) {
                0 -> color.lightness
                1 -> color.aComponent
                else -> color.bComponent
            }
        })

        val node = points[mid].let { color ->
            KDNode(color.lightness, color.aComponent, color.bComponent, color)
        }

        node.left = buildTree(points.subList(0, mid), depth + 1)
        node.right = buildTree(points.subList(mid + 1, points.size), depth + 1)

        return node
    }
}