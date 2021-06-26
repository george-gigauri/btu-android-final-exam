package btu.finalexam.georgegigauri.ui

import android.view.LayoutInflater
import btu.finalexam.georgegigauri.base.BaseActivity
import btu.finalexam.georgegigauri.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getBinding(layoutInflater: LayoutInflater): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun onReady() {

    }
}