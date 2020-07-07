package com.erickson.retroxchange.di

import com.erickson.retroxchange.MainActivity
import com.erickson.retroxchange.login.LoginActivity
import com.erickson.retroxchange.ui.dashboard.DashboardItemActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeDashboardItemActivity(): DashboardItemActivity
}