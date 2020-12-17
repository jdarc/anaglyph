import java.awt.*
import java.awt.geom.Line2D
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import javax.swing.JPanel
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class Viewport : JPanel(true) {
    private var image = BufferedImage(1440, 900, BufferedImage.TYPE_INT_ARGB_PRE)

    fun render(wireframe: Wireframe) {
        val g = image.graphics as Graphics2D
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

        g.background = background
        g.clearRect(0, 0, image.width, image.height)

        val angle = System.nanoTime() / 2000000000.0
        val eyeSpacing = 2.0
        val transform = Matrix4D.scale(80.0, 80.0, 80.0) * Matrix4D.rotationY(angle) * Matrix4D.rotationX(-0.33)

        g.stroke = BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER)
        g.composite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f)

        g.color = Color(0x0000EE)
        renderMesh(g, wireframe, transform, Vector3D(+eyeSpacing, 0.0, -100.0))

        g.color = Color(0xB30000)
        renderMesh(g, wireframe, transform, Vector3D(-eyeSpacing, 0.0, -100.0))

        g.dispose()
        repaint()
    }

    @Override
    override fun paintComponent(g: Graphics) {
        g as Graphics2D
        g.drawImage(image, null, 0, 0)
    }

    private fun renderMesh(graphics: Graphics2D, wireframe: Wireframe, world: Matrix4D, camera: Vector3D) {
        val ang = 1.0 - Vector3D.dot(Vector3D(0.0, 0.0, -1.0), Vector3D.normalize(camera))
        val cos = cos(ang)
        val sin = sin(ang)
        val line = Line2D.Double()
        for (edge in wireframe.edges) {
            val a = project(world * wireframe.vertices[edge.a], camera, cos, sin)
            val b = project(world * wireframe.vertices[edge.b], camera, cos, sin)
            line.setLine(a.x, a.y, b.x, b.y)
            graphics.draw(line)
        }
    }

    private fun project(v: Vector3D, camera: Vector3D, cos: Double, sin: Double): Point2D.Double {
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
    }
}
