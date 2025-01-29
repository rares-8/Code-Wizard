package com.example.codewizard

import android.app.Application
import com.example.codewizard.data.AppContainer
import com.example.codewizard.data.AppDataContainer

class CodeWizardApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}