package com.doanducdat.shoppingapp.ui.main.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.adapter.CategoryBasicAdapter
import com.doanducdat.shoppingapp.databinding.FragmentWomanCategoryBinding
import com.doanducdat.shoppingapp.module.category.CategoryListFactory
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants


class WomanCategoryFragment : BaseFragment<FragmentWomanCategoryBinding>(), MyActionApp {
    val adapter = CategoryBasicAdapter()
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
        binding.rcvWomanCategory.adapter = adapter
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