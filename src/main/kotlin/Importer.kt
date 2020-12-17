import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.math.max
import kotlin.math.min

object Importer {

    fun read(filename: String): Wireframe {
        val resource = Importer::class.java.classLoader.getResourceAsStream(filename)
        if (resource != null) {
            val vertices = mutableListOf<Vector3D>()
            val indices = mutableListOf<IntArray>()
            BufferedReader(InputStreamReader(resource)).use {
                var line = it.readLine()
                while (line != null) {
                    if (line.startsWith("v ", true)) {
                        val data = line.split(" ").toTypedArray()
                        vertices.add(Vector3D(data[1].toDouble(), data[2].toDouble(), data[3].toDouble()))
                    }
                    if (line.startsWith("f ", true)) {
                        val split = line.split(" ").toTypedArray()
                        indices.add(Arrays.stream(split).skip(1).mapToInt { l -> l.split("/").first().toInt() - 1 }.toArray())
                    }
                    line = it.readLine()
                }
            }
            return Wireframe(centerAndScale(vertices), toEdges(indices))
        }
        throw NullPointerException()
    }

    private fun toEdges(faces: List<IntArray>): Array<Edge> {
        val edges = hashSetOf<Edge>()
        for (face in faces) {
            for (i in face.indices) {
                val a = face[i]
                val b = face[(i + 1) % face.size]
                edges.add(Edge(min(a, b), max(a, b)))
            }
        }
        return edges.toList().toTypedArray()
    }

    private fun centerAndScale(vertices: List<Vector3D>): Array<Vector3D> {
        var min = Vector3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        var max = Vector3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
        for (vertex in vertices) {
            min = Vector3D.minimum(min, vertex)
            max = Vector3D.maximum(max, vertex)
        }
        val cog = (max + min) / 2.0
        val dim = max - min
        val scale = 1.0 / max(dim.x, max(dim.y, dim.z))
        return vertices.map { (it - cog) * scale }.toTypedArray()
    }
}
