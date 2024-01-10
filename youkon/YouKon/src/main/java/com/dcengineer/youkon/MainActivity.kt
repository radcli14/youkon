package com.dcengineer.youkon

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dcengineer.youkon.viewmodels.MainViewModel
import com.dcengineer.youkon.viewmodels.OnboardingScreenViewModel
import com.dcengineer.youkon.viewmodels.ProjectsCardViewModel
import com.dcengineer.youkon.viewmodels.QuickConvertCardViewModel
import com.dcengineer.youkon.views.MainView


class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val quickConvertCardViewModel: QuickConvertCardViewModel by viewModels()
    private lateinit var projectsCardViewModel: ProjectsCardViewModel
    private val onboardingScreenViewModel: OnboardingScreenViewModel by viewModels()

    private val tag = "MainViewModel"
    private val showOnboardingKey = "showOnboarding"
    private val quickConvertUnitKey = "quickConvertUnit"
    private val quickConvertTargetKey = "quickConvertTarget"
    private val quickConvertValueKey = "quickConvertValue"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get saved state of the quick convert card from last time the app was open
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        Log.d(tag, "Loaded shared preferences: ${sharedPref.all}")
        if (sharedPref.getBoolean(showOnboardingKey, true)) {
            onboardingScreenViewModel.openOnboarding()
        }
        quickConvertCardViewModel.updateUnit(
            YkUnit.valueOf(sharedPref.getString(quickConvertUnitKey, "METERS") ?: "METERS")
        )
        quickConvertCardViewModel.updateTargetUnit(
            YkUnit.valueOf(sharedPref.getString(quickConvertTargetKey, "FEET") ?: "FEET")
        )
        quickConvertCardViewModel.updateValue(
            sharedPref.getString(quickConvertValueKey, "2.26")?.toDouble() ?: 2.26
        )

        // The Projects card are dependent on user data that is contained in the `mainViewModel`
        projectsCardViewModel = ProjectsCardViewModel(mainViewModel.user)

        // Specify whether the onboarding screen displays in wide (tablet) or narrow form (phones)
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        onboardingScreenViewModel.isWide = manager.phoneType == TelephonyManager.PHONE_TYPE_NONE

        // Any time the user closes an editing dialog, save the user data to a json file
        mainViewModel.isEditingProject.observe(this) { isEditing ->
            if (!isEditing) {
                mainViewModel.saveUserToJson()
            }
        }

        setContent {
            MainView(
                mainViewModel,
                quickConvertCardViewModel,
                projectsCardViewModel,
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
            putString(quickConvertUnitKey, quickConvertCardViewModel.unit.toString())
            putString(quickConvertTargetKey, quickConvertCardViewModel.targetUnit.toString())
            putString(quickConvertValueKey, quickConvertCardViewModel.value.toString())
            apply()
            commit()
        }
        Log.d(tag, "Saved shared preferences: ${sharedPref.all}")
        Log.d(tag, "Destroyed the main activity")
    }
}
