package btu.finalexam.georgegigauri.ui

import android.view.LayoutInflater
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import btu.finalexam.georgegigauri.R
import btu.finalexam.georgegigauri.base.BaseActivity
import btu.finalexam.georgegigauri.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getBinding(layoutInflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onReady() {
        binding.bottomNavView.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }
}