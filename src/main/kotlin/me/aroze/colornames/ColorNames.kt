package me.aroze.colornames

import com.github.ajalt.colormath.model.LAB
import com.github.ajalt.colormath.model.RGB
import me.aroze.colornames.tree.KDNode
import me.aroze.colornames.tree.KDTreeBuilder
import java.awt.Color
import kotlin.math.sqrt

/**
 * Represents a collection of cached colors and provides methods for finding the closest color, as well as quickly
 * retrieving a fitting name for a color.
 */
class ColorNames(colorNames: List<NamedColor>) {
    private val root: KDNode? = KDTreeBuilder.buildTree(colorNames.toMutableList(), 0)
    private var minDist = Float.POSITIVE_INFINITY
    private var bestNode: KDNode? = null

    companion object {
        @JvmStatic
        @JvmName("create")
        fun create() = ColorNameBuilder().loadDefaults().build()
    }

    private fun searchNearest(node: KDNode?, l: Float, a: Float, b: Float, depth: Int) {
        if (node == null) return

        val axis = depth % 3
        val dim = when(axis) {
            0 -> l - node.l
            1 -> a - node.a
            else -> b - node.b
        }

        val dist = sqrt(
            (l - node.l) * (l - node.l) +
            (a - node.a) * (a - node.a) +
            (b - node.b) * (b - node.b))

        if (dist < minDist) {
            minDist = dist
            bestNode = node
        }

        val firstChild = if (dim <= 0) node.left else node.right
        val secondChild = if (dim <= 0) node.right else node.left

        searchNearest(firstChild, l, a, b, depth + 1)

        if (dim * dim < minDist) {
            searchNearest(secondChild, l, a, b, depth + 1)
        }
    }

    /**
     * Finds the closest color to the given [com.github.ajalt.colormath.Color] object
     *
     * @param color the color
     * @return the closest color
     */
    fun findClosestColor(color: com.github.ajalt.colormath.Color): NamedColor {
        color.toLAB().let { lab ->
            minDist = Float.POSITIVE_INFINITY
            bestNode = null
            searchNearest(root, lab.l, lab.a, lab.b, 0)
            return bestNode?.color ?: throw IllegalStateException("No colors found")
        }
    }

    /**
     * Finds the closest color to the given hex color
     *
     * @param hex the hex color, e.g. "#FF0000" or "FF0000"
     * @return the closest color
     */
    fun findClosestColor(hex: String) =
        findClosestColor(RGB(hex))

    /**
     * Finds the closest color to the given RGB color
     *
     * @param r Red value (0-255)
     * @param g Green value (0-255)
     * @param b Blue value (0-255)
     * @return the closest color
     */
    fun findClosestColor(r: Int, g: Int, b: Int) =
        findClosestColor(RGB(r/255f, g/255f, b/255f))

    /**
     * Finds the closest color to the given [Color] object
     *
     * @param color the color
     * @return the closest color
     */
    fun findClosestColor(color: Color) =
        findClosestColor(RGB(color.red/255f, color.green/255f, color.blue/255f))


    /**
     * Finds a fitting name for any given hex color
     *
     * @param hex the hex color, e.g. "#FF0000" or "FF0000"
     * @return a nice fitting color name
     */
    fun getName(hex: String): String = findClosestColor(hex).name

    /**
     * Finds a fitting name for any given RGB color
     *
     * @param r Red value (0-255)
     * @param g Green value (0-255)
     * @param b Blue value (0-255)
     * @return a nice fitting color name
     */
    fun getName(r: Int, g: Int, b: Int): String = findClosestColor(r, g, b).name

    /**
     * Finds a fitting name for any given [LAB] color
     *
     * @param color the [LAB] color
     * @return a nice fitting color name
     */
    fun getName(lab: LAB): String = findClosestColor(lab).name

    /**
     * Finds a fitting name for any given [Color] object
     *
     * @param color the color
     * @return a nice fitting color name
     */
    fun getName(color: Color): String = findClosestColor(color).name
}