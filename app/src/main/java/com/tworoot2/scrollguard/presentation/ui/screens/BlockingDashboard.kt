package com.tworoot2.scrollguard.presentation.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tworoot2.scrollguard.presentation.ui.screens.ui.theme.ScrollingBlockTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

class BlockingDashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScrollingBlockTheme {
                PrizeProgressScreen()

            }
        }
    }
}

@Preview
@Composable
fun CircularProgress(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.then(Modifier.size(32.dp)),
        strokeCap = StrokeCap.Round,

        )
}

@Preview
@Composable
fun PrizeProgressScreen() {

    var score by remember {
        mutableStateOf(0f)
    }

    var scoreInput by remember {
        mutableStateOf("0")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6b4cba)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(vertical = 16.dp),
            text = "Progress for every level up: $maxProgressPerLevel",
            color = Color.LightGray,
            fontSize = 16.sp
        )

        ArcProgressbar(
            score = score,
        )

        Button(onClick = {
            score = scoreInput.toFloat()
        }) {
            Text("Add Score")
        }

        TextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = scoreInput,
            onValueChange = {
                scoreInput = it
            }
        )
    }
}


val maxProgressPerLevel = 200 // you can change this to any max value that you want
val progressLimit = 300f

fun calculate(
    score: Float,
    level: Int,
): Float {
    return (abs(score - (maxProgressPerLevel * level)) / maxProgressPerLevel) * progressLimit
}

@Composable
fun ArcProgressbar(
    modifier: Modifier = Modifier,
    score: Float
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

        if (score > 0f) {

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
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            PointsProgress(
                progress = {
                    progressAnimate.value // deferred read of progress
                }
            )

            CollectorLevel(
                modifier = Modifier.align(Alignment.Center),
                level = {
                    level + 1 // deferred read of level
                }
            )
        }

        CollectorScore(
            modifier = Modifier.padding(top = 16.dp),
            score = {
                scoreAnimate.value // deferred read of score
            }
        )
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
    progress: () -> Float
) {

    val start = 120f
    val end = 300f
    val thickness = 8.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.45f)
            .padding(10.dp)
            .aspectRatio(1f)
            .align(Alignment.Center),
        onDraw = {
            // Background Arc
            drawArc(
                color = Color.LightGray,
                startAngle = start,
                sweepAngle = end,
                useCenter = false,
                style = Stroke(thickness.toPx(), cap = StrokeCap.Square),
                size = Size(size.width, size.height)
            )

            // Foreground Arc
            drawArc(
                color = Color(0xFF3db39f),
                startAngle = start,
                sweepAngle = progress(),
                useCenter = false,
                style = Stroke(thickness.toPx(), cap = StrokeCap.Square),
                size = Size(size.width, size.height)
            )
        }
    )
}