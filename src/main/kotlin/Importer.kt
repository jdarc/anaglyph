import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.math.max
import kotlin.math.min

object Importer {

    fun read(filename: String): Array<Edge> {
        val resource = Importer::class.java.classLoader.getResourceAsStream(filename)
        if (resource != null) {
            val vertices = mutableListOf<Vector3>()
            val indices = mutableListOf<IntArray>()
            BufferedReader(InputStreamReader(resource)).use { reader ->
                var line = reader.readLine()
                while (line != null) {
                    if (line.startsWith("v ", true)) {
                        val data = line.split(' ').drop(1).map { it.toDouble() }.toTypedArray()
                        vertices.add(Vector3(data[0], data[1], data[2]))
                    }
                    if (line.startsWith("f ", true)) {
                        val split = line.split(" ").drop(1).toTypedArray()
                        indices.add(Arrays.stream(split).mapToInt { l -> l.split("/").first().toInt() - 1 }.toArray())
                    }
                    line = reader.readLine()
                }
            }
            return toEdges(centerAndScale(vertices), indices)
        }
        throw NullPointerException()
    }

    private fun toEdges(vertices: Array<Vector3>, indices: List<IntArray>): Array<Edge> {
        val edges = hashSetOf<Edge>()
        for (face in indices) {
            for (i in face.indices) {
                val a = face[i]
                val b = face[(i + 1) % face.size]
                edges.add(Edge(vertices[min(a, b)], vertices[max(a, b)]))
            }
        }
        return edges.toTypedArray()
    }

    private fun centerAndScale(vertices: List<Vector3>): Array<Vector3> {
        var min = Vector3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        var max = Vector3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
        for (vertex in vertices) {
            min = Vector3.minimum(min, vertex)
            max = Vector3.maximum(max, vertex)
        }
        val dim = max - min
        val cog = (max + min) / 2.0
        val scale = 1.0 / max(dim.x, max(dim.y, dim.z))
        return vertices.map { (it - cog) * scale }.toTypedArray()
    }
}
