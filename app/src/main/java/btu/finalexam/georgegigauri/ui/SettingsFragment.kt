package btu.finalexam.georgegigauri.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import btu.finalexam.georgegigauri.R
import btu.finalexam.georgegigauri.base.BaseFragment
import btu.finalexam.georgegigauri.databinding.FragmentSettingsBinding
import btu.finalexam.georgegigauri.extension.openActivity
import btu.finalexam.georgegigauri.extension.snackBar
import btu.finalexam.georgegigauri.extension.visible
import btu.finalexam.georgegigauri.util.UIState
import btu.finalexam.georgegigauri.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    private val viewModel : ProfileViewModel by viewModels()
    private lateinit var imageUri : Uri

    val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result : ActivityResult? ->
        if(result?.resultCode == Activity.RESULT_OK){
            imageUri = result.data?.data!!
            binding.ivProfileRounded.visible(true)
            binding.ivProfile.visible(false)
            Glide.with(this)
                .load(imageUri)
                .centerInside()
                .into(binding.ivProfileRounded)
        }
    }


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onReady() {
        lifecycleScope.launchWhenStarted { initObserver() }
        binding.ivProfile.setOnClickListener {
            selectImage()
        }
        binding.btnSave.setOnClickListener {
            if(binding.etPassword.text.isNotEmpty()){
                if(binding.etPassword.text.toString() != binding.etRepeatPassword.text.toString()){
                    binding.etRepeatPassword.error = "პაროლი არ დაემთხვა"
                    binding.etRepeatPassword.requestFocus()
                } else {
                    setPassword(binding.etPassword.text.toString())
                }
            }
            addUser(imageUri)
        }


        binding.btnLogOut.setOnClickListener {
            logout()
        }
    }


    private fun addUser(uri: Uri) {
        viewModel.addUser(uri)
    }

    private fun setPassword(password :String) {
        viewModel.setNewPassword(password)
    }




    private fun selectImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        getImage.launch(intent)
    }

    fun logout() {
        viewModel.logOut()
    }

    private suspend fun initObserver() {
        viewModel.uiState.collect {
            when (it) {
                is UIState.Success -> {
                    activity?.openActivity(LoginActivity::class.java)
                    hideProgress()
                }

                is UIState.Loading -> showProgress()

                is UIState.Error -> {
                    hideProgress()
                    snackBar(it.message)
                }

                else -> Unit
            }
        }
    }
}