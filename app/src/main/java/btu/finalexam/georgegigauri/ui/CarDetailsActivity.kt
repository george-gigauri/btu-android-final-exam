package btu.finalexam.georgegigauri.ui

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import btu.finalexam.georgegigauri.base.BaseActivity
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.data.model.Comment
import btu.finalexam.georgegigauri.databinding.ActivityCarDetailsBinding
import btu.finalexam.georgegigauri.extension.snackBar
import btu.finalexam.georgegigauri.util.UIState
import btu.finalexam.georgegigauri.viewmodel.CarDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CarDetailsActivity : BaseActivity<ActivityCarDetailsBinding>() {

    private val viewModel by viewModels<CarDetailsViewModel>()

    override fun getBinding(layoutInflater: LayoutInflater): ActivityCarDetailsBinding =
        ActivityCarDetailsBinding.inflate(layoutInflater)

    override fun onReady() {
        init()
        lifecycleScope.launchWhenStarted { initObservers() }
    }

    private fun init() {
        val carId = intent.getStringExtra("car_id")
        carId?.let {
            viewModel.load(it)
            viewModel.loadComments(it)
        }
    }

    private suspend fun initObservers() {
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

        viewModel.commentsUiState.collect {
            when (it) {
                is UIState.Loading -> showProgress()
                is UIState.Error -> {
                    snackBar(binding.root, it.message)
                    hideProgress()
                }
                is UIState.Success -> {
                    onSuccessLoadComments(it.data)
                    hideProgress()
                }
                else -> Unit
            }
        }
    }

    private fun onSuccessLoadDetails(it: Car) {

    }

    private fun onSuccessLoadComments(comments: List<Comment>) {

    }
}