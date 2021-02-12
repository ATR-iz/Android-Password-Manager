package com.atriztech.passwordmanager.service.di

import android.app.Application
import android.os.Environment
import androidx.room.Room
import com.atriztech.crypto_api.CryptoApi
import com.atriztech.crypto_impl.CryptoImpl
import com.atriztech.file_manager_api.DirApi
import com.atriztech.file_manager_impl.DirImpl
import com.atriztech.image_api.ImageApi
import com.atriztech.image_impl.ImageImpl
import com.atriztech.passwordmanager.model.Backup
import com.atriztech.passwordmanager.model.DirBackup
import com.atriztech.passwordmanager.model.DirImage
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

    @Provides
    @Singleton
    protected fun provideCrypto(): CryptoApi {
        return CryptoImpl()
    }

    @Provides
    @Singleton
    protected fun provideDir(): DirImage {
        return DirImage(app.filesDir.absolutePath)
    }

    @Provides
    @Singleton
    protected fun provideImage(): ImageApi {
        return ImageImpl()
    }

    @Provides
    @Singleton
    protected fun provideBackup(): Backup {
        return Backup(DirBackup("${Environment.getExternalStorageDirectory().path}/PasswordManager"))
    }
}