package com.erickson.retroxchange.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.erickson.retroxchange.MainActivity
import com.erickson.retroxchange.R
import com.erickson.retroxchange.login.signin.SignInFragment
import com.erickson.retroxchange.login.signup.SignUpViewModel
import com.erickson.retroxchange.manager.UserManager
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    @Inject
    lateinit var userManager: UserManager

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AndroidInjection.inject(this)
        FirebaseApp.initializeApp(this)

        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.login_content_view, SignInFragment(), "SignIn")
            .commit()

    }
}
