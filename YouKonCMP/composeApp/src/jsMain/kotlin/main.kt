import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main()  {
    onWasmReady {
        CanvasBasedWindow(title = "YouKon - Web Preview") {
            Text("This is a web based version of YouKon!")
        }
    }
}
