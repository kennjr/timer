package com.ramanie.timer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun Timer(totalTime: Long, handleColor: Color, inactiveBarColor: Color, activeBarColor: Color,
          initialValue: Float = 0f, strokeWidth: Dp = 5.dp, modifier: Modifier = Modifier){

    // we use this
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    // this is for the %age value covered(time left)
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
    
    Box(contentAlignment = Alignment.Center, modifier = modifier.onSizeChanged { size = it }){
        // we're gon draw 2 arcs : 1 with the inactiveBarColor and the other with the activeBarColor that'll overlay the 1st one
        Canvas(modifier = modifier, onDraw = {
//            drawArc(color = inactiveBarColor)
        })
    }
}