package view

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle

/// A composable function similar to the default `Text`, but a superscript will be used for the first character after any "^"
@Composable
fun TextWithSubscripts(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    style: TextStyle = LocalTextStyle.current
) {
    // Define the style for the superscript, shifted upward and slightly smaller than normal script
    val scriptStyleSuper = SpanStyle(
        baselineShift = BaselineShift.Superscript,
        fontSize = style.fontSize * 0.8,
    )

    // Build the annotated string using superscripts for the first character after each carrot
    val splitAtCarrots = text.split("^")
    val annotatedString = buildAnnotatedString {
        splitAtCarrots.forEachIndexed { idx, subString ->
            if (idx == 0) {
                // Beginning of string, use regular script
                append(subString)
            } else {
                // Superscript the first character
                withStyle(scriptStyleSuper) {
                    append(subString.first())
                }

                // Regular script for all except the first character
                append(subString.substring(1))
            }
        }
    }

    // The `Text` composable containing annotated strings for the superscripts
    Text(annotatedString, modifier, color, style = style)
}