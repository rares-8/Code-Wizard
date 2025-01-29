package com.example.codewizard.navigation

interface NavigationDestination {
    /**
     * Unique route to a destination
     */
    val route: String

    /**
     * Title that shows up in the top bar
     */
    val titleRes: Int
}