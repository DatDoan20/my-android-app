package com.doanducdat.shoppingapp.ui.main.profile

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentProfileUpdateBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProfileUpdateFragment : BaseFragment<FragmentProfileUpdateBinding>(), MyActionApp {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileUpdateBinding =
        FragmentProfileUpdateBinding.inflate(inflater, container, false)

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    val viewModel: ProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLengthAndValidationName()
        setUpBackFragment()
        loadSpinner()

    }

    private fun loadSpinner() {
        val sex = listOf("Nam", "Nữ")
        val sexAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, sex)
        binding.spinnerSex.setAdapter(sexAdapter)

        val birthYear = mutableListOf<String>()
        val birthYearAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, birthYear)

        for (i in 1956..Calendar.getInstance().get(Calendar.YEAR)) {
            birthYear.add(i.toString())
        }
        binding.spinnerBirthYear.setAdapter(birthYearAdapter)
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controller.popBackStack()
        }
    }

    private fun checkLengthAndValidationName() {
        with(binding) {
            txtInputEdtName.doOnTextChanged { text, _, _, _ ->
                val errMsgValid = FormValidation.checkValidationName(text.toString())
                val errMsgLength =
                    FormValidation.checkLengthName(text.toString().trim().length > 50)
                when {
                    errMsgLength == null && errMsgValid == null -> { // not any err
                        viewModel.stateErrName = false
                        txtInputLayoutName.error = null
                    }
                    errMsgLength != null -> { // err length
                        viewModel.stateErrName = true
                        txtInputLayoutName.error = errMsgLength
                    }
                    errMsgValid != null -> { // err contains number or special character
                        viewModel.stateErrName = true
                        txtInputLayoutName.error = errMsgValid
                    }
                }
            }
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        binding.imgPhoto.setOnClickListener {

        }
        binding.btnSave.setOnClickListener {
            handlePreSave()
        }
    }

    private fun handlePreSave() {
        when {
            viewModel.stateErrName -> {
                return focusView(binding.txtInputEdtName, AppConstants.MsgErr.NAME_ERR_MSG)
            }
            TextUtils.isEmpty(binding.spinnerBirthYear.text.toString().trim()) -> {
                return focusView(binding.spinnerBirthYear, AppConstants.MsgErr.BIRTH_YEAR_ERR_MSG)
            }
            TextUtils.isEmpty(binding.spinnerSex.text.toString().trim()) -> {
                return focusView(binding.spinnerSex, AppConstants.MsgErr.GENDER_ERR_MSG)
            }
        }
        handleSave()
    }

    private fun handleSave() {
        viewModel.updateProfile()
    }
}