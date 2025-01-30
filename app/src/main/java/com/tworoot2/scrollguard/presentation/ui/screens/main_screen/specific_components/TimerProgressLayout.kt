package com.tworoot2.scrollguard.presentation.ui.screens.main_screen.specific_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tworoot2.scrollguard.presentation.ui.components.SimpleArcProgressbar
import com.tworoot2.scrollguard.ui.theme.Ui_2
import com.tworoot2.scrollguard.ui.theme.Ui_RED_1

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
