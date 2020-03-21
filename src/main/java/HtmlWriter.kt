import java.io.File

interface HtmlWriter {
    fun create(htmlFile: File, contents: String)
}