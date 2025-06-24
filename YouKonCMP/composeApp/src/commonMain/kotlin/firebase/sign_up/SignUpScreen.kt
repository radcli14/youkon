package firebase.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import firebase.login.BasicButton
import firebase.login.EmailField
import firebase.login.MessageType
import firebase.login.PasswordField
import firebase.login.RepeatPasswordField
import firebase.login.UserMessage
import firebase.login.basicButton
import firebase.login.fieldModifier
import fullWidthSemitransparentPadded
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.create_account
import youkon.composeapp.generated.resources.create_account_hint
import youkon.composeapp.generated.resources.password_match_error
import kotlinx.coroutines.delay


@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: SignUpViewModel
) {
    val uiState by viewModel.uiState
    val message by viewModel.message.collectAsState()

    SignUpScreenContent(
        uiState = uiState,
        message = message,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onSignUpClick = { viewModel.onSignUpClick(openAndPopUp) },
        clearMessage = viewModel::clearMessage
    )
}

@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    message: UserMessage?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    clearMessage: () -> Unit
) {
    val fieldModifier = Modifier.fieldModifier()

    Column(
        modifier = modifier
            .fullWidthSemitransparentPadded()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.create_account_hint),
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        EmailField(uiState.email, onEmailChange, fieldModifier)
        PasswordField(uiState.password, onPasswordChange, fieldModifier)
        RepeatPasswordField(uiState.repeatPassword, onRepeatPasswordChange, fieldModifier)

        BasicButton(
            text = Res.string.create_account,
            modifier = Modifier.basicButton(),
            action = onSignUpClick,
            isLoading = uiState.isLoading
        )
        if (message != null) {
            Text(
                text = stringResource(message.text),
                color = when (message.type) {
                    MessageType.SUCCESS -> MaterialTheme.colorScheme.primary
                    MessageType.ERROR -> MaterialTheme.colorScheme.error
                },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LaunchedEffect(message) {
                delay(3000L)
                clearMessage()
            }
        }
    }
}
