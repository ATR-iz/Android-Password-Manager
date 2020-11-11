package com.atriztech.passwordmanager.service.di

import android.app.Application
import com.atriztech.passwordmanager.service.di.AppComponent.Initializer.init

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
        buildComponentGraph()
    }

    companion object {
        private var appComponent: AppComponent? = null
        private var app: App? = null

        fun component(): AppComponent? {
            return appComponent
        }

        fun buildComponentGraph() {
            appComponent = init(app!!)
        }
    }
}