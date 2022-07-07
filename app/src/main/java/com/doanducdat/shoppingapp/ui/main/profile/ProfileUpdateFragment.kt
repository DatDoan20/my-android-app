package com.doanducdat.shoppingapp.ui.main.profile

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import coil.load
import coil.request.CachePolicy
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentProfileUpdateBinding
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.handler.HandlerErrRes
import com.doanducdat.shoppingapp.utils.handler.HandlerFile
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


@AndroidEntryPoint
class ProfileUpdateFragment : BaseFragment<FragmentProfileUpdateBinding>(), MyActionApp {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileUpdateBinding =
        FragmentProfileUpdateBinding.inflate(inflater, container, false)

    val viewModel: ProfileViewModel by viewModels()
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenLoadingForm()
        setUpBackFragment()
        loadDataForm()
        checkLengthAndValidationName()
        initResultLauncher()
        setUpActionClick()

    }


    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            setStateEnableViews(!it, binding.btnSave)
            if (it) {
                setStateVisibleView(View.VISIBLE, binding.spinKitProgressBar)
            } else {
                setStateVisibleView(View.GONE, binding.spinKitProgressBar)
            }
        })
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controllerMain.popBackStack()
        }
    }

    private fun loadDataForm() {
        //load name
        binding.txtInputEdtName.setText(InfoLocalUser.currentUser?.name)
        //load spinner sex
        val sex = listOf("Nam", "Nữ")
        val sexAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, sex)
        binding.spinnerSex.setAdapter(sexAdapter)
        //load spinner birthYear
        val birthYear = mutableListOf<String>()
        val birthYearAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, birthYear)

        for (i in Calendar.getInstance().get(Calendar.YEAR) downTo 1956) {
            birthYear.add(i.toString())
        }
        binding.spinnerBirthYear.setAdapter(birthYearAdapter)

        //load value default
        if (InfoLocalUser.currentUser?.sex == "male") binding.spinnerSex.setText("Nam", false)
        if (InfoLocalUser.currentUser?.birthYear != null) {
            binding.spinnerBirthYear.setText(InfoLocalUser.currentUser!!.birthYear, false)
        }
    }

    private fun checkLengthAndValidationName() {
        with(binding) {
            txtInputEdtName.doOnTextChanged { text, _, _, _ ->
                val name = text.toString().trim()
                val errMsgValid = FormValidation.checkValidationName(name)
                val errMsgLength =
                    FormValidation.checkLengthName(name.length > 50 || TextUtils.isEmpty(name))
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

    private fun initResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                    viewModel.imageUri = result.data!!.data
                    binding.imgAvatar.load(viewModel.imageUri) {
                        diskCachePolicy(CachePolicy.DISABLED)
                        memoryCachePolicy(CachePolicy.DISABLED)
                    }
                    val realPath =
                        HandlerFile.getPathFromUri(requireContext(), viewModel.imageUri!!)
                    if (realPath != null) {
                        viewModel.file = File(realPath)
                    } else {
                        Log.e(AppConstants.TAG.UPDATE_ME, "realPath: null ")
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
        val result = checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE)

        if (result == PackageManager.PERMISSION_GRANTED) {
            pickImage()
        } else {
            requireActivity().requestPermissions(
                arrayOf(READ_EXTERNAL_STORAGE),
                1
            )
            showLongToast(AppConstants.MsgInfo.PERMISSION_PICK_PHOTO_NOT_GRANTED)
        }
    }


    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher?.launch(intent)
    }

    private fun handlePreSave() {
        viewModel.birthYear = binding.spinnerBirthYear.text.toString().trim()
        if (binding.spinnerSex.text.toString().trim() == "Nam") {
            viewModel.sex = "male"
        } else {
            viewModel.sex = "female"
        }
        viewModel.name = FormValidation.formatName(binding.txtInputEdtName.text.toString().trim())

        when {
            //check empty
            viewModel.stateErrName -> {
                return focusView(binding.txtInputEdtName, AppConstants.MsgErr.NAME_ERR_MSG)
            }
            TextUtils.isEmpty(viewModel.birthYear) -> {
                return focusView(
                    binding.spinnerBirthYear,
                    AppConstants.MsgErr.BIRTH_YEAR_ERR_MSG
                )
            }
            TextUtils.isEmpty(viewModel.sex) -> {
                return focusView(binding.spinnerSex, AppConstants.MsgErr.GENDER_ERR_MSG)
            }
            //check value change?, if there is not change -> not call api update info
            viewModel.name == InfoLocalUser.currentUser?.name &&
                    viewModel.birthYear == InfoLocalUser.currentUser?.birthYear &&
                    viewModel.sex == InfoLocalUser.currentUser?.sex &&
                    viewModel.imageUri == null -> {
                controllerMain.popBackStack()
                return
            }
        }
        handleSave()
    }

    private fun handleSave() {
        listenUpdateMe()
        updateMe()
    }

    private fun listenUpdateMe() {
        viewModel.dataStateUpdateUser.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    Log.e(AppConstants.TAG.UPDATE_ME, "listenUpdateMe: ${it.message}")
                    showLongToast(HandlerErrRes.checkMsg(it.response?.message))
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    InfoLocalUser.currentUser?.name = viewModel.name
                    InfoLocalUser.currentUser?.birthYear = viewModel.birthYear
                    InfoLocalUser.currentUser?.sex = viewModel.sex
                    InfoLocalUser.currentUser?.avatar =
                        "user-${InfoLocalUser.currentUser?.id}.png"
                    viewModel.isLoading.value = false
                    controllerMain.popBackStack()
                }
            }
        }
    }

    private fun updateMe() {
        val str = AppConstants.BodyRequest
        with(viewModel)
        {
            val name = RequestBody.create(MultipartBody.FORM, name)
            val birthYear = RequestBody.create(MultipartBody.FORM, birthYear)
            val sex = RequestBody.create(MultipartBody.FORM, sex)

            //user can update avatar or not
            if (viewModel.imageUri == null) {
                Log.e(AppConstants.TAG.UPDATE_ME, "imageUri: null ")
                updateMe(name, birthYear, sex, null)
            } else {
                if (viewModel.file == null) {
                    Log.e(AppConstants.TAG.UPDATE_ME, "file: null ")
                    showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    return
                }
                val reqBodyAvt = RequestBody.create(
                    MediaType.parse(requireContext().contentResolver.getType(imageUri!!)!!),
                    file!!
                )
                val avatar =
                    MultipartBody.Part.createFormData(str.AVATAR, file!!.name, reqBodyAvt)
                updateMe(name, birthYear, sex, avatar)
            }
        }
    }
}