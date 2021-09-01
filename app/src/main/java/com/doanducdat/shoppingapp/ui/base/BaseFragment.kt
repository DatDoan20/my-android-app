package com.doanducdat.shoppingapp.ui.base

import android.animation.AnimatorSet
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.AppBarLayout


abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected lateinit var binding: VB
    val setAnimationSearchView = AnimatorSet()

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

    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun setStateViews(isEnable: Boolean, vararg views: View) {
        for (view in views) {
            view.isEnabled = isEnable
        }
    }

    fun setStateProgressBar(isVisible: Int, progressBar: View) {
        progressBar.visibility = isVisible
    }

    fun hideSearchPlate(view: View) {
        val searchPlate = resources.getIdentifier("android:id/search_plate", null, null)
        val mSearchPlate = view.findViewById(searchPlate) as View
        mSearchPlate.setBackgroundColor(Color.TRANSPARENT)
    }

    fun collapsingListen(
        searchView: View,
    ): AppBarLayout.OnOffsetChangedListener {
        return AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val params = searchView.layoutParams
            if (verticalOffset == 0) {
//                val newWidth = WindowManager.LayoutParams.MATCH_PARENT
//                changeSizeSearchView(searchView, searchView.layoutParams.width, newWidth)
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
            } else  {
//                val newWidth = (searchView.context.resources.displayMetrics.density *250).toInt()
//                changeSizeSearchView(searchView, searchView.layoutParams.width, newWidth)
                params.width = (searchView.context.resources.displayMetrics.density * 250).toInt()
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            searchView.layoutParams = params
        }
    }

//    private fun changeSizeSearchView(searchView: View, currentWidth: Int, newWidth: Int) {
//        val slideAnimator = ValueAnimator
//            .ofInt(
//                currentWidth,
//                newWidth
//            )
//            .setDuration(300)
//
//        slideAnimator.addUpdateListener { animation ->
//            val value = animation.animatedValue as Int
//            searchView.layoutParams.width = value
//            searchView.requestLayout()
//        }
//        setAnimationSearchView.play(slideAnimator)
//        setAnimationSearchView.interpolator = AccelerateDecelerateInterpolator()
//        setAnimationSearchView.start()
//
//    }

}