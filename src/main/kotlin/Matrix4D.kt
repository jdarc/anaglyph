import java.lang.Math.fma
import kotlin.math.cos
import kotlin.math.sin

class Matrix4D(val m00: Double, val m01: Double, val m02: Double, val m03: Double,
               val m10: Double, val m11: Double, val m12: Double, val m13: Double,
               val m20: Double, val m21: Double, val m22: Double, val m23: Double,
               val m30: Double, val m31: Double, val m32: Double, val m33: Double) {

    operator fun times(rhs: Vector3D) = Vector3D(
        fma(m00, rhs.x, fma(m10, rhs.y, fma(m20, rhs.z, m30))),
        fma(m01, rhs.x, fma(m11, rhs.y, fma(m21, rhs.z, m31))),
        fma(m02, rhs.x, fma(m12, rhs.y, fma(m22, rhs.z, m32)))
    )

    operator fun times(rhs: Matrix4D) = Matrix4D(
        fma(m00, rhs.m00, fma(m01, rhs.m10, fma(m02, rhs.m20, m03 * rhs.m30))),
        fma(m00, rhs.m01, fma(m01, rhs.m11, fma(m02, rhs.m21, m03 * rhs.m31))),
        fma(m00, rhs.m02, fma(m01, rhs.m12, fma(m02, rhs.m22, m03 * rhs.m32))),
        fma(m00, rhs.m03, fma(m01, rhs.m13, fma(m02, rhs.m23, m03 * rhs.m33))),
        fma(m10, rhs.m00, fma(m11, rhs.m10, fma(m12, rhs.m20, m13 * rhs.m30))),
        fma(m10, rhs.m01, fma(m11, rhs.m11, fma(m12, rhs.m21, m13 * rhs.m31))),
        fma(m10, rhs.m02, fma(m11, rhs.m12, fma(m12, rhs.m22, m13 * rhs.m32))),
        fma(m10, rhs.m03, fma(m11, rhs.m13, fma(m12, rhs.m23, m13 * rhs.m33))),
        fma(m20, rhs.m00, fma(m21, rhs.m10, fma(m22, rhs.m20, m23 * rhs.m30))),
        fma(m20, rhs.m01, fma(m21, rhs.m11, fma(m22, rhs.m21, m23 * rhs.m31))),
        fma(m20, rhs.m02, fma(m21, rhs.m12, fma(m22, rhs.m22, m23 * rhs.m32))),
        fma(m20, rhs.m03, fma(m21, rhs.m13, fma(m22, rhs.m23, m23 * rhs.m33))),
        fma(m30, rhs.m00, fma(m31, rhs.m10, fma(m32, rhs.m20, m33 * rhs.m30))),
        fma(m30, rhs.m01, fma(m31, rhs.m11, fma(m32, rhs.m21, m33 * rhs.m31))),
        fma(m30, rhs.m02, fma(m31, rhs.m12, fma(m32, rhs.m22, m33 * rhs.m32))),
        fma(m30, rhs.m03, fma(m31, rhs.m13, fma(m32, rhs.m23, m33 * rhs.m33)))
    )

    companion object {

        fun scale(sx: Double, sy: Double, sz: Double): Matrix4D {
            return Matrix4D(sx, 0.0, 0.0, 0.0, 0.0, sy, 0.0, 0.0, 0.0, 0.0, sz, 0.0, 0.0, 0.0, 0.0, 1.0)
        }

        fun rotationX(angle: Double): Matrix4D {
            val cos = cos(angle)
            val sin = sin(angle)
            return Matrix4D(1.0, 0.0, 0.0, 0.0, 0.0, cos, sin, 0.0, 0.0, -sin, cos, 0.0, 0.0, 0.0, 0.0, 1.0)
        }

        fun rotationY(angle: Double): Matrix4D {
            val cos = cos(angle)
            val sin = sin(angle)
            return Matrix4D(cos, 0.0, -sin, 0.0, 0.0, 1.0, 0.0, 0.0, sin, 0.0, cos, 0.0, 0.0, 0.0, 0.0, 1.0)
        }

        fun rotationZ(angle: Double): Matrix4D {
            val cos = cos(angle)
            val sin = sin(angle)
            return Matrix4D(cos, sin, 0.0, 0.0, -sin, cos, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)
        }
    }
}
