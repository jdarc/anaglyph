import java.awt.Composite
import java.awt.CompositeContext
import java.awt.RenderingHints
import java.awt.image.ColorModel
import java.awt.image.Raster
import java.awt.image.WritableRaster

class Additive : Composite, CompositeContext {
    private var srcPixels = IntArray(256)
    private var dstPixels = IntArray(256)

    override fun compose(src: Raster, dstIn: Raster, dstOut: WritableRaster) {
        val width = src.width.coerceAtMost(dstIn.width)
        val height = src.height.coerceAtMost(dstIn.height)
        if (srcPixels.size < width) srcPixels = IntArray(width)
        if (dstPixels.size < width) dstPixels = IntArray(width)
        for (y in 0 until height) {
            src.getDataElements(0, y, width, 1, srcPixels)
            dstIn.getDataElements(0, y, width, 1, dstPixels)
            for (x in 0 until width) dstPixels[x] = add(srcPixels[x], dstPixels[x])
            dstOut.setDataElements(0, y, width, 1, dstPixels)
        }
    }

    override fun createContext(srcColorModel: ColorModel, dstColorModel: ColorModel, hints: RenderingHints): CompositeContext {
        return this
    }

    override fun dispose() {}

    companion object {
        private const val a = 0xFF shl 24
        private fun add(x: Int, y: Int): Int {
            val b = (x and 0xFF) + (y and 0xFF)
            val g = (x shr 8 and 0xFF) + (y shr 8 and 0xFF)
            val r = (x shr 16 and 0xFF) + (y shr 16 and 0xFF)
            return b.coerceAtMost(255) or g.coerceAtMost(255).shl(8) or r.coerceAtMost(255).shl(16) or a
        }
    }
}
