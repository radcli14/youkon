import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch
import view.MainView
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel

@Composable
fun App(
    mainViewModel: MainViewModel = MainViewModel(),
    quickConvertCardViewModel: QuickConvertCardViewModel = QuickConvertCardViewModel(),
    onboardingScreenViewModel: OnboardingScreenViewModel = OnboardingScreenViewModel()
) {
    val scope = rememberCoroutineScope()
    val auth = remember { Firebase.auth }
    var firebaseUser: FirebaseUser? by remember { mutableStateOf(null) }
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    if (firebaseUser == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = userEmail,
                onValueChange = { userEmail = it },
                placeholder = { Text(text = "Email Address") }
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = userPassword,
                onValueChange = { userPassword = it },
                placeholder = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                scope.launch {
                    firebaseUser = try {
                        auth.createUserWithEmailAndPassword(
                            email = userEmail,
                            password = userPassword
                        ).user
                    } catch (e: Exception) {
                        auth.signInWithEmailAndPassword(
                            email = userEmail,
                            password = userPassword
                        ).user
                    }
                }
            }) {
                Text(text = "Sign in")
            }
        }
    } else {
        Text(firebaseUser!!.uid)
        /*MainView(
            mainViewModel,
            quickConvertCardViewModel,
            onboardingScreenViewModel
        ).Body()*/
    }
}