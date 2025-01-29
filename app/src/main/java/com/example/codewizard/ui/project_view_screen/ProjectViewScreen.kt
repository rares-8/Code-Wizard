package com.example.codewizard.ui.project_view_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.codewizard.R
import com.example.codewizard.navigation.NavigationDestination
import com.example.codewizard.ui.project_add_screen.ProjectUiState
import dev.jeziellago.compose.markdowntext.MarkdownText

object ViewDestination : NavigationDestination {
    override val route = "view"
    override val titleRes = R.string.view_destination
}

@Composable
fun ProjectViewScreen(
    projectUiState: ProjectUiState,
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var descriptionExpanded by remember { mutableStateOf(false) }
    var instructionsExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {

            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))

            Text(
                text = projectUiState.project.projectName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )

            ListSection(
                sectionTitle = stringResource(R.string.topics),
                content = projectUiState.project.topics,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )

            ProjectLevelChoice(
                sectionTitle = stringResource(R.string.project_difficulty),
                selectedLevel = projectUiState.project.projectDifficulty,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )

            ListSection(
                sectionTitle = stringResource(R.string.technologies),
                content = projectUiState.project.technologies,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )

            TextSection(
                sectionTitle = stringResource(R.string.project_description),
                isExpanded = descriptionExpanded,
                onExpandClicked = { descriptionExpanded = !descriptionExpanded },
                textValue = projectUiState.project.description,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small)),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            TextSection(
                sectionTitle = stringResource(R.string.project_instructions),
                isExpanded = instructionsExpanded,
                onExpandClicked = { instructionsExpanded = !instructionsExpanded },
                textValue = projectUiState.project.instructions,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun TextSection(
    sectionTitle: String,
    isExpanded: Boolean,
    onExpandClicked: () -> Unit,
    textValue: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = sectionTitle,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.padding_small)
                )
            )
            IconButton(
                onClick = onExpandClicked,
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.expand_section)
                )
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)),
            modifier = Modifier.animateContentSize()
        ) {
            MarkdownText(
                markdown = textValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        dimensionResource(R.dimen.padding_small)
                    ),
                maxLines = 50,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListSection(
    sectionTitle: String,
    content: List<String>,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = sectionTitle,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.padding_medium)
                )
            )
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            modifier = modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            content.forEach { item ->
                Card(
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                    colors = CardDefaults.cardColors(
                        containerColor = if (content.contains(item))
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    )
                }
            }
        }
    }

}

@Composable
fun ProjectLevelChoice(
    selectedLevel: String,
    modifier: Modifier = Modifier,
    sectionTitle: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = sectionTitle,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(
                        dimensionResource(R.dimen.padding_small)

                    )
                    .alignByBaseline()
            )

            Text(
                text = selectedLevel,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alignByBaseline()
            )
        }
    }
}