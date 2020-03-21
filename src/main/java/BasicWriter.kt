import java.io.File

interface BasicWriter {
    fun create(file: File, contents: String)
}