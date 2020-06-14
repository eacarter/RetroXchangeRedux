package com.erickson.retroxchange.login.signup

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.erickson.retroxchange.MainActivity

import com.erickson.retroxchange.R
import com.erickson.retroxchange.databinding.SignInFragmentBinding
import com.erickson.retroxchange.databinding.SignUpFragmentBinding
import com.erickson.retroxchange.login.signin.SignInFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: SignUpFragmentBinding

    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignUpViewModel::class.java)


        binding.loginSubmit.setOnClickListener {
            if(!binding.loginEmail.text.isEmpty() || !binding.loginPass.text.isEmpty()) {
                context?.let { it1 ->
                    viewModel.createAccount(
                        binding.loginEmail.text.toString().trim(),
                        binding.loginPass.text.toString().trim(),
                        it1
                    )
                }
                viewModel.getUserInfo()!!.observe(this, Observer {
                    if (it != null) {
                        startActivity(Intent(context, MainActivity::class.java))
                    }
                });
            }
        }

        binding.loginSignin.setOnClickListener {
            activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_content_view, SignInFragment(), "SignUp")
                .commit()
        }
    }

}
