package com.tworoot2.scrollguard.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager


object AppSettings {


    @SuppressLint("QueryPermissionsNeeded")
    fun getNonSystemAppsList(context: Context): Map<String, String> {
        val appInfos = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val appInfoMap = HashMap<String, String>()
        for (appInfo in appInfos) {
            if (appInfo.flags != ApplicationInfo.FLAG_SYSTEM) {
                appInfoMap[appInfo.packageName] =
                    context.packageManager.getApplicationLabel(appInfo).toString()
//                Log.e("AppInfoName", appInfo.packageName)
            }
        }
        return appInfoMap
    }

    fun getUserInstalledApps(context: Context): List<ApplicationInfo> {
        // Retrieve all installed packages
        val pm: PackageManager = context.packageManager
        val packages: List<ApplicationInfo> =
            pm.getInstalledApplications(PackageManager.GET_META_DATA)

        // Filter out system apps
        return packages.filter {
            // Exclude system apps (those with the FLAG_SYSTEM)
            (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        }
    }

    fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return ((pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0)
    }

}