import java.lang.Math.fma
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class Vector3D(val x: Double, val y: Double, val z: Double) {

    val length = sqrt(fma(x, x, fma(y, y, z * z)))

    operator fun plus(rhs: Vector3D) = Vector3D(x + rhs.x, y + rhs.y, z + rhs.z)
    operator fun minus(rhs: Vector3D) = Vector3D(x - rhs.x, y - rhs.y, z - rhs.z)
    operator fun times(scalar: Double) = Vector3D(x * scalar, y * scalar, z * scalar)
    operator fun div(scalar: Double) = this * (1.0 / scalar)

    companion object {
        fun minimum(lhs: Vector3D, rhs: Vector3D) = Vector3D(min(lhs.x, rhs.x), min(lhs.y, rhs.y), min(lhs.z, rhs.z))
        fun maximum(lhs: Vector3D, rhs: Vector3D) = Vector3D(max(lhs.x, rhs.x), max(lhs.y, rhs.y), max(lhs.z, rhs.z))
        fun dot(lhs: Vector3D, rhs: Vector3D) = fma(lhs.x, rhs.x, fma(lhs.y, rhs.y, lhs.z * rhs.z))
        fun normalize(vec: Vector3D) = vec / vec.length
    }
}
