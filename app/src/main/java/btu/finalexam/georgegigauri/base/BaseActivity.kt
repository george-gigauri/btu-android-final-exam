package btu.finalexam.georgegigauri.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import btu.finalexam.georgegigauri.custom.ProgressDialog

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    abstract fun getBinding(layoutInflater: LayoutInflater): VB

    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    private val progressDialog: ProgressDialog = ProgressDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getBinding(layoutInflater)
        setContentView(binding.root)
        onReady()
    }

    abstract fun onReady()

    fun showProgress() {
        if (!progressDialog.isAdded)
            progressDialog.show(supportFragmentManager, "progress")
    }

    fun hideProgress() {
        if (progressDialog.isAdded)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}