package com.atriztech.passwordmanager.service.di

import android.app.Application
import androidx.room.Room
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    protected fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    protected fun provideRoom(): GroupWithItemDB {
        return Room.databaseBuilder(app.applicationContext, GroupWithItemDB::class.java, "db").build()
    }
}