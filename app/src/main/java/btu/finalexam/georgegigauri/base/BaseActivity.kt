package btu.finalexam.georgegigauri.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    abstract fun getBinding(layoutInflater: LayoutInflater): VB

    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getBinding(layoutInflater)
        setContentView(binding.root)
        onReady()
    }

    abstract fun onReady()

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}