package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import viewmodel.SettingsUiState
import viewmodel.SettingsViewModel
import youkon.composeapp.generated.resources.Res
import youkon.composeapp.generated.resources.cancel
import youkon.composeapp.generated.resources.delete_account_description
import youkon.composeapp.generated.resources.delete_account_title
import youkon.composeapp.generated.resources.delete_my_account
import youkon.composeapp.generated.resources.settings
import youkon.composeapp.generated.resources.sign_out
import youkon.composeapp.generated.resources.sign_out_description
import youkon.composeapp.generated.resources.sign_out_title


//@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    viewModel: SettingsViewModel// = SettingsViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(
        initial = SettingsUiState(false)
    )
    SettingsScreenContent(
        uiState = uiState,
        onLoginClick = { viewModel.onLoginClick(openScreen) },
        onSignUpClick = { viewModel.onSignUpClick(openScreen) },
        onSignOutClick = { viewModel.onSignOutClick(restartApp) },
        onDeleteMyAccountClick = { viewModel.onDeleteMyAccountClick(restartApp) }
    )
}

//@ExperimentalMaterialApi
@OptIn(ExperimentalResourceApi::class)
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
        modifier = modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(Res.string.settings)

        //Spacer(modifier = Modifier.spacer())

        if (uiState.isAnonymousAccount) {
            /*
            RegularCardEditor(AppText.sign_in, AppIcon.ic_sign_in, "", Modifier.card()) {
                onLoginClick()
            }

            RegularCardEditor(AppText.create_account, AppIcon.ic_create_account, "", Modifier.card()) {
                onSignUpClick()
            }
             */
        } else {
            SignOutCard { onSignOutClick() }
            DeleteMyAccountCard { onDeleteMyAccountClick() }
        }
    }
}

//@ExperimentalMaterialApi
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    /*
    RegularCardEditor(AppText.sign_out, AppIcon.ic_exit, "", Modifier.card()) {
        showWarningDialog = true
    }
     */
    if (showWarningDialog) {
        /*
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

         */
    }
}

//@ExperimentalMaterialApi
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }
/*
    DangerousCardEditor(
        Res.string.delete_my_account,
        Res.drawable.ic_delete_my_account,
        "",
        Modifier.card()
    ) {
        showWarningDialog = true
    }
*/
    if (showWarningDialog) {
        /*
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
         */
    }
}