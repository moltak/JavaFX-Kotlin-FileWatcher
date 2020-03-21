import java.io.File

interface HwpReader {
    fun read(hwpFile: File): String
}