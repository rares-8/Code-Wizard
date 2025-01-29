package com.example.codewizard.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.codewizard.R
import com.example.codewizard.ui.BottomBar
import com.example.codewizard.ui.project_edit_screen.EditDestination
import com.example.codewizard.ui.project_edit_screen.EditViewModel
import com.example.codewizard.ui.home.HomeDestination
import com.example.codewizard.ui.home.HomeScreen
import com.example.codewizard.ui.home.CodeStashTopBar
import com.example.codewizard.ui.home.HomeViewModel
import com.example.codewizard.ui.loading_screen.LoadingDestination
import com.example.codewizard.ui.loading_screen.LoadingScreen
import com.example.codewizard.ui.project_add_screen.AddDestination
import com.example.codewizard.ui.project_add_screen.ProjectAddScreen
import com.example.codewizard.ui.project_add_screen.ProjectAddViewModel
import com.example.codewizard.ui.project_request.ProjectRequestDestination
import com.example.codewizard.ui.project_request.ProjectRequestScreen
import com.example.codewizard.ui.project_request.ProjectRequestViewModel
import com.example.codewizard.ui.project_view_screen.ProjectViewModel
import com.example.codewizard.ui.project_view_screen.ProjectViewScreen
import com.example.codewizard.ui.project_view_screen.ViewDestination
import com.example.codewizard.ui.wizard_chat.ChatDestination
import com.example.codewizard.ui.wizard_chat.ChatScreen
import com.example.codewizard.ui.wizard_chat.ChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val projectAddViewModel: ProjectAddViewModel = viewModel(factory = ProjectAddViewModel.Factory)
    val projectEditViewModel: EditViewModel = viewModel(factory = EditViewModel.Factory)
    val projectViewModel: ProjectViewModel = viewModel(factory = ProjectViewModel.Factory)
    val projectRequestViewModel: ProjectRequestViewModel =
        viewModel(factory = ProjectRequestViewModel.Factory)
    val chatViewModel: ChatViewModel = viewModel()

    val geminiCreateProjectPrompt: String =
        stringResource(R.string.gemini_generate_project_prompt)

    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(
            route = HomeDestination.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }) {
            HomeScreen(
                homeUiState = homeViewModel.homeUiState.collectAsState(), onAddProjectClick = {
                    projectAddViewModel.clearUiState()
                    navController.navigate(AddDestination.route)
                },
                onProjectDelete = {
                    homeViewModel.viewModelScope.launch {
                        homeViewModel.deleteProject(it)
                    }
                },
                onProjectEdit = {
                    projectEditViewModel.setProjectState(it)
                    navController.navigate(EditDestination.route)
                },
                onProjectOpen = {
                    projectViewModel.setProjectState(it)
                    navController.navigate(ViewDestination.route)
                },
                topBar = {
                    CodeStashTopBar(
                        titleRes = R.string.app_name,
                        topBarActions = {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = stringResource(R.string.search_description)
                                )
                            }
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.filter_list),
                                    contentDescription = stringResource(R.string.filter_projects)
                                )
                            }
                        }
                    )
                },
                onWizardClicked = {
                    chatViewModel.clearUiState()
                    chatViewModel.setUiState(it)
                    chatViewModel.onFirstMessage()
                    navController.navigate(ChatDestination.route)
                },
                bottomBar = {
                    BottomBar(
                        onIdeasClicked = {
                            projectRequestViewModel.clearUiState()
                            navController.navigate(ProjectRequestDestination.route)
                        },
                        onHomeClicked = { },
                        onSettingsClicked = {}
                    )
                }
            )
        }

        composable(route = AddDestination.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }) {
            ProjectAddScreen(projectUiState = projectAddViewModel.projectUiState, topBar = {
                CodeStashTopBar(titleRes = R.string.save_project, navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(HomeDestination.route)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                })
            },
                onTopicSelect = projectAddViewModel::selectNewTopic,
                onTechnologySelect = projectAddViewModel::selectNewTechnology,
                onProjectNameUpdate = projectAddViewModel::updateProjectName,
                onProjectDescriptionUpdate = projectAddViewModel::updateProjectDescription,
                onProjectInstructionsUpdate = projectAddViewModel::updateProjectInstructions,
                onProjectLevelClick = projectAddViewModel::updateProjectLevel,
                buttonEnabled = projectAddViewModel::isValid,
                onActionButtonClicked = {
                    navController.navigate(HomeDestination.route)
                    projectAddViewModel.viewModelScope.launch {
                        projectAddViewModel.saveProject()
                        projectAddViewModel.clearUiState()
                    }
                })
        }

        composable(route = EditDestination.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }) {
            ProjectAddScreen(
                projectUiState = projectEditViewModel.projectUiState, topBar = {
                    CodeStashTopBar(titleRes = R.string.save_project, navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate(HomeDestination.route)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    })
                },
                onTopicSelect = projectEditViewModel::selectNewTopic,
                onTechnologySelect = projectEditViewModel::selectNewTechnology,
                onProjectNameUpdate = projectEditViewModel::updateProjectName,
                onProjectDescriptionUpdate = projectEditViewModel::updateProjectDescription,
                onProjectInstructionsUpdate = projectEditViewModel::updateProjectInstructions,
                onProjectLevelClick = projectEditViewModel::updateProjectLevel,
                buttonEnabled = projectEditViewModel::isValid,
                onActionButtonClicked = {
                    navController.popBackStack()
                    projectEditViewModel.viewModelScope.launch {
                        projectEditViewModel.saveProject()
                        projectEditViewModel.clearUiState()
                    }
                }
            )
        }

        composable(route = ViewDestination.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }) {
            ProjectViewScreen(projectUiState = projectViewModel.projectUiState, topBar = {
                CodeStashTopBar(titleRes = R.string.app_name, navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                })
            })
        }

        composable(route = ProjectRequestDestination.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }) {
            ProjectRequestScreen(
                projectRequestUiState = projectRequestViewModel.projectRequestUiState,
                topBar = {
                    CodeStashTopBar(titleRes = R.string.app_name)
                },
                onTopicSelect = projectRequestViewModel::selectNewTopic,
                onTechnologySelect = projectRequestViewModel::selectNewTechnology,
                onProjectLevelClick = projectRequestViewModel::updateProjectLevel,
                onActionButtonClicked = {
                    projectRequestViewModel.viewModelScope.launch {
                        navController.navigate(LoadingDestination.route)
                        val generatedProject =
                            projectRequestViewModel.generateProject(geminiCreateProjectPrompt)
                        if (generatedProject != null) {
                            projectAddViewModel.setProjectState(generatedProject)
                            navController.navigate(AddDestination.route)
                        } else {
                            navController.popBackStack()
                        }
                    }
                },
                onInfoUpdate = projectRequestViewModel::updateProjectInformation,
                buttonEnabled = projectRequestViewModel::isValid,
                bottomBar = {
                    BottomBar(
                        onIdeasClicked = { },
                        onHomeClicked = {
                            navController.popBackStack()
                        },
                        onSettingsClicked = {}
                    )
                }
            )
        }

        composable(route = LoadingDestination.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }) {
            LoadingScreen(topBar = { CodeStashTopBar(titleRes = R.string.app_name) })
        }

        composable(route = ChatDestination.route) {
            ChatScreen(
                topBar = {
                    CodeStashTopBar(titleRes = R.string.app_name, navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate(HomeDestination.route)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    })
                },
                modifier = Modifier, viewModel = chatViewModel
            )
        }

    }
}