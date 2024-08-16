package com.tworoot2.scrollguard

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tworoot2.scrollguard.presentation.ui.components.CustomToggleButton
import com.tworoot2.scrollguard.presentation.ui.components.SimpleArcProgressbar
import com.tworoot2.scrollguard.presentation.ui.viewmodels.PermissionsViewModel
import com.tworoot2.scrollguard.settings.AppSettings
import com.tworoot2.scrollguard.settings.MyAppPermissions
import com.tworoot2.scrollguard.settings.YourAccessibilityService
import com.tworoot2.scrollguard.ui.theme.ScrollingBlockTheme
import com.tworoot2.scrollguard.ui.theme.Ui_1
import com.tworoot2.scrollguard.ui.theme.Ui_2
import com.tworoot2.scrollguard.ui.theme.Ui_RED_1
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val permissionsViewModel: PermissionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            ScrollingBlockTheme {
                Column(
                    Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(10.dp)
                        .padding(
                            PaddingValues(top = 50.dp)
                        ),
                ) {

                    var foregroundAppPackageName: String? = null
                    val currentTime = System.currentTimeMillis()
// The `queryEvents` method takes in the `beginTime` and `endTime` to retrieve the usage events.
// In our case, beginTime = currentTime - 10 minutes ( 1000 * 60 * 10 milliseconds )
// and endTime = currentTime
                    val usageStatsManager =
                        getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                    val usageEvents =
                        usageStatsManager.queryEvents(currentTime - (1000 * 60 * 10), currentTime)
                    val usageEvent = UsageEvents.Event()
                    val nonSystemApps = AppSettings.getNonSystemAppsList(this@MainActivity)
                    Log.e("InstalledAppsCount", nonSystemApps.size.toString())
                    while (usageEvents.hasNextEvent()) {
                        usageEvents.getNextEvent(usageEvent)
                        if (nonSystemApps.contains(usageEvent.packageName)) {
                            Log.e("APP", "${usageEvent.packageName} ${usageEvent.timeStamp}")
                        }
                    }


                    MainScreenLayout()


                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        permissionsViewModel.checkPermissions()
    }


    @Preview
    @Composable
    fun MainScreenLayout(modifier: Modifier = Modifier) {
        Column(
            modifier
                .background(Color.Black)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp)
        ) {

            CustomToolBar()

            TimerProgressLayout()

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Permissions ",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))

            AllPermissionLayout(permissionsViewModel)


            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Applications ",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))

            SelectApplicationLayout(
                text = "Instagram",
                image = painterResource(R.drawable.instagram),
                onUpdate = {

                })

            SelectApplicationLayout(
                text = "Youtube",
                image = painterResource(R.drawable.youtube),
                onUpdate = {

                })

        }
    }


    @Composable
    fun AllPermissionLayout(permissionsViewModel: PermissionsViewModel) {


        val isAccessibilityGranted by permissionsViewModel.isAccessibilityGranted.observeAsState(
            initial = false
        )
        val isUsageStatsGranted by permissionsViewModel.isUsageStatsGranted.observeAsState(initial = false)


        val context = LocalContext.current

        if (!isAccessibilityGranted) {
            PermissionsLayout(text = "Accessibility") {
                permissionsViewModel.handleAccessibilityPermission(context)
            }
        }

        if (!isUsageStatsGranted) {
            Log.e("PermissionUsage: ", "Accepted")
            PermissionsLayout(text = "App Usage") {
                permissionsViewModel.handleUsageStatsPermission(context)
            }
        }

        PermissionsLayout(text = "Run in Background") {

        }

        PermissionsLayout(text = "Notification") {

        }


    }

    @Composable
    private fun SelectApplicationLayout(
        modifier: Modifier = Modifier,
        text: String,
        image: Painter,
        selected: Boolean = false,
        onUpdate: (Boolean) -> Unit,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(Ui_1)
                    .fillMaxWidth()
                    .padding(8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = image, contentDescription = "Menu",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(weight = 1f),
                    fontWeight = FontWeight.Normal
                )

                CustomToggleButton(
                    selected = selected, selectedColor = Color.Black,
                    unselectedColor = Color.DarkGray, onUpdate = onUpdate
                )


            }
        }
    }


    @Composable
    private fun PermissionsLayout(
        modifier: Modifier = Modifier,
        text: String, onClick: () -> Unit
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(Ui_1)
                    .fillMaxWidth()
                    .padding(8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(weight = 1f),
                    fontWeight = FontWeight.Normal
                )

                Button(
                    onClick = onClick,
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(text = "Allow", fontSize = 11.sp)
                }


            }
        }
    }


    @Composable
    private fun CustomToolBar(modifier: Modifier = Modifier) {

        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
        ) {
            Row(
                Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(R.drawable.baseline_menu_24),
                    contentDescription = "Menu",
                    colorFilter = ColorFilter.tint(color = Color.Black),
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.app_name, "Scrolling Block"),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }

    }


    @Composable
    fun TimerProgressLayout(modifier: Modifier = Modifier) {


        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.Black)
            ) {
                Box(
                    modifier = Modifier.wrapContentSize()
                ) {
                    SimpleArcProgressbar(progressColor = Ui_RED_1, progress = 10f)

                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Left",
                            color = Color.White,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = "00h 05m",
                            color = Ui_2,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                    }


                }

                Row(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Total time: ",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = "00h 05m",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = Modifier.width(15.dp))

            Column(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(top = 30.dp),

                ) {
                CustomPlatformUsageText()
                CustomPlatformUsageText()
            }

            Spacer(modifier = Modifier.width(5.dp))


        }


    }

    @Composable
    fun CustomPlatformUsageText(modifier: Modifier = Modifier) {
        Row(

        ) {
            Text(
                text = "Shorts: ",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
            )

            Text(
                text = "00h 05m",
                color = Ui_RED_1,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
            )
        }
    }
}