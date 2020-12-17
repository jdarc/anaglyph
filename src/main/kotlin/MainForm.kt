import java.awt.BorderLayout
import java.io.File
import javax.swing.*

class MainForm : JFrame("Anaglyph") {
    private var timer: Timer
    private val viewer = Viewport()
    private lateinit var wireframe: Wireframe

    fun start() {
        wireframe = Importer.read("beethoven.obj")
        timer.start()
    }

    fun stop() = timer.stop()

    private fun load(path: String) {
        wireframe = Importer.read(path)
    }

    private fun buildButtonPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        arrayOf("ram.obj", "beetle.obj", "aircar.obj", "dooleys.obj", "nutbolt.obj", "urn.obj",
                "reel.obj", "raider.obj", "tut.obj", "scene.obj", "dinorider.obj", "spacefrigate.obj")
                .forEach { panel.add(buildButton(it)) }
        return panel
    }

    private fun buildButton(filename: String) = JButton(File(filename).nameWithoutExtension.capitalize()).apply {
        isFocusPainted = false
        addActionListener { load(filename) }
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()
        contentPane.add(viewer, BorderLayout.CENTER)
        contentPane.add(buildButtonPanel(), BorderLayout.NORTH)
        pack()
        isResizable = false
        setLocationRelativeTo(null)
        isVisible = true
        timer = Timer(16) { viewer.render(wireframe) }
    }
}
