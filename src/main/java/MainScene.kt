import com.github.davidmoten.rx2.file.Files
import com.sun.nio.file.SensitivityWatchEventModifier
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class MainScene {

    lateinit var stage: Stage
    lateinit var basicParser: BasicParser
    lateinit var basicWriter: BasicWriter
    lateinit var basicReader: BasicReader

    private var ref: Optional<Disposable> = Optional.empty()

    @FXML
    private lateinit var sourceLabel: Label

    @FXML
    private lateinit var destinationLabel: Label

    @FXML
    fun uploadClicked() {}

    @FXML
    fun watchFileClicked() {
        val selectedFile: File? = FileChooser().let { fileChooser ->
            fileChooser.extensionFilters.addAll(FileChooser.ExtensionFilter("HWP Files", "*"))

            fileChooser.showOpenDialog(stage)
        };

        selectedFile?.let { file ->
            val hwpPath = file.absolutePath
            val htmlPath = file.absolutePath.let { path ->
                path.removeRange(path.indexOfLast { it == '.' }, path.length).plus(".html")
            }

            sourceLabel.text = hwpPath
            destinationLabel.text = htmlPath

            val disposable = Files.watch(file)
                .nonBlocking()
                .pollInterval(500, TimeUnit.MILLISECONDS, Schedulers.io())
                .modifier(SensitivityWatchEventModifier.HIGH)
                .build()
                .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe {
                    basicWriter.create(File(htmlPath), basicParser.parse(basicReader.read(file)))

                    Platform.runLater {
                        destinationLabel.text = "Updated"
                    }
                }

            ref.ifPresent { it.dispose() }
            ref = Optional.of(disposable)
        }
    }
}