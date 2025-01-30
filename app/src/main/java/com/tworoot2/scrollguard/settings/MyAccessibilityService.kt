package com.tworoot2.scrollguard.settings

import android.accessibilityservice.AccessibilityGestureEvent
import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.tworoot2.scrollguard.data.local.application_data.SelectedApplicationDao
import com.tworoot2.scrollguard.domain.models.ApplicationModel
import com.tworoot2.scrollguard.presentation.viewmodels.ApplicationDataViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.toList
import javax.inject.Inject
import android.accessibilityservice.GestureDescription
import android.graphics.Path

class MyAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var dao: SelectedApplicationDao

    var listOfApplications = listOf<ApplicationModel>()

    @Inject
    lateinit var viewModel: ApplicationDataViewModel


    override fun onServiceConnected() {
        super.onServiceConnected()


//        listOfApplications = viewModel.applications
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService()
//        }

        Toast.makeText(applicationContext, "Service Connected", Toast.LENGTH_SHORT).show()
        showNotification("Service connected", "Your accessibility service is now active")
        Log.d("TAG", "onServiceConnected:")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
//            Log.d("TAG", "Event detected: ${event.eventType}, package: ${event.packageName}, source: ${event.source}")

//            if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            Log.d("TAG", "Scroll detected in package: ${event.packageName}")

            Handler(Looper.getMainLooper()).postDelayed({
                val rootNode = rootInActiveWindow
                if (rootNode != null) {
                    handleDetectedScrolling(event.packageName.toString(), rootNode)
                }
            }, 1000)

//            if (event.packageName == "com.instagram.android" ||
//                event.packageName == "com.google.android.youtube" ||
//                event.packageName == "app.revanced.android.youtube"
//            ) {
//
//                val rootNode = rootInActiveWindow
//                if (rootNode != null) {
//                    when (event.packageName.toString()) {
//                        "com.instagram.android" -> {
//                            if (isInstagramReels(rootNode)) {
//
//
//                                stopScrollingAndExitPage()
//
//                                showNotification("Be cautious", "Reels scrolling detected")
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Reels Detected",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//
//                                Log.e("InstaReels", "Instagram Reels scrolling detected!")
//                            }
//                        }
//
//                        "com.google.android.youtube" -> {
//                            if (isYouTubeShorts(rootNode)) {
//                                stopScrollingAndExitPage()
//                                showNotification("Be cautious", "Shorts scrolling detected")
//
//                                Log.e("YouTubeShorts", "YouTube Shorts scrolling detected!")
//                            }
//                        }
//
//                        "app.revanced.android.youtube" -> {
//                            if (isYouTubeRevancedShorts(rootNode)) {
//
//
//                                stopScrollingAndExitPage()
//                                showNotification("Be cautious", "Shorts scrolling detected")
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Shorts Detected",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//
//                                Log.e(
//                                    "YouTubeRevancedShorts",
//                                    "YouTube Revanced Shorts scrolling detected!"
//                                )
//                            }
//                        }
//                    }
//                }
//            }

        }
    }

    private fun handleDetectedScrolling(packageName: String, rootNode: AccessibilityNodeInfo) {
        when (packageName) {
            "com.instagram.android" -> {
                if (isInstagramReels(rootNode)) {
                    stopScrollingAndExitPage()
                    showNotification("Be cautious", "Reels scrolling detected")
                    Log.e("InstaReels", "Instagram Reels scrolling detected!")
                }
            }

            "com.google.android.youtube" -> {
                if (isYouTubeShorts(rootNode)) {
                    stopScrollingAndExitPage()
                    showNotification("Be cautious", "Shorts scrolling detected")
                    Log.e("YouTubeShorts", "YouTube Shorts scrolling detected!")
                }
            }
        }
    }


    private fun stopScrollingAndExitPage() {
        // Perform a global action to simulate pressing the back button
        performGlobalAction(GLOBAL_ACTION_BACK)
        Log.d("TAG", "Back button triggered to stop scrolling and exit page")
    }


    private fun isYouTubeShorts(node: AccessibilityNodeInfo?): Boolean {
        if (node == null) return false

        // Introduce a blocking delay before checking UI elements
        Thread.sleep(500) // Delay to allow UI elements to load (adjust timing if needed)

        return detectShortsUI(node)
    }

    private fun detectShortsUI(node: AccessibilityNodeInfo?): Boolean {
        if (node == null) return false

        val requiredIds = listOf(
            "com.google.android.youtube:id/reel_watch_fragment_root",
            "com.google.android.youtube:id/reel_recycler",
            "com.google.android.youtube:id/reel_player_page_container",
            "com.google.android.youtube:id/reel_time_bar"
        )

        for (id in requiredIds) {
            val elements = node.findAccessibilityNodeInfosByViewId(id)
            if (elements.isNotEmpty()) {
                Log.d("ShortsDetection", "Detected Shorts element: $id")
                return true
            }
        }

        // Recursively check child nodes
        for (i in 0 until node.childCount) {
            if (detectShortsUI(node.getChild(i))) {
                return true
            }
        }

        return false
    }


    private fun isInstagramReels2(node: AccessibilityNodeInfo): Boolean {
        // Check for specific YouTube Shorts views
        return node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/root_clips_layout").size > 0
    }

    private fun isInstagramReels(node: AccessibilityNodeInfo): Boolean {
        // Inspect the hierarchy for views specific to Reels
        // Check for specific View IDs or structure related to Reels
        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/root_clips_layout")?.let {
            if (it.isNotEmpty()) {
                return true
            }
        }
        node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_viewer_view_pager")
            ?.let {
                if (it.isNotEmpty()) {
                    return true
                }
            }
        return false
    }

    private fun isVerticalScroll(event: AccessibilityEvent): Boolean {
        // Use event.scrollY to detect vertical movement, if available
        val scrollY = event.scrollY
        return scrollY != 0
    }

    override fun onInterrupt() {
        Log.d("TAG", "onInterrupt:")

    }

    override fun onGesture(gestureEvent: AccessibilityGestureEvent): Boolean {
        return super.onGesture(gestureEvent)
    }

    override fun onMotionEvent(event: MotionEvent) {
        super.onMotionEvent(event)
    }

    private fun isYouTubeRevancedShorts(node: AccessibilityNodeInfo): Boolean {
        // Check for specific YouTube Shorts views
        return node.findAccessibilityNodeInfosByViewId("app.revanced.android.youtube:id/reel_watch_fragment_root").size > 0
    }

    private fun isYouTubeRevancedShorts2(node: AccessibilityNodeInfo): Boolean {
//        Log.e("YouTubeRevancedShorts", "isYouTubeRevancedShorts:")
        // Check for the presence of views specific to YouTube Shorts
        node.findAccessibilityNodeInfosByViewId("app.revanced.android.youtube:id/reel_watch_fragment_root")
            ?.let {
                if (it.isNotEmpty()) {
                    return true
                }
            }
        node.findAccessibilityNodeInfosByViewId("app.revanced.android.youtube:id/reel_recycler")
            ?.let {
                if (it.isNotEmpty()) {
                    return true
                }
            }
        return false
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId = "SCROLL_GUARD_CHANNEL"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Scroll Guard Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(applicationContext, notificationChannelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        notificationManager.notify(1, notification)
    }

}