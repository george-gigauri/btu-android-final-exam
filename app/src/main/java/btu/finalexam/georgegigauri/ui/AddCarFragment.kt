package btu.finalexam.georgegigauri.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import btu.finalexam.georgegigauri.base.BaseFragment
import btu.finalexam.georgegigauri.databinding.FragmentAddCarBinding
import btu.finalexam.georgegigauri.extension.visible
import btu.finalexam.georgegigauri.viewmodel.AddCarViewModel
import btu.finalexam.georgegigauri.viewmodel.AuthViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.ImageDecoderResourceDecoder
import dagger.hilt.android.AndroidEntryPoint

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

    }



    private fun addCar() {
        val brand = binding.etBrand.text.toString()
        val model = binding.etModel.text.toString()
        val description = binding.etDescription.text.toString()

        if(brand.isEmpty()){
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

    private fun resetScreen(){
        binding.ivImage.visible(true)
        binding.tvAddImage.visible(true)
        binding.addCardView.setPadding(42)
        binding.ivPicture.visible(false)
        binding.etBrand.text = ""
        binding.etModel.text = ""
        binding.etDescription.text = ""
    }



}