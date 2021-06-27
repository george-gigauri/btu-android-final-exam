package btu.finalexam.georgegigauri.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import btu.finalexam.georgegigauri.adapter.CarAdapter
import btu.finalexam.georgegigauri.base.BaseFragment
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.databinding.FragmentHomeBinding
import btu.finalexam.georgegigauri.util.UIState
import btu.finalexam.georgegigauri.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), CarAdapter.OnCarItemClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private val adapter: CarAdapter = CarAdapter(this)

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun onReady() {
        init()
        lifecycleScope.launchWhenStarted { initObservers() }
    }

    private fun init() {
        binding.rvCars.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private suspend fun initObservers() {
        viewModel.carsUiState.collect {
            when (it) {
                is UIState.Empty -> {
                    hideProgress()
                }
                is UIState.Loading -> {
                    showProgress()
                }
                is UIState.Error -> {
                    hideProgress()
                }
                is UIState.Success -> {
                    // Handle Success Action
                    Log.i("DataClass", it.data.toString())
                    adapter.submitList(it.data)
                    hideProgress()
                }
            }
        }
    }

    override fun showProgress() {
        binding.swipeRefresh.isRefreshing = true
    }

    override fun hideProgress() {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onCarClick(car: Car) {
    }
}