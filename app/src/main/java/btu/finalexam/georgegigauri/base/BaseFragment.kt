package btu.finalexam.georgegigauri.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import btu.finalexam.georgegigauri.custom.ProgressDialog

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun onReady()

    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    private val progressDialog = ProgressDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onReady()
    }

    open fun showProgress() {
        if (!progressDialog.isAdded)
            progressDialog.show(requireActivity().supportFragmentManager, "progress")
    }

    open fun hideProgress() {
        if (progressDialog.isAdded)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}