package com.example.codewizard.data

abstract class ProjectData {
    companion object {
        fun getProjectTopics() : List<String> {
            return listOf(
                "Artificial Intelligence",
                "Machine Learning",
                "Networking",
                "Computer Security",
                "Operating Systems",
                "Computer Graphics",
                "Mobile development",
                "DevOps",
                "Backend",
                "Web frontend",
                "Web backend",
                "Web fullstack"
            )
        }

        fun getProjectLevels(): List<String> {
            return listOf(
                "Beginner",
                "Intermediate",
                "Advanced",
            )
        }

        fun getProjectTechnologies(): List<String> {
            return listOf(
                "Java",
                "Python",
                "C/C++",
                "Kotlin",
                "SQL",
                "Jetpack Compose",
                "REST API",
                "JavaScript",
                "Docker",
                "React",
                "Swift",
                "C#",
                "AWS",
                "Angular"
            ).sorted()
        }
    }
}