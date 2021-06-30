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
import btu.finalexam.georgegigauri.databinding.FragmentAddCarBinding
import btu.finalexam.georgegigauri.extension.snackBar
import btu.finalexam.georgegigauri.extension.visible
import btu.finalexam.georgegigauri.util.UIState
import btu.finalexam.georgegigauri.viewmodel.AddCarViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddCarFragment : BaseFragment<FragmentAddCarBinding>() {
    private val viewModel: AddCarViewModel by viewModels()
    private lateinit var imageUri: Uri

    val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result : ActivityResult? ->
        if(result?.resultCode == Activity.RESULT_OK){
            imageUri = result.data?.data!!
            binding.ivImage.visible(false)
            binding.tvAddImage.visible(false)
            binding.addCardView.setPadding(0)
            binding.ivPicture.visible(true)
            Glide.with(this)
                .load(imageUri)
                .centerInside()
                .into(binding.ivPicture)
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddCarBinding =
        FragmentAddCarBinding.inflate(inflater, container, false)

    override fun onReady() {
        binding.addCardView.setOnClickListener {
            selectImage()

        }
        binding.btnAdd.setOnClickListener { addCar() }
        lifecycleScope.launchWhenStarted { initObserver() }
    }

    private suspend fun initObserver() {
        viewModel.uiState.collect {
            when (it) {
                is UIState.Success -> {
                    requireActivity().findNavController(
                        R.id.nav_host_fragment
                    ).navigate(R.id.navMain)

                    resetScreen()
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


    private fun addCar() {
        val brand = binding.etBrand.text.toString()
        val model = binding.etModel.text.toString()
        val description = binding.etDescription.text.toString()

        if (brand.isEmpty()) {
            binding.etBrand.error = "გთხოვთ მიუთითოთ მანქანის ბრენდი"
            binding.etBrand.requestFocus()
            return
        }

        if(model.isEmpty()){
            binding.etModel.error = "გთხოვთ მიუთითოთ მანქანის მოდელი"
            binding.etModel.requestFocus()
            return
        }
        if(description.isEmpty()){
            binding.etDescription.error = "გთხოვთ დაამატოთ მანქანის აღწერა"
            binding.etDescription.requestFocus()
            return
        }

        viewModel.addCar(brand,model,description,imageUri)
        resetScreen()
    }



    private fun selectImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        getImage.launch(intent)
    }

    private fun resetScreen() {
        binding.ivImage.visible(true)
        binding.tvAddImage.visible(true)
        binding.ivPicture.visible(false)
        binding.etBrand.text.clear()
        binding.etModel.text.clear()
        binding.etDescription.text.clear()
    }
}