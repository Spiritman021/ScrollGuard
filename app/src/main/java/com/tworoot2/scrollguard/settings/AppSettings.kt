package com.tworoot2.scrollguard.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log

object AppSettings {


    @SuppressLint("QueryPermissionsNeeded")
    fun getNonSystemAppsList(context: Context): Map<String, String> {
        val appInfos = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val appInfoMap = HashMap<String, String>()
        for (appInfo in appInfos) {
            if (appInfo.flags != ApplicationInfo.FLAG_SYSTEM) {
                appInfoMap[appInfo.packageName] =
                    context.packageManager.getApplicationLabel(appInfo).toString()
                Log.e("AppInfoName", appInfo.packageName)
            }
        }
        return appInfoMap
    }


}