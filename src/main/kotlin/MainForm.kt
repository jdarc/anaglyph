import java.awt.BorderLayout
import javax.swing.*

class MainForm : JFrame("Anaglyph") {
    private var timer: Timer
    private val viewer = Viewport()
    private lateinit var edges: Array<Edge>

    fun start() {
        edges = Importer.read("nutbolt.obj")
        timer.start()
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()
        contentPane.add(buildButtonPanel(), BorderLayout.NORTH)
        contentPane.add(viewer, BorderLayout.CENTER)
        pack()
        isResizable = false
        setLocationRelativeTo(null)
        isVisible = true
        timer = Timer(10) { viewer.render(edges) }
    }

    private fun buildButtonPanel() = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        arrayOf("ram", "beetle", "dooleys", "nutbolt", "urn", "reel", "tut", "scene", "dinorider", "spacefrigate").map { name ->
            JButton(name.capitalize()).apply {
                isFocusPainted = false
                addActionListener { edges = Importer.read("${name}.obj") }
            }
        }.forEach { add(it) }
    }
}
