package btu.finalexam.georgegigauri.ui

import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import btu.finalexam.georgegigauri.adapter.CommentAdapter
import btu.finalexam.georgegigauri.base.BaseActivity
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.data.model.Comment
import btu.finalexam.georgegigauri.databinding.ActivityCarDetailsBinding
import btu.finalexam.georgegigauri.extension.setImage
import btu.finalexam.georgegigauri.extension.snackBar
import btu.finalexam.georgegigauri.util.UIState
import btu.finalexam.georgegigauri.viewmodel.CarDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CarDetailsActivity : BaseActivity<ActivityCarDetailsBinding>() {

    private val viewModel by viewModels<CarDetailsViewModel>()
    private val adapter = CommentAdapter()

    override fun getBinding(layoutInflater: LayoutInflater): ActivityCarDetailsBinding =
        ActivityCarDetailsBinding.inflate(layoutInflater)

    override fun onReady() {
        init()
        initObservers()
    }

    private fun init() {
        binding.rvComments.adapter = adapter
        val carId = intent.getStringExtra("car_id")
        carId?.let {
            viewModel.load(it)
            viewModel.loadComments(it)
        }

        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.ivComment.setOnClickListener {
            if (binding.etComment.text.isNotEmpty())
                viewModel.addComment(carId!!, binding.etComment.text.toString())
        }
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it) {
                    is UIState.Loading -> showProgress()
                    is UIState.Error -> {
                        snackBar(binding.root, it.message)
                        hideProgress()
                    }
                    is UIState.Success -> {
                        onSuccessLoadDetails(it.data)
                        hideProgress()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.commentsUiState.collect {
                Log.d("EMITTED_BUT", "WHAT?")
                when (it) {
                    is UIState.Loading -> binding.progressComments.isVisible = true
                    is UIState.Error -> {
                        snackBar(binding.root, it.message)
                        binding.progressComments.isVisible = false
                    }
                    is UIState.Success -> {
                        onSuccessLoadComments(it.data)
                        binding.progressComments.isVisible = false
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun onSuccessLoadDetails(it: Car) {
        binding.tvTitle.text = "${it.brand} ${it.model}"
        binding.ivCar.setImage(it.image)
        binding.tvDescription.text = it.description
    }

    private fun onSuccessLoadComments(comments: List<Comment>) {
        Log.d("COMMENTS", comments.toString())
        adapter.submitList(comments)
        binding.etComment.text.clear()
        binding.etComment.clearFocus()
    }
}