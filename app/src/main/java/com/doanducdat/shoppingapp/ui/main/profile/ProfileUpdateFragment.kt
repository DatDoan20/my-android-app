package com.doanducdat.shoppingapp.ui.main.profile

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker.checkSelfPermission
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
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.imgAvatar.setImageURI(result.data?.data)
            }
        }

    val viewModel: ProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBackFragment()
        loadSpinner()
        checkLengthAndValidationName()
        setUpActionClick()

    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controller.popBackStack()
        }
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

    private fun setUpActionClick() {
        binding.imgCamera.setOnClickListener {
            doActionClick(AppConstants.ActionClick.PICKER_PHOTO)
        }

        binding.btnSave.setOnClickListener {
            doActionClick(AppConstants.ActionClick.SAVE_INFO_PERSONAL)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.PICKER_PHOTO -> {
                requestPermissionAndPickImage()
            }
            AppConstants.ActionClick.SAVE_INFO_PERSONAL -> {
                handlePreSave()
            }
        }
    }

    private fun requestPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pickImage()
            return
        }
        showLongToast("ahihi")
        val result = checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE)

        if (result == PackageManager.PERMISSION_GRANTED) {
            pickImage()
        } else {
            showLongToast(AppConstants.MsgInfo.PERMISSION_PICK_PHOTO_NOT_GRANTED)
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher.launch(intent)
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