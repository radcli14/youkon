package com.dcengineer.youkon

import AccountService
import Storage
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import firebase.login.LoginViewModel
import firebase.service.LogService
import firebase.service.StorageService
import firebase.settings.SettingsViewModel
import firebase.sign_up.SignUpViewModel
import viewmodel.MainViewModel
import viewmodel.QuickConvertCardViewModel

/// The `MainViewModelFactory` exists so that the storage class can be provided on creation
class MainViewModelFactory(
    private val accountService: AccountService,
    private val localStorage: Storage,
    private val cloudStorage: StorageService
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(accountService, localStorage, cloudStorage) as T
}

/// The `QuickConvertCardViewModelFactory` exists so that the storage class can be provided on creation
class QuickConvertCardViewModelFactory(
    private val localStorage: Storage
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = QuickConvertCardViewModel(localStorage) as T
}

/// The `SettingsViewModelFactory` exists so that the log, accoont, and storage classes can be provided on creation
class SettingsViewModelFactory(
    private val logService: LogService,
    private val accountService: AccountService,
    private val cloudStorage: StorageService
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SettingsViewModel(logService, accountService, cloudStorage) as T
}

/// The `LoginViewModelFactory` exists so that the log and account classes can be provided on creation
class LoginViewModelFactory(
    private val accountService: AccountService,
    private val logService: LogService
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = LoginViewModel(accountService, logService) as T
}

/// The `SignUpViewModelFactory` exists so that the log and account classes can be provided on creation
class SignUpViewModelFactory(
    private val accountService: AccountService,
    private val logService: LogService
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = SignUpViewModel(accountService, logService) as T
}