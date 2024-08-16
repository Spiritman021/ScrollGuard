package com.tworoot2.scrollguard.presentation.ui.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tworoot2.scrollguard.settings.MyAppPermissions
import com.tworoot2.scrollguard.settings.YourAccessibilityService

class PermissionsViewModel(application: Application) : AndroidViewModel(application) {

    private val _isAccessibilityGranted = MutableLiveData<Boolean>()
    val isAccessibilityGranted: LiveData<Boolean> get() = _isAccessibilityGranted

    private val _isUsageStatsGranted = MutableLiveData<Boolean>()
    val isUsageStatsGranted: LiveData<Boolean> get() = _isUsageStatsGranted

    fun checkPermissions() {
        _isAccessibilityGranted.value = MyAppPermissions.isAccessibilityServiceEnabled(
            getApplication(),
            YourAccessibilityService::class.java
        )
        _isUsageStatsGranted.value = MyAppPermissions.checkUsageStatsPermission(getApplication())
    }

    fun handleAccessibilityPermission(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }

    fun handleUsageStatsPermission(context: Context) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        context.startActivity(intent)
    }
}
