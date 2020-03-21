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
            it.basicReader = object : BasicReader {
                override fun read(file: File): String {
                    return "Source file path ${file.absolutePath}"
                }
            }

            it.basicParser = object : BasicParser {
                override fun parse(contents: String): String {
                    return contents
                }
            }

            it.basicWriter = object : BasicWriter {
                override fun create(file: File, contents: String) {
                    println("Destination file path: ${file.absolutePath}")
                    println("Parsed contents: $contents")
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