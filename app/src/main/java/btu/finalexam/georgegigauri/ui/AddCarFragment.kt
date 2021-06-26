package btu.finalexam.georgegigauri.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import btu.finalexam.georgegigauri.base.BaseFragment
import btu.finalexam.georgegigauri.databinding.FragmentAddCarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCarFragment : BaseFragment<FragmentAddCarBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddCarBinding =
        FragmentAddCarBinding.inflate(inflater, container, false)

    override fun onReady() {
        TODO("Not yet implemented")
    }
}