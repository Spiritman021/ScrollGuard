package com.tworoot2.scrollguard.settings

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity


object MyAppPermissions {


    fun checkUsageStatsPermission(context: Context): Boolean {
        val appOpsManager =
            context.getSystemService(AppCompatActivity.APP_OPS_SERVICE) as AppOpsManager
        // `AppOpsManager.checkOpNoThrow` is deprecated from Android Q
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                "android:get_usage_stats",
                Process.myUid(), context.packageName
            )
        } else {
            appOpsManager.checkOpNoThrow(
                "android:get_usage_stats",
                Process.myUid(), context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun checkAccessibilityPermission(context: Context): Boolean {
        var accessEnabled = 0
        try {
            accessEnabled = Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return accessEnabled != 0
    }
    fun isAccessibilityServiceEnabled(
        context: Context,
        service: Class<out AccessibilityService?>
    ): Boolean {
        val am: AccessibilityManager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices: List<AccessibilityServiceInfo> =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

        for (enabledService in enabledServices) {
            val enabledServiceInfo: ServiceInfo = enabledService.resolveInfo.serviceInfo
            if (enabledServiceInfo.packageName.equals(context.packageName) && enabledServiceInfo.name.equals(
                    service.name
                )
            ) return true
        }

        return false
    }
    fun requestAccessibilityPermission(context: Context): Boolean {
        val intent: Intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        // request permission via start activity for result
        context.startActivity(intent)
        return true
    }
}