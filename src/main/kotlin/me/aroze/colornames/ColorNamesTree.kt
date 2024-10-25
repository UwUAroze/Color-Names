package me.aroze.colornames

import com.github.ajalt.colormath.model.LAB
import com.github.ajalt.colormath.model.RGB
import kotlin.math.*

class ColorNamesTree(
    colorNames: List<CachedColor>
) {
    private val kdTree: KDNode?

    init {
        kdTree = buildKDTree(colorNames.map { Triple(it.lightness, it.aComponent, it.bComponent) to it }.toList(), 0)
    }

    fun findClosestColor(hex: String): CachedColor {
        return findClosestColor(
            RGB(hex)
                .toLAB()
        )
    }

    fun findClosestColor(r: Int, g: Int, b: Int): CachedColor {
        return findClosestColor(
            RGB(r/255f, g/255f, b/255f)
                .toLAB()
        )
    }

    fun findClosestColor(lab: LAB): CachedColor {
        val point = Triple(lab.l, lab.a, lab.b)
        return findNearest(kdTree, point, 0)?.second
            ?: throw IllegalStateException("No colors found?")
    }

    private class KDNode(
        val point: Triple<Float, Float, Float>,
        val color: CachedColor,
        val left: KDNode?,
        val right: KDNode?
    )

    private fun buildKDTree(
        points: List<Pair<Triple<Float, Float, Float>, CachedColor>>,
        depth: Int
    ): KDNode? {
        if (points.isEmpty()) return null

        val k = 3 // We're working in 3D space (L*a*b*)
        val axis = depth % k

        // Sort points based on current axis
        val sortedPoints = points.sortedBy { (point, _) ->
            when (axis) {
                0 -> point.first  // L
                1 -> point.second // a
                else -> point.third // b
            }
        }

        val medianIdx = sortedPoints.size / 2
        val medianPoint = sortedPoints[medianIdx]

        return KDNode(
            medianPoint.first,
            medianPoint.second,
            buildKDTree(sortedPoints.subList(0, medianIdx), depth + 1),
            buildKDTree(sortedPoints.subList(medianIdx + 1, sortedPoints.size), depth + 1)
        )
    }

    private fun findNearest(
        node: KDNode?,
        target: Triple<Float, Float, Float>,
        depth: Int
    ): Pair<Triple<Float, Float, Float>, CachedColor>? {
        if (node == null) return null

        val axis = depth % 3
        val nextBranch: KDNode?
        val oppositeBranch: KDNode?

        // Determine which branch to search first
        val comparison = when (axis) {
            0 -> target.first.compareTo(node.point.first)
            1 -> target.second.compareTo(node.point.second)
            else -> target.third.compareTo(node.point.third)
        }

        if (comparison <= 0) {
            nextBranch = node.left
            oppositeBranch = node.right
        } else {
            nextBranch = node.right
            oppositeBranch = node.left
        }

        // Search down the most promising branch
        val best = findNearest(nextBranch, target, depth + 1)
        var bestPair = if (best == null || squaredDeltaE(target, node.point) < squaredDeltaE(target, best.first)) {
            Pair(node.point, node.color)
        } else {
            best
        }

        // Check if we need to search the other branch
        val axisDist = when (axis) {
            0 -> abs(target.first - node.point.first)
            1 -> abs(target.second - node.point.second)
            else -> abs(target.third - node.point.third)
        }

        if (axisDist * axisDist < squaredDeltaE(target, bestPair.first)) {
            val best2 = findNearest(oppositeBranch, target, depth + 1)
            if (best2 != null && squaredDeltaE(target, best2.first) < squaredDeltaE(target, bestPair.first)) {
                bestPair = best2
            }
        }

        return bestPair
    }

    /**
     * Calculate the squared delta-e between two points in the CIELAB color space
     */
    private fun squaredDeltaE(
        p1: Triple<Float, Float, Float>,
        p2: Triple<Float, Float, Float>
    ): Float {
        val dL = p1.first - p2.first
        val da = p1.second - p2.second
        val db = p1.third - p2.third
        return abs(dL * dL + da * da + db * db)
    }
}
