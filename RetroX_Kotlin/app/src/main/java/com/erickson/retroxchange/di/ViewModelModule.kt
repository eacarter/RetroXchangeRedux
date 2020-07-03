package com.erickson.retroxchange.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.erickson.retroxchange.MainViewModel
import com.erickson.retroxchange.login.LoginViewModel
import com.erickson.retroxchange.login.signin.SignInViewModel
import com.erickson.retroxchange.login.signup.SignUpViewModel
import com.erickson.retroxchange.ui.dashboard.DashboardItemViewModel
import com.erickson.retroxchange.ui.dashboard.DashboardViewModel
import com.erickson.retroxchange.ui.home.HomeViewModel
import com.erickson.retroxchange.ui.notifications.NotificationsViewModel
import com.erickson.retroxchange.ui.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    protected abstract fun dashboardViewModel(dashboardViewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardItemViewModel::class)
    protected abstract fun dashboardItemViewModel(dashboardItemViewModel: DashboardItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    protected abstract fun homeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel::class)
    protected abstract fun notificationsViewModel(notificationsViewModel: NotificationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    protected abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    protected abstract fun signInViewModel(signInViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    protected abstract fun signUpViewModel(signUpViewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    protected abstract fun profileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    protected abstract fun mainViewModel(mainViewModel: MainViewModel): ViewModel
}