package com.example.codewizard.ui.project_request

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.codewizard.R
import com.example.codewizard.data.ProjectData
import com.example.codewizard.data.ProjectRequest
import com.example.codewizard.navigation.NavigationDestination

object ProjectRequestDestination : NavigationDestination {
    override val route = "request"
    override val titleRes = R.string.app_name
}

@Composable
fun ProjectRequestScreen(
    projectRequestUiState: ProjectRequestUiState,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    onTopicSelect: (String) -> Unit,
    onTechnologySelect: (String) -> Unit,
    onProjectLevelClick: (String) -> Unit,
    onActionButtonClicked: () -> Unit,
    onInfoUpdate: (String) -> Unit,
    buttonEnabled: (ProjectRequest) -> Boolean,
    modifier: Modifier = Modifier,
) {
    var topicsExpanded by remember { mutableStateOf(false) }
    var technologiesExpanded by remember { mutableStateOf(false) }
    var levelsExpanded by remember { mutableStateOf(true) }
    var infoExpanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        topBar = topBar,
        bottomBar = bottomBar,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))

            ListSection(
                sectionTitle = stringResource(R.string.topics),
                content = ProjectData.getProjectTopics(),
                selectedContent = projectRequestUiState.project.topics,
                isExpanded = topicsExpanded,
                onExpandClicked = { topicsExpanded = !topicsExpanded },
                onSelect = onTopicSelect,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )

            ProjectLevelChoice(
                sectionTitle = stringResource(R.string.choose_project_level),
                onProjectLevelClick = onProjectLevelClick,
                isExpanded = levelsExpanded,
                selectedLevel = projectRequestUiState.project.projectDifficulty,
                onExpandClicked = { levelsExpanded = !levelsExpanded },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )

            ListSection(
                sectionTitle = stringResource(R.string.technologies),
                content = ProjectData.getProjectTechnologies(),
                selectedContent = projectRequestUiState.project.technologies,
                isExpanded = technologiesExpanded,
                onExpandClicked = { technologiesExpanded = !technologiesExpanded },
                onSelect = onTechnologySelect,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )

            TextSection(
                sectionTitle = stringResource(R.string.project_info),
                textPlaceholder = stringResource(R.string.info_placeholder),
                isExpanded = infoExpanded,
                onExpandClicked = { infoExpanded = !infoExpanded },
                textFieldValue = projectRequestUiState.project.additionalInformation,
                onValueChange = onInfoUpdate,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )

            Spacer(Modifier.padding(dimensionResource(R.dimen.padding_medium)))

            Button(
                enabled = buttonEnabled(projectRequestUiState.project),
                onClick = onActionButtonClicked,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .scale(if (buttonEnabled(projectRequestUiState.project)) 1f else 0.95f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ) {
                Text(text = stringResource(R.string.generate_project))
            }

        }
    }
}

@Composable
fun TextSection(
    sectionTitle: String,
    textPlaceholder: String,
    isExpanded: Boolean,
    onExpandClicked: () -> Unit,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
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
            TextField(
                value = textFieldValue,
                onValueChange = { onValueChange(it) },
                placeholder = { Text(text = textPlaceholder) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 50,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListSection(
    sectionTitle: String,
    content: List<String>,
    selectedContent: List<String>,
    isExpanded: Boolean,
    onExpandClicked: () -> Unit,
    onSelect: (String) -> Unit,
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
            enter = fadeIn(animationSpec = tween(durationMillis = 300)) + slideInVertically(
                initialOffsetY = { it }),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)) + slideOutVertically(
                targetOffsetY = { it }),
            modifier = Modifier.animateContentSize()
        ) {
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
                            containerColor = if (selectedContent.contains(item))
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .clickable { onSelect(item) }
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

}

@Composable
fun ProjectLevelChoice(
    levels: List<String> = ProjectData.getProjectLevels(),
    onProjectLevelClick: (String) -> Unit,
    isExpanded: Boolean,
    selectedLevel: String,
    onExpandClicked: () -> Unit,
    modifier: Modifier = Modifier,
    sectionTitle: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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

        AnimatedVisibility(visible = isExpanded, modifier = Modifier.animateContentSize()) {
            Column()
            {
                for (level in levels) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = level == selectedLevel,
                            onClick = {
                                onProjectLevelClick(level)
                            })
                        Text(text = level)
                    }
                }
            }
        }
    }
}