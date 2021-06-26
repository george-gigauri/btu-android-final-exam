package btu.finalexam.georgegigauri.ui

import android.view.LayoutInflater
import btu.finalexam.georgegigauri.base.BaseActivity
import btu.finalexam.georgegigauri.databinding.ActivitySplashBinding
import btu.finalexam.georgegigauri.extension.openActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private var firebaseUser: FirebaseUser? = null

    override fun getBinding(layoutInflater: LayoutInflater): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun onReady() {
        firebaseUser = Firebase.auth.currentUser
        if (firebaseUser != null) {
            openActivity(MainActivity::class.java)
        } else openActivity(LoginActivity::class.java)
    }
}