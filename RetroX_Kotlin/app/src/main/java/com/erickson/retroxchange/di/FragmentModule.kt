package com.erickson.retroxchange.di

import com.erickson.retroxchange.login.signin.SignInFragment
import com.erickson.retroxchange.login.signup.SignUpFragment
import com.erickson.retroxchange.ui.dashboard.DashboardFragment
import com.erickson.retroxchange.ui.home.HomeFragment
import com.erickson.retroxchange.ui.notifications.NotificationsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    /*
     * We define the name of the Fragment we are going
     * to inject the ViewModelFactory into. i.e. in our case
     */
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeDasdboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun contributeNotificationsFragment(): NotificationsFragment

    @ContributesAndroidInjector
    abstract fun contributeSignInFragment(): SignInFragment

    @ContributesAndroidInjector
    abstract fun contributeSignUpFragment(): SignUpFragment
}