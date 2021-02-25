import java.awt.*
import java.awt.geom.Path2D
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import javax.swing.JPanel
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class Viewport : JPanel(true) {
    private var additive: Additive
    private var image = BufferedImage(1440, 900, Transparency.TRANSLUCENT)

    fun render(edges: Array<Edge>) {
        val g = image.graphics as Graphics2D
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED)

        g.background = background
        g.clearRect(0, 0, image.width, image.height)

        val angle = System.nanoTime() / 2000000000.0
        val eyeSpacing = 0.5
        val transform = Matrix4.scale(80.0, 80.0, 80.0) * Matrix4.rotationY(angle) * Matrix4.rotationX(-0.33)

        g.stroke = BasicStroke(2F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER)
        g.composite = additive

        g.color = Color(0xff0000)
        renderMesh(g, edges, transform, Vector3(+eyeSpacing, 0.0, -100.0))

        g.color = Color(0x0088ff)
        renderMesh(g, edges, transform, Vector3(-eyeSpacing, 0.0, -100.0))

        g.dispose()
        repaint()
    }

    override fun paintComponent(g: Graphics) = (g as Graphics2D).drawImage(image, null, 0, 0)

    private val path = Path2D.Double()
    private fun renderMesh(graphics: Graphics2D, edges: Array<Edge>, world: Matrix4, camera: Vector3) {
        val ang = 1.0 - Vector3.dot(Vector3(0.0, 0.0, -1.0), Vector3.normalize(camera))
        val cos = cos(ang)
        val sin = sin(ang)
        path.reset()
        for (edge in edges) {
            val a = project(world * edge.a, camera, cos, sin)
            val b = project(world * edge.b, camera, cos, sin)
            path.moveTo(a.x, a.y)
            path.lineTo(b.x, b.y)
        }
        graphics.draw(path)
    }

    private fun project(v: Vector3, camera: Vector3, cos: Double, sin: Double): Point2D.Double {
        val vx = v.x * cos - v.z * sin
        val vy = v.y
        val vz = v.z * cos + v.x * sin
        val scale = min(image.width, image.height).toDouble() / (vz - camera.z)
        return Point2D.Double(0.5 * image.width + (vx + camera.x) * scale, 0.5 * image.height - vy * scale)
    }

    init {
        size = Dimension(image.width, image.height)
        preferredSize = size
        background = Color(0x1E1A2A)
        additive = Additive()
    }
}
