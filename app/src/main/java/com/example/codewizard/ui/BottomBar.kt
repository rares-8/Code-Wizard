package com.example.codewizard.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.codewizard.R

@Composable
fun BottomBar(
    onIdeasClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onHomeClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            ),
        tonalElevation = dimensionResource(R.dimen.padding_medium),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        content = {
            IconButton(
                onClick = {}, modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onIdeasClicked() }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.magic),
                        contentDescription = stringResource(R.string.ideas),
                    )
                    Text(text = stringResource(R.string.ideas))
                }
            }

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight(),
                thickness = 2.dp
            )

            IconButton(
                onClick = {}, modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onHomeClicked() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = stringResource(R.string.home),
                    )
                    Text(text = stringResource(R.string.home))
                }
            }

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight(),
                thickness = 2.dp
            )

            IconButton(
                onClick = {}, modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onSettingsClicked() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.settings),
                    )
                    Text(text = stringResource(R.string.settings))
                }
            }
        }
    )
}