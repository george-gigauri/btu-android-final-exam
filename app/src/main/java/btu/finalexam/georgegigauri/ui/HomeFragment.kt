package btu.finalexam.georgegigauri.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
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

        binding.ivSort.setOnClickListener { showMenu() }
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
                    adapter.submitList(it.data) {
                        binding.rvCars.scrollToPosition(0)
                    }
                    hideProgress()
                }
            }
        }
    }

    private fun showMenu() {
        val popup = PopupMenu(requireContext(), binding.ivSort)
        HomeViewModel.SortBy.values().forEach { popup.menu.add(it.toName()) }

        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                HomeViewModel.SortBy.DEFAULT.toName() -> viewModel.refresh()
                HomeViewModel.SortBy.BRAND.toName() -> viewModel.refresh(HomeViewModel.SortBy.BRAND)
                HomeViewModel.SortBy.AUTHOR.toName() -> viewModel.refresh(HomeViewModel.SortBy.AUTHOR)
            }

            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    override fun showProgress() {
        binding.swipeRefresh.isRefreshing = true
    }

    override fun hideProgress() {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onCarClick(car: Car) {
        startActivity(Intent(requireContext(), CarDetailsActivity::class.java).apply {
            putExtra("car_id", car.id)
        })
    }
}