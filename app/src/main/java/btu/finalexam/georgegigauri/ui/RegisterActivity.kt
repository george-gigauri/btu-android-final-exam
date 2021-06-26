package btu.finalexam.georgegigauri.ui

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import btu.finalexam.georgegigauri.base.BaseActivity
import btu.finalexam.georgegigauri.databinding.ActivityRegisterBinding
import btu.finalexam.georgegigauri.extension.openActivity
import btu.finalexam.georgegigauri.extension.snackBar
import btu.finalexam.georgegigauri.util.UIState
import btu.finalexam.georgegigauri.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun getBinding(layoutInflater: LayoutInflater): ActivityRegisterBinding =
        ActivityRegisterBinding.inflate(layoutInflater)

    override fun onReady() {
        setOnClickListeners()
        lifecycleScope.launchWhenStarted { initObservers() }
    }

    private fun setOnClickListeners() {
        binding.btnSignUp.setOnClickListener { register() }
        binding.llSignIn.setOnClickListener { openActivity(LoginActivity::class.java) }
        binding.ivBack.setOnClickListener { onBackPressed() }
    }

    private fun register() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val repeatPassword = binding.etRepeatPassword.text.toString()

        if (name.isEmpty()) {
            binding.etName.error = "ეს ველი სავალდებულოა"
            binding.etName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "ეს ველი სავალდებულოა"
            binding.etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "ეს ველი სავალდებულოა"
            binding.etPassword.requestFocus()
            return
        }

        if (repeatPassword.isEmpty()) {
            binding.etRepeatPassword.error = "ეს ველი სავალდებულოა"
            binding.etRepeatPassword.requestFocus()
            return
        }

        if (password != repeatPassword) {
            binding.etRepeatPassword.error = "პაროლები უნდა ემთხვეოდეს ერთმანეთს"
            binding.etRepeatPassword.requestFocus()
            return
        }

        viewModel.register(name, email, password)
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