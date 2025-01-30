package com.tworoot2.scrollguard.utils

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent


class MyAccessibilityService : AccessibilityService() {


    override fun onServiceConnected() {
        super.onServiceConnected()

        Log.d("TAG", "onServiceConnected:")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
        }
    }


    override fun onInterrupt() {
        Log.d("TAG", "onInterrupt:")

    }


}