package com.example.codewizard.data

data class ProjectRequest(
    val topics: List<String> = listOf(),
    val projectDifficulty: String = "",
    val technologies: List<String> = listOf(),
    val additionalInformation: String = "",
)