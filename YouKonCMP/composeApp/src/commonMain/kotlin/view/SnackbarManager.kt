/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package view

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.generic_error

object SnackbarManager {
  private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
  val snackbarMessages: StateFlow<SnackbarMessage?>
    get() = messages.asStateFlow()

  @OptIn(ExperimentalResourceApi::class)
  fun showMessage(message: StringResource) {
    messages.value = SnackbarMessage.ResourceSnackbar(message)
  }

  fun showMessage(message: SnackbarMessage) {
    messages.value = message
  }

  fun clearSnackbarState() {
    messages.value = null
  }
}


sealed class SnackbarMessage {
  class StringSnackbar(val message: String) : SnackbarMessage()
  @OptIn(ExperimentalResourceApi::class)
  class ResourceSnackbar(val message: StringResource) : SnackbarMessage()

  companion object {
    @Composable
    @OptIn(ExperimentalResourceApi::class)
    fun SnackbarMessage.toMessage(): String {
      return when (this) {
        is StringSnackbar -> this.message
        is ResourceSnackbar -> stringResource(this.message)
      }
    }

    @OptIn(ExperimentalResourceApi::class)
    fun Throwable.toSnackbarMessage(): SnackbarMessage {
      val message = this.message.orEmpty()
      return if (message.isNotBlank()) StringSnackbar(message)
      else ResourceSnackbar(Res.string.generic_error)
    }
  }
}
