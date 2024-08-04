package com.tworoot2.scrollguard.presentation.ui.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tworoot2.scrollguard.presentation.ui.screens.calculate
import kotlinx.coroutines.launch

val maxProgressPerLevel = 200 // you can change this to any max value that you want
val progressLimit = 300f


@Composable
fun SimpleArcProgressbar(
    progressColor: Color,
    modifier: Modifier = Modifier,
    progress: Float // progress value between 0 to 100
) {
    val targetAnimatedValue = (progress / 100) * 300f // convert 0-100 range to 0-300 degrees
    val progressAnimate = remember { Animatable(targetAnimatedValue) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(progress) {
        coroutineScope.launch {
            progressAnimate.animateTo(
                targetValue = targetAnimatedValue,
                animationSpec = tween(durationMillis = 1000)
            )
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        PointsProgress(progressColor = progressColor, progress = { progressAnimate.value })
    }
}


@Composable
fun ArcProgressbar(
    progressColor: Color,
    modifier: Modifier = Modifier,
    score: Float,
    maxProgressPerLevel: Int = 100,
    progressLimit: Float = 100f
) {

    Log.e("ArcProgressBar", "Recomposed")

    var level by remember {
        mutableStateOf(score.toInt() / maxProgressPerLevel)
    }

    var targetAnimatedValue = calculate(score, level)
    val progressAnimate = remember { Animatable(targetAnimatedValue) }
    val scoreAnimate = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(level, score) {

        if (score >= 0f) {

            // animate progress
            coroutineScope.launch {
                progressAnimate.animateTo(
                    targetValue = targetAnimatedValue,
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                ) {
                    if (value >= progressLimit) {

                        coroutineScope.launch {
                            level++
                            progressAnimate.snapTo(0f)
                        }
                    }
                }
            }

            // animate score
            coroutineScope.launch {

                if (scoreAnimate.value > score) {
                    scoreAnimate.snapTo(0f)
                }

                scoreAnimate.animateTo(
                    targetValue = score,
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                )
            }
        }
    }

    Column(
        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            PointsProgress(
                progressColor = progressColor,
                progress = {
                    progressAnimate.value // deferred read of progress
                }
            )

//            CollectorLevel(
//                modifier = Modifier.align(Alignment.Center),
//                level = {
//                    level + 1 // deferred read of level
//                }
//            )
        }

//        CollectorScore(
//            modifier = Modifier.padding(top = 16.dp),
//            score = {
//                scoreAnimate.value // deferred read of score
//            }
//        )
    }
}

@Composable
fun CollectorScore(
    modifier: Modifier = Modifier,
    score: () -> Float
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Collector Score",
            color = Color.White,
            fontSize = 16.sp
        )

        Text(
            text = "${score().toInt()} PTS",
            color = Color.White,
            fontSize = 40.sp
        )
    }
}

@Composable
fun CollectorLevel(
    modifier: Modifier = Modifier,
    level: () -> Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(top = 16.dp),
            text = level().toString(),
            color = Color.White,
            fontSize = 82.sp
        )

        Text(
            text = "LEVEL",
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@Composable
fun BoxScope.PointsProgress(
    progressColor: Color,
    progress: () -> Float
) {

    val start = 120f
    val end = 300f
    val thickness = 12.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.45f)
            .padding(10.dp)
            .aspectRatio(1f)
            .align(Alignment.Center),
        onDraw = {
            // Background Arc
            val gap = 25f / 2
            val radius = size.width / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            drawArc(

                color = Color.LightGray,
                startAngle = start,
                sweepAngle = end,
                useCenter = false,

                style = Stroke(thickness.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )

            // Foreground Arc
            drawArc(
                color = progressColor,
                startAngle = start,
                sweepAngle = progress(),
                useCenter = false,
                style = Stroke(thickness.toPx(), cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }
    )
}