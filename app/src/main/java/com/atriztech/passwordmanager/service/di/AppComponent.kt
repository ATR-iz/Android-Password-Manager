package com.atriztech.passwordmanager.service.di

import com.atriztech.passwordmanager.view.MainActivity
import com.atriztech.passwordmanager.view.fragmentgroup.GroupFragment
import com.atriztech.passwordmanager.view.fragmentitem.ItemFragment
import com.atriztech.passwordmanager.view.fragmentlistgroups.ListGroupsFragment
import com.atriztech.passwordmanager.view.fragmentlistitems.ListItemsFragment
import com.atriztech.passwordmanager.view.fragmentlogin.LoginFragment
import com.atriztech.passwordmanager.view.fragmentmain.MainFragment
import com.atriztech.passwordmanager.view.fragmentnewpassword.NewPasswordFragment
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : AndroidInjector<App> {
    fun inject(mainActivity: MainActivity?)
    fun inject(mainFragment: MainFragment?)
    fun inject(loginFragment: LoginFragment?)
    fun inject(newPasswordFragment: NewPasswordFragment?)
    fun inject(listGroupsFragment: ListGroupsFragment?)
    fun inject(listItemsFragment: ListItemsFragment?)
    fun inject(groupsFragment: GroupFragment?)
    fun inject(itemFragment: ItemFragment?)

    object Initializer {
        fun init(app: App): AppComponent {
            return DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
        }
    }
}