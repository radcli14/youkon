package com.dcengineer.youkon

import AccountServiceImpl
import App
import Storage
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize
import firebase.login.LoginViewModel
import firebase.service.LogServiceImpl
import firebase.service.StorageServiceImpl
import firebase.settings.SettingsViewModel
import firebase.sign_up.SignUpViewModel
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel


class MainActivity : ComponentActivity() {
    // Initialize services
    private val logService = LogServiceImpl()
    private val accountService = AccountServiceImpl(Firebase.auth)
    private val localStorage = Storage(this)
    private val cloudStorage = StorageServiceImpl(Firebase.firestore, accountService)

    // Initialize view models
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(localStorage, cloudStorage)
    }
    private val onboardingScreenViewModel: OnboardingScreenViewModel by viewModels()
    private val quickConvertCardViewModel: QuickConvertCardViewModel by viewModels {
        QuickConvertCardViewModelFactory(localStorage)
    }
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(logService, accountService, cloudStorage)
    }
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(accountService, logService)
    }
    private val signUpViewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory(accountService, logService)
    }

    private val tag = "MainActivity"
    private val showOnboardingKey = "showOnboarding"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize authentication with Google Firebase
        Firebase.initialize(this)

        // Get saved state of the quick convert card from last time the app was open
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        Log.d(tag, "Loaded shared preferences: ${sharedPref.all}")

        if (sharedPref.getBoolean(showOnboardingKey, false)) {
            onboardingScreenViewModel.openOnboarding()
        }

        // Specify whether the onboarding screen displays in wide (tablet) or narrow form (phones)
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        onboardingScreenViewModel.isWide = manager.phoneType == TelephonyManager.PHONE_TYPE_NONE

        Firebase.initialize(this)

        setContent {
            App(
                mainViewModel,
                quickConvertCardViewModel,
                onboardingScreenViewModel,
                loginViewModel,
                settingsViewModel,
                signUpViewModel
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Save state of the quick convert card for next time the app is opened
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(showOnboardingKey, false)
            apply()
            commit()
        }
        Log.d(tag, "Saved shared preferences: ${sharedPref.all}")

        mainViewModel.saveUserToJson()
        Log.d(tag, "Destroyed the main activity")
    }
}
