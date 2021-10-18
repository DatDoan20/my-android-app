package com.doanducdat.shoppingapp.ui.main.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CategoryBasicAdapter
import com.doanducdat.shoppingapp.databinding.FragmentWomanCategoryBinding
import com.doanducdat.shoppingapp.model.category.CategoryListFactory
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants


class WomanCategoryFragment : BaseFragment<FragmentWomanCategoryBinding>(), MyActionApp {

    val womanCategoryAdapter by lazy { CategoryBasicAdapter() }
    lateinit var listNameCategory: MutableList<TextView>

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWomanCategoryBinding =
        FragmentWomanCategoryBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListNameCategory()
        setUpRcvCategory()
        setUpActionClick()
        setDefaultDataDisplay()
    }

    private fun setListNameCategory() {
        with(binding) {
            listNameCategory = mutableListOf(
                txtDress, txtShirt, txtTrouser, txtCoat, txtHomeWear, txtSportWear
            )
        }
    }

    private fun setUpRcvCategory() {
        binding.rcvWomanCategory.layoutManager = GridLayoutManager(
            requireContext(), 2,
            RecyclerView.VERTICAL, false
        )
        binding.rcvWomanCategory.adapter = womanCategoryAdapter
        womanCategoryAdapter.mySetOnClickCategoryAdapter {
            val bundleCategory = bundleOf("CATEGORY" to it)
            controllerMain.navigate(R.id.productListFragment, bundleCategory)
        }
    }

    private fun setUpActionClick() {
        with(binding) {
            txtDress.setOnClickListener {
                //format name category unselected and selected
                formatListNameCategory(listNameCategory, txtDress)
                //set adapter when click name category
                doActionClick(AppConstants.ActionClick.SELECT_DRESS)
            }
            txtShirt.setOnClickListener {
                formatListNameCategory(listNameCategory, txtShirt)
                doActionClick(AppConstants.ActionClick.SELECT_SHIRT)
            }
            txtTrouser.setOnClickListener {
                formatListNameCategory(listNameCategory, txtTrouser)
                doActionClick(AppConstants.ActionClick.SELECT_TROUSER)
            }
            txtHomeWear.setOnClickListener {
                formatListNameCategory(listNameCategory, txtHomeWear)
                doActionClick(AppConstants.ActionClick.SELECT_HOME_WEAR)
            }
            txtCoat.setOnClickListener {
                formatListNameCategory(listNameCategory, txtCoat)
                doActionClick(AppConstants.ActionClick.SELECT_COAT)
            }
            txtSportWear.setOnClickListener {
                formatListNameCategory(listNameCategory, txtSportWear)
                doActionClick(AppConstants.ActionClick.SELECT_SPORT_WEAR)
            }
        }
    }


    override fun doActionClick(CODE_ACTION_CLICK: Int) {

        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.SELECT_DRESS -> {
                womanCategoryAdapter.setCategoryList(CategoryListFactory.getInstance().womanDress())
            }
            AppConstants.ActionClick.SELECT_SHIRT -> {
                womanCategoryAdapter.setCategoryList(CategoryListFactory.getInstance().womanShirt())
            }
            AppConstants.ActionClick.SELECT_TROUSER -> {
                womanCategoryAdapter.setCategoryList(
                    CategoryListFactory.getInstance().womanTrouser()
                )
            }
            AppConstants.ActionClick.SELECT_HOME_WEAR -> {
                womanCategoryAdapter.setCategoryList(
                    CategoryListFactory.getInstance().womanHomeWear()
                )
            }
            AppConstants.ActionClick.SELECT_COAT -> {
                womanCategoryAdapter.setCategoryList(CategoryListFactory.getInstance().womanCoat())
            }
            AppConstants.ActionClick.SELECT_SPORT_WEAR -> {
                womanCategoryAdapter.setCategoryList(
                    CategoryListFactory.getInstance().womanSportWear()
                )
            }
        }
    }
    private fun setDefaultDataDisplay() {
        //format name category unselected and selected
        formatListNameCategory(listNameCategory, binding.txtDress)
        //set adapter when click name category
        doActionClick(AppConstants.ActionClick.SELECT_DRESS)
    }
}