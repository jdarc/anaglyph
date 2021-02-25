import java.lang.Math.fma
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class Vector3(val x: Double, val y: Double, val z: Double) {

    val length get() = sqrt(fma(x, x, fma(y, y, z * z)))

    operator fun plus(rhs: Vector3) = Vector3(x + rhs.x, y + rhs.y, z + rhs.z)
    operator fun minus(rhs: Vector3) = Vector3(x - rhs.x, y - rhs.y, z - rhs.z)
    operator fun times(scalar: Double) = Vector3(x * scalar, y * scalar, z * scalar)
    operator fun div(scalar: Double) = this * (1.0 / scalar)

    companion object {
        fun minimum(lhs: Vector3, rhs: Vector3) = Vector3(min(lhs.x, rhs.x), min(lhs.y, rhs.y), min(lhs.z, rhs.z))
        fun maximum(lhs: Vector3, rhs: Vector3) = Vector3(max(lhs.x, rhs.x), max(lhs.y, rhs.y), max(lhs.z, rhs.z))
        fun dot(lhs: Vector3, rhs: Vector3) = fma(lhs.x, rhs.x, fma(lhs.y, rhs.y, lhs.z * rhs.z))
        fun normalize(vec: Vector3) = vec / vec.length
    }
}
