package com.dcengineer.youkon

import Storage
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import view.MainView
import viewmodel.MainViewModel
import viewmodel.OnboardingScreenViewModel
import viewmodel.QuickConvertCardViewModel

/// The `MainViewModelFactory` exists so that the storage class can be provided on creation
class MainViewModelFactory(private val storage: Storage): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(storage) as T
}

/// The `QuickConvertCardViewModelFactory` exists so that the storage class can be provided on creation
class QuickConvertCardViewModelFactory(private val storage: Storage): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = QuickConvertCardViewModel(storage) as T
}

class MainActivity : ComponentActivity() {
    private val storage = Storage(this)
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(storage)
    }
    private val onboardingScreenViewModel: OnboardingScreenViewModel by viewModels()
    private val quickConvertCardViewModel: QuickConvertCardViewModel by viewModels {
        QuickConvertCardViewModelFactory(storage)
    }

    private val tag = "MainActivity"
    private val showOnboardingKey = "showOnboarding"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get saved state of the quick convert card from last time the app was open
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        Log.d(tag, "Loaded shared preferences: ${sharedPref.all}")

        if (sharedPref.getBoolean(showOnboardingKey, false)) {
            onboardingScreenViewModel.openOnboarding()
        }

        // Specify whether the onboarding screen displays in wide (tablet) or narrow form (phones)
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        onboardingScreenViewModel.isWide = manager.phoneType == TelephonyManager.PHONE_TYPE_NONE

        setContent {
            MainView(
                mainViewModel,
                quickConvertCardViewModel,
                onboardingScreenViewModel
            ).Body()
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

/*
@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
 */