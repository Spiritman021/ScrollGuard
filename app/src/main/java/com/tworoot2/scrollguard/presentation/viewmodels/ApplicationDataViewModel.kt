package com.tworoot2.scrollguard.presentation.viewmodels

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tworoot2.scrollguard.data.repositories.SelectedApplicationRepository
import com.tworoot2.scrollguard.domain.models.ApplicationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationDataViewModel @Inject constructor(val repository: SelectedApplicationRepository) :
    ViewModel() {

    private var _applications by mutableStateOf<List<ApplicationModel>>(emptyList())
    val applications: List<ApplicationModel> get() = _applications

    private var _installedApps by mutableStateOf<List<ApplicationInfo>>(emptyList())
    val installedApps: List<ApplicationInfo> get() = _installedApps

    fun getInstalledApps(packageManager: PackageManager) {
        viewModelScope.launch(Dispatchers.IO) {
            _installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        }
    }

    // Print the app names and package names
//    println("Size: ${installedApps.size}")
//    for (app in installedApps) {
//        Log.d("UserApp", "App Name: ${app.loadLabel(packageManager)}, Package Name: ${app.packageName}")
//    }


    private var _selectedApplication by mutableStateOf<ApplicationModel?>(null)
    val selectedApplication: ApplicationModel? get() = _selectedApplication


    fun insertApplication(applicationModel: ApplicationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addApplication(applicationModel)
        }
    }

    fun updateApplication(applicationModel: ApplicationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateApplication(applicationModel)
        }
    }

    fun getAllApplications() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllApplications().collect { applicationList ->
                _applications = applicationList
            }
        }
    }

    fun getApplicationById(applicationModel: ApplicationModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedApplication = repository.getApplicationById(applicationModel)
        }
    }

}