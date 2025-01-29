package com.example.codewizard.ui.loading_screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.codewizard.R
import com.example.codewizard.navigation.NavigationDestination
import com.example.codewizard.ui.theme.CodeWizardTheme

object LoadingDestination : NavigationDestination {
    override val route = "loading"
    override val titleRes = R.string.app_name
}

@Composable
fun LoadingScreen(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) { innerPadding ->

        val infiniteTransition = rememberInfiniteTransition(label = "")
        val angle by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = LinearEasing)
            ), label = ""
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.hexford1),
                contentDescription = "wizard",
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = angle
                    }
            )
        }
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    CodeWizardTheme {
        LoadingScreen(topBar = {})
    }
}