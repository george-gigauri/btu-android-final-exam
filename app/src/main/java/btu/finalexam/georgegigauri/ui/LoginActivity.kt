package btu.finalexam.georgegigauri.ui

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import btu.finalexam.georgegigauri.base.BaseActivity
import btu.finalexam.georgegigauri.databinding.ActivityLoginBinding
import btu.finalexam.georgegigauri.extension.openActivity
import btu.finalexam.georgegigauri.extension.snackBar
import btu.finalexam.georgegigauri.util.UIState
import btu.finalexam.georgegigauri.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getBinding(layoutInflater: LayoutInflater): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun onReady() {
        setOnClickListeners()
        lifecycleScope.launchWhenStarted {
            initObservers()
        }
    }

    private fun setOnClickListeners() {
        binding.btnLogin.setOnClickListener { login() }
        binding.tvResetPassword.setOnClickListener { resetPassword() }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.toString()

        if (email.isEmpty()) {
            binding.etEmail.error = "ელ. ფოსტის შეტანა სავალდებულოა"
            binding.etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "პაროლის შეტანა სავალდებულოა"
            binding.etPassword.requestFocus()
            return
        }

        viewModel.login(email, password)
    }

    private fun resetPassword() {
        val email = binding.etEmail.text.toString()
        if (email.isEmpty()) {
            binding.etEmail.error = "ელ. ფოსტის შეტანა სავალდებულოა"
            binding.etEmail.requestFocus()
            return
        }

        viewModel.resetPassword(email)
    }

    private suspend fun initObservers() {
        viewModel.uiState.collect {
            when (it) {
                is UIState.Empty -> {
                    hideProgress()
                }
                is UIState.Loading -> {
                    showProgress()
                }
                is UIState.Error -> {
                    hideProgress()
                    snackBar(binding.root, it.message)
                }
                is UIState.Success -> {
                    // Handle Success Action
                    openActivity(MainActivity::class.java)
                }
            }
        }
    }
}