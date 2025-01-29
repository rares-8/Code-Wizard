package com.example.codewizard.data

import android.content.Context

interface AppContainer {
    val projectsRepository: ProjectsRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val projectsRepository: ProjectsRepository by lazy {
        OfflineProjectsRepository(ProjectDatabase.getDatabase(context).projectDao())
    }
}


