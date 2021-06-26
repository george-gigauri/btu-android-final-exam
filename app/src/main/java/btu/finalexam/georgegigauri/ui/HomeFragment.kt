package btu.finalexam.georgegigauri.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import btu.finalexam.georgegigauri.base.BaseFragment
import btu.finalexam.georgegigauri.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun onReady() {
        TODO("Not yet implemented")
    }
}