package com.doanducdat.shoppingapp.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.doanducdat.shoppingapp.ui.main.MainActivity




abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        return binding.root
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    fun focusView(requestedView: View, msg: String) {
        requestedView.requestFocus()
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
    fun showToast(msg:String){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
    fun setStateViews(isEnable: Boolean, vararg views: View) {
        for (view in views){
            view.isEnabled = isEnable
        }
    }

    fun setStateProgressBar(isVisible: Int, progressBar: View) {
        progressBar.visibility = isVisible
    }
}