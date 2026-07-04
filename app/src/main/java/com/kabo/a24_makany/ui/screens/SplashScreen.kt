package com.kabo.a24_makany.ui.screens


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kabo.a24_makany.R
import com.kabo.a24_makany.ui.theme.Background
import com.kabo.a24_makany.ui.theme.Primary
import com.kabo.a24_makany.ui.theme.onSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {

    Log.d("SplashTest", "Compose")

    var showText by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (showText) 1f else 0.6f,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    LaunchedEffect(Unit) {
        Log.d("SplashTest", "Launched")
        delay(500)
        showText = true
        delay(1800)
        Log.d("SplashTest", "Finished")
        onFinished()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.icon_outlined),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale)
            )

            Spacer(Modifier.height(24.dp))

            AnimatedVisibility(
                visible = showText,
                enter = fadeIn() + slideInVertically { it / 3 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Makany",
                        style = MaterialTheme.typography.titleLarge,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        "Every place has a memory.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = onSecondary
                    )
                }

            }
        }
    }
}
