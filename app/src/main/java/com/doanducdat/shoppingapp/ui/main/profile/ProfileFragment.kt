package com.doanducdat.shoppingapp.ui.main.profile

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import coil.request.CachePolicy
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentProfileBinding
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.model.user.Email
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.dialog.MyVerifyEmailDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(), MyActionApp {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    private val verifyEmailDialog: MyVerifyEmailDialog by lazy { MyVerifyEmailDialog(requireActivity()) }
    val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInfo()
        setUpActionClick()
        listenUpdateEmail()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpInfo() {
        binding.imgAvatar.load(InfoLocalUser.currentUser?.getUrlAvatar()) {
            diskCachePolicy(CachePolicy.DISABLED)
            memoryCachePolicy(CachePolicy.DISABLED)
        }
        binding.txtEmail.text = InfoLocalUser.currentUser?.email ?: ""
        binding.txtName.text = InfoLocalUser.currentUser?.name ?: ""
        var sex = ""
        var birthYear = ""
        sex = if (InfoLocalUser.currentUser?.sex != null) {
            if (InfoLocalUser.currentUser?.sex == "male") "Nam" else "Nữ"
        } else {
            "..."
        }
        birthYear = if (InfoLocalUser.currentUser?.birthYear != null) {
            InfoLocalUser.currentUser?.birthYear!!
        } else {
            "..."
        }
        binding.txtSexAndBirthYear.text = "Giới tính: $sex - Ngày sinh: $birthYear"
    }

    private fun setUpVerifyEmail() {
        if (InfoLocalUser.currentUser?.stateVerifyEmail == false) {
            binding.txtVerifyEmail.visibility = View.VISIBLE
            binding.txtVerifyEmail.setOnClickListener {
                doActionClick(AppConstants.ActionClick.VERIFY_EMAIL)
            }
        } else {
            binding.txtVerifyEmail.visibility = View.GONE
        }
    }

    private fun setUpActionClick() {
        setUpVerifyEmail()
        binding.layoutEditProfileItem.setOnClickListener {
            doActionClick(AppConstants.ActionClick.NAV_EDIT_PROFILE)
        }
        binding.layoutManageOrderItem.setOnClickListener {
            doActionClick(AppConstants.ActionClick.NAV_MANAGE_ORDER)
        }
        binding.layoutReviewPurchasedProduct.setOnClickListener {
            doActionClick(AppConstants.ActionClick.REVIEW_PURCHASED_PRODUCT)
        }
        binding.layoutSignOutItem.setOnClickListener {

        }
    }


    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.VERIFY_EMAIL -> {
                verifyEmailDialog.show()
                verifyEmailDialog.myOnListenUpdateEmail { email ->
                    listenUpdateEmail()
                    viewModel.updateEmail(Email(email))
                }
            }
            AppConstants.ActionClick.NAV_MANAGE_ORDER -> {
                controllerMain.navigate(R.id.orderManagementFragment)
            }
            AppConstants.ActionClick.REVIEW_PURCHASED_PRODUCT -> {
                controllerMain.navigate(R.id.purchasedProductFragment)
            }
            AppConstants.ActionClick.NAV_EDIT_PROFILE -> {
                val bitmapAvatar = (binding.imgAvatar.drawable as BitmapDrawable).bitmap
                controllerMain.navigate(R.id.profileUpdateFragment )
            }

        }
    }

    private fun listenUpdateEmail() {
        viewModel.dataStateUpdateEmail.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    verifyEmailDialog.setStateDialog(false)
                    verifyEmailDialog.setStateProgressBar(View.VISIBLE)
                }
                Status.ERROR -> {
                    Log.e(AppConstants.TAG.VERIFY_EMAIL, "listenUpdateEmail: ${it.message}")
                    showLongToast(AppConstants.MsgErr.GENERIC_ERR_MSG)
                    verifyEmailDialog.setStateDialog(true)
                    verifyEmailDialog.setStateProgressBar(View.GONE)
                }
                Status.SUCCESS -> {
                    Log.d(
                        AppConstants.TAG.VERIFY_EMAIL,
                        "listenUpdateEmail: ${it.response!!.message}"
                    )
                    val email = it.response.data
                    InfoLocalUser.currentUser?.email = email
                    InfoLocalUser.currentUser?.stateVerifyEmail = true

                    binding.txtEmail.text = email
                    binding.txtVerifyEmail.visibility = View.GONE

                    verifyEmailDialog.setStateDialog(true)
                    verifyEmailDialog.setStateProgressBar(View.GONE)
                    verifyEmailDialog.dismiss()

                    showLongToast(AppConstants.EmailAuth.UPDATE_EMAIL_SUCCESS)
                }
            }
        })
    }
}