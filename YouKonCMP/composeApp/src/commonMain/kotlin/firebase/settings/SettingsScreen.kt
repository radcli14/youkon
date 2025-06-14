package firebase.settings

import Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import fullWidthSemitransparentPadded
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.cancel
import youkon.composeapp.generated.resources.create_account
import youkon.composeapp.generated.resources.delete_account_description
import youkon.composeapp.generated.resources.delete_account_title
import youkon.composeapp.generated.resources.delete_my_account
import youkon.composeapp.generated.resources.ic_create_account
import youkon.composeapp.generated.resources.ic_delete_my_account
import youkon.composeapp.generated.resources.ic_exit
import youkon.composeapp.generated.resources.ic_sign_in
import youkon.composeapp.generated.resources.privacy_policy_button
import youkon.composeapp.generated.resources.privacy_policy_url
import youkon.composeapp.generated.resources.sign_in
import youkon.composeapp.generated.resources.sign_out
import youkon.composeapp.generated.resources.sign_out_description
import youkon.composeapp.generated.resources.sign_out_title


@Composable
fun SettingsScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsState(
        initial = SettingsUiState("Anonymous User",true)
    )
    SettingsScreenContent(
        uiState = uiState,
        onLoginClick = { viewModel.onLoginClick(openScreen) },
        onSignUpClick = { viewModel.onSignUpClick(openScreen) },
        onSignOutClick = { viewModel.onSignOutClick(restartApp) },
        onDeleteMyAccountClick = { viewModel.onDeleteMyAccountClick(restartApp) }
    )
}


@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onDeleteMyAccountClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fullWidthSemitransparentPadded()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (uiState.isAnonymousAccount) "Log in or create an account" else "Logged in as ${uiState.name}",
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        if (uiState.isAnonymousAccount) {
            RegularCardEditor(Res.string.sign_in, Res.drawable.ic_sign_in, "", Modifier.card()) {
                onLoginClick()
            }

            RegularCardEditor(Res.string.create_account, Res.drawable.ic_create_account, "", Modifier.card()) {
                onSignUpClick()
            }
        } else {
            SignOutCard { onSignOutClick() }
            DeleteMyAccountCard { onDeleteMyAccountClick() }
        }

        PrivacyPolicyButton()
    }
}


@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(Res.string.sign_out, Res.drawable.ic_exit, "", Modifier.card()) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(Res.string.sign_out_title)) },
            text = { Text(stringResource(Res.string.sign_out_description)) },
            dismissButton = { DialogCancelButton(Res.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(Res.string.sign_out) {
                    signOut()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    DangerousCardEditor(
        Res.string.delete_my_account,
        Res.drawable.ic_delete_my_account,
        "",
        Modifier.card()
    ) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(Res.string.delete_account_title)) },
            text = { Text(stringResource(Res.string.delete_account_description)) },
            dismissButton = { DialogCancelButton(Res.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(Res.string.delete_my_account) {
                    deleteMyAccount()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

fun Modifier.spacer(): Modifier {
    return this.fillMaxWidth().padding(12.dp)
}

fun Modifier.card(): Modifier {
    return this.padding(16.dp, 0.dp, 16.dp, 8.dp)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RegularCardEditor(
    title: StringResource,
    icon: DrawableResource,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.onSurface, modifier)
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun DangerousCardEditor(
    title: StringResource,
    icon: DrawableResource,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.primary, modifier)
}


@OptIn(ExperimentalResourceApi::class)
@Composable
private fun CardEditor(
    title: StringResource,
    icon: DrawableResource,
    content: String,
    onEditClick: () -> Unit,
    highlightColor: Color,
    modifier: Modifier
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier,
        onClick = onEditClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) { Text(stringResource(title), color = highlightColor) }

            if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
            }

            Icon(
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = highlightColor
            )
        }
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun DialogConfirmButton(text: StringResource, action: () -> Unit) {
    Button(
        onClick = action,
        colors =
        ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = stringResource(text))
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun DialogCancelButton(text: StringResource, action: () -> Unit) {
    Button(
        onClick = action,
        colors =
        ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = stringResource(text))
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PrivacyPolicyButton() {
    val url = stringResource(Res.string.privacy_policy_url)
    val uriHandler = LocalUriHandler.current
    TextButton(onClick = {
        Log.d("Settings Screen","Tapped Privacy Policy Button: $url")
        uriHandler.openUri(url)
    }) {
        Text(stringResource(Res.string.privacy_policy_button), modifier = Modifier.padding(16.dp))
    }
}
