import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import theming.YoukonTheme
import view.BackgroundBox

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        YoukonTheme {
            BackgroundBox {
                Text("This is a web based version of YouKon!")
            }
        }
    }
}
