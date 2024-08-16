package com.tworoot2.scrollguard.settings

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class YourAccessibilityService : AccessibilityService() {
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("TAG", "onServiceConnected:")
    }

//    override fun onAccessibilityEvent(e: AccessibilityEvent?) {
//        Log.d("TAG", "onAccessibilityEvent: $e")
//    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
//        Log.d("TAG", "onAccessibilityEvent: $event")

        event?.let {
//          this.f  AccessibilityServiceInfo.FEEDBACK_SPOKEN
            if (event.packageName == "com.instagram.android" || event.packageName == "com.google.android.youtube"
                || event.packageName == "app.revanced.android.youtube"
            ) {
//                blockScrolling(event)
                Log.e("InstaShortsScrolling", event.packageName.toString())
            }
        }
    }

    override fun onInterrupt() {
        Log.d("TAG", "onInterrupt:")

    }
}