import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.File

class MainStage : Application() {
    override fun start(stage: Stage) {
        val loader = FXMLLoader().also {
            it.location = javaClass.getResource("/mainScene.fxml")
        }

        val root = loader.load<Parent>()

        loader.getController<MainScene>().also {
            it.stage = stage
            it.hwpReader = object : HwpReader {
                override fun read(hwpFile: File): String {
                    return "Read the ${hwpFile.absolutePath}"
                }
            }

            it.hwpParser = object : HwpParser {
                override fun parse(contents: String): String {
                    return "Parsed Hwp: $contents"
                }
            }

            it.htmlWriter = object : HtmlWriter {
                override fun create(htmlFile: File, contents: String) {
                    println("HTML File Path: ${htmlFile.absolutePath}")
                    println("Parsed contents for HTML: $contents")
                }
            }
        }

        stage.run {
            scene = Scene(root)
            show()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: String) {
            launch(args)
        }
    }
}