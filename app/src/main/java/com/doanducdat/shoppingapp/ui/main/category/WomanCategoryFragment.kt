package com.doanducdat.shoppingapp.ui.main.category

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CategoryBasicAdapter
import com.doanducdat.shoppingapp.databinding.FragmentWomanCategoryBinding
import com.doanducdat.shoppingapp.module.category.CategoryListFactory
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants


class WomanCategoryFragment : BaseFragment<FragmentWomanCategoryBinding>(), MyActionApp {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWomanCategoryBinding =
        FragmentWomanCategoryBinding.inflate(inflater, container, false)

    val adapter = CategoryBasicAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvWomanCategory.layoutManager = GridLayoutManager(
            requireContext(), 2,
            RecyclerView.VERTICAL, false
        )
        binding.rcvWomanCategory.adapter = adapter

        setUpActionClick()

    }

    private fun setUpActionClick() {
        with(binding) {
            txtDress.setOnClickListener {
                changeTextDisplay(it as TextView)
                doActionClick(AppConstants.ActionClick.SELECT_DRESS)
            }
            txtShirt.setOnClickListener {
                changeTextDisplay(it as TextView)
                doActionClick(AppConstants.ActionClick.SELECT_SHIRT)
            }
            txtTrouser.setOnClickListener {
                changeTextDisplay(it as TextView)
                doActionClick(AppConstants.ActionClick.SELECT_TROUSER)
            }
            txtHomeWear.setOnClickListener {
                changeTextDisplay(it as TextView)
                doActionClick(AppConstants.ActionClick.SELECT_HOME_WEAR)
            }
            txtCoat.setOnClickListener {
                changeTextDisplay(it as TextView)
                doActionClick(AppConstants.ActionClick.SELECT_COAT)
            }
            txtSportWear.setOnClickListener {
                changeTextDisplay(it as TextView)
                doActionClick(AppConstants.ActionClick.SELECT_SPORT_WEAR)
            }
        }
    }

    private fun changeTextDisplay(textView: TextView) {
        textView.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.textTitleColorGeneric
            )
        )
        textView.setTypeface(null, Typeface.BOLD_ITALIC)
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {

        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.SELECT_DRESS -> {
                adapter.setCategoryList(CategoryListFactory.getInstance().womanDress())
            }
            AppConstants.ActionClick.SELECT_SHIRT -> {
                adapter.setCategoryList(CategoryListFactory.getInstance().womanShirt())

            }
            AppConstants.ActionClick.SELECT_TROUSER -> {
                adapter.setCategoryList(CategoryListFactory.getInstance().womanTrouser())

            }
            AppConstants.ActionClick.SELECT_HOME_WEAR -> {
                adapter.setCategoryList(CategoryListFactory.getInstance().womanHomeWear())

            }
            AppConstants.ActionClick.SELECT_COAT -> {
                adapter.setCategoryList(CategoryListFactory.getInstance().womanCoat())

            }
            AppConstants.ActionClick.SELECT_SPORT_WEAR -> {
                adapter.setCategoryList(CategoryListFactory.getInstance().womanSportWear())

            }
        }
    }
}