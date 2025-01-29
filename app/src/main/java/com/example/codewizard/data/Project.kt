package com.example.codewizard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val projectName: String = "",
    val projectDifficulty: String = "",
    val topics: List<String> = listOf(),
    val technologies: List<String> = listOf(),
    val description: String = "",
    val instructions: String = "",
)