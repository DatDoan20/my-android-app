package com.doanducdat.shoppingapp.ui.base

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.doanducdat.shoppingapp.R
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

    /**
     *  use when there is progressbar in form
     */
    fun setStateProgressBar(isVisible: Int, progressBar: View) {
        progressBar.visibility = isVisible
    }

    /**
     *  use when there is searchView in form
     */
    fun hideSearchPlate(view: View) {
        val searchPlate = resources.getIdentifier("android:id/search_plate", null, null)
        val mSearchPlate = view.findViewById(searchPlate) as View
        mSearchPlate.setBackgroundColor(Color.TRANSPARENT)
    }

    /** use when click searchView -> navigate to Search Fragment
     *
     * Not show keyboard when click edittext.
     *
     * Set event for Edittext of SearchView.
     *
     * Set event for SearchView(click on icon).
     */
    fun setOnSearchView(searchView: View, callback: () -> Unit) {
        val id = resources.getIdentifier("android:id/search_src_text", null, null)
        val searchViewEdt: EditText = searchView.findViewById(id)
        // not show keyboard when click edittext
        searchViewEdt.showSoftInputOnFocus = false
        //can't not use setOnClick, click single and handle event -> use focusChange
        searchViewEdt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                callback.invoke()
            }
        }
        searchView.setOnClickListener {
            callback.invoke()
        }
    }

    /** use when there is Collapsing appbar in form
     *
     * change size SearchView
     *
     * refreshLayout disable when appbar layout is collapsing
     */
    fun collapsingListen(
        searchView: View,
        refreshLayout: View
    ): AppBarLayout.OnOffsetChangedListener {
        return AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val params = searchView.layoutParams
            if (verticalOffset == 0) {
                refreshLayout.isEnabled = true // appbar is fully -> allow refresh layout
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
            } else {
                refreshLayout.isEnabled = false
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

    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


    /**
     * use format textview in form (eg. name of category)
     */
    fun formatListNameCategory(
        textViewsUnSelect: List<TextView>,
        textViewSelected: TextView
    ) {
        textViewsUnSelect.forEach {
            formatTextViewUnSelected(it)
        }
        formatTextViewSelected(textViewSelected)
    }

    private fun formatTextViewSelected(textView: TextView) {
        textView.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textTitleColorGeneric
            )
        )
        textView.setTypeface(null, Typeface.BOLD_ITALIC)
    }

    private fun formatTextViewUnSelected(textView: TextView) {
        textView.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textDesColorGeneric
            )
        )
        textView.setTypeface(null, Typeface.NORMAL)
    }
}