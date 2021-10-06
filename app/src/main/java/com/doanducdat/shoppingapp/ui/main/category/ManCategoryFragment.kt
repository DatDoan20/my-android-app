package com.doanducdat.shoppingapp.ui.main.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.CategoryBasicAdapter
import com.doanducdat.shoppingapp.databinding.FragmentManCategoryBinding
import com.doanducdat.shoppingapp.model.category.CategoryListFactory
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants

class ManCategoryFragment : BaseFragment<FragmentManCategoryBinding>(), MyActionApp {

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }

    private val manCategoryAdapter by lazy { CategoryBasicAdapter() }

    lateinit var listNameCategory: MutableList<TextView>

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManCategoryBinding = FragmentManCategoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListNameCategory()
        setUpRcvCategory()
        setUpActionClick()
    }

    private fun setListNameCategory() {
        with(binding) {
            listNameCategory = mutableListOf(
                txtShirt, txtTrouser, txtCoat, txtHomeWear, txtSportWear
            )
        }
    }

    private fun setUpRcvCategory() {
        binding.rcvManCategory.layoutManager = GridLayoutManager(
            requireContext(), 2,
            RecyclerView.VERTICAL, false
        )
        binding.rcvManCategory.adapter = manCategoryAdapter

        manCategoryAdapter.mySetOnClickCategoryAdapter {
            val bundleCategory = bundleOf("CATEGORY" to it)
            controller.navigate(R.id.productListFragment, bundleCategory)
        }
    }

    private fun setUpActionClick() {
        with(binding) {
            txtShirt.setOnClickListener {
                //format name category unselected and text is selected
                formatListNameCategory(listNameCategory, txtShirt)
                //set adapter when click name category
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
            AppConstants.ActionClick.SELECT_SHIRT -> {
                manCategoryAdapter.setCategoryList(CategoryListFactory.getInstance().manShirt())
            }
            AppConstants.ActionClick.SELECT_TROUSER -> {
                manCategoryAdapter.setCategoryList(CategoryListFactory.getInstance().manTrouser())
            }
            AppConstants.ActionClick.SELECT_HOME_WEAR -> {
                manCategoryAdapter.setCategoryList(CategoryListFactory.getInstance().manHomeWear())
            }
            AppConstants.ActionClick.SELECT_COAT -> {
                manCategoryAdapter.setCategoryList(CategoryListFactory.getInstance().manCoat())
            }
            AppConstants.ActionClick.SELECT_SPORT_WEAR -> {
                manCategoryAdapter.setCategoryList(CategoryListFactory.getInstance().manSportWear())
            }
        }
    }
}