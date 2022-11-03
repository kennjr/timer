package com.ramanie.timer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Timer(
    totalTime: Long, handleColor: Color, inactiveBarColor: Color, activeBarColor: Color,
    modifier: Modifier = Modifier, initialValue: Float = 1f, strokeWidth: Dp = 5.dp
) {

    // we use this
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    // this is for the %age time not covered(time left)
    var value by remember {
        mutableStateOf(initialValue)
    }

    // the current time we're at i.e how much time is left since the timer started running
    var currentTime by remember {
        mutableStateOf(totalTime)
    }

    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    // whenever either one of the keys changes the code in the block will be run again,
    // the code below will run once(when the composable is composed) then every subsequent execution will be whenever any of the keys changes
    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning, block = {
        // if the timer is running and the current time is > than 0L,  we'd like to run the code in the if-block
        if (currentTime > 0L && isTimerRunning){
            delay(100L)
            currentTime -= 100L
            value = currentTime.div(totalTime.toFloat())
        }
    })

    Box(contentAlignment = Alignment.Center, modifier = modifier.onSizeChanged { size = it }) {
        // we're gon draw 2 arcs : 1 with the inactiveBarColor and the other with the activeBarColor that'll overlay the 1st one
        Canvas(modifier = modifier, onDraw = {
            drawArc(
                color = inactiveBarColor,
                // startAngle - this is where the timer's visible progressbar will end(right-left)
                startAngle = -215f,
                // sweepAngle - determines how far (to the right) the angle should be from the startAngle
                sweepAngle = 250f,
                // useCenter - will prevent the arc from being connected to the center(forming a pie)
                useCenter = false,
                size = Size(width = size.width.toFloat(), height = size.height.toFloat()),
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = activeBarColor,
                // startAngle - this is where the timer's visible progressbar will end(right-left)
                startAngle = -215f,
                // sweepAngle - determines how far (to the right) the angle should be from the startAngle
                // we're multiplying 250 by the value so that we can get the angle, since the angle
                // will reduce as time goes by and the var. value rep.s the percentage of time left on the clock
                sweepAngle = 250f.times(value),
                // useCenter - will prevent the arc from being connected to the center(forming a pie)
                useCenter = false,
                size = Size(width = size.width.toFloat(), height = size.height.toFloat()),
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            // the var below is for the center of our canvas
            val center = Offset(size.width.div(2f), size.height.div(2f))
            // the line below will give us the angle btwn the knob and the center of the canvas
            // the 145f value is just a random value that happens to fit perfectly
            // we're multiplying the response by PI.div(180f) so that we can get the value in radians
            val beta = (250f.times(value).plus(145f)).times(PI.div(180f)).toFloat()
            val radius = (size.width).div(2f)
            // should be the a in the a-b-c we use in triangles
            val a = cos(beta).times(radius)
            val b = sin(beta).times(radius)

            drawPoints(
                listOf(
                    // we're drawing the knob, the 1st list item's x value will set the position of the knob on the x-axis(from the origin)
                    // and the 2nd(y value) will set it's position on the y-axis(from the origin). We'll use
                    // the 2(values in the list) to set the point where the knob should be drawn
                    Offset(x = center.x.plus(a), y = center.y.plus(b))
                ),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = strokeWidth.times(3f).toPx(),
                cap = StrokeCap.Round
            )
        })
        Text(
            text = (currentTime.div(1000L)).toString(),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Button(
            onClick = {
                // the user wants to pause the timer
                if (isTimerRunning && currentTime > 0L) {
                    isTimerRunning = false
                }
                // the user wants to resume the timer
                else if (!isTimerRunning && currentTime > 0L) {
                    isTimerRunning = true
                } else {
                    currentTime = totalTime
                    isTimerRunning = true
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(if (!isTimerRunning || currentTime <= 0L) Color.Green else Color.Red),
        ) {
            Text(text = if (isTimerRunning && currentTime > 0L) "PAUSE" else if (!isTimerRunning && (currentTime in 1 until totalTime)) "CONTINUE" else "START")
        }
    }
}