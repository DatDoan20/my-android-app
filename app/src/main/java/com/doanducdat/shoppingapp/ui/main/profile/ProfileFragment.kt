package com.doanducdat.shoppingapp.ui.main.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import com.doanducdat.shoppingapp.databinding.FragmentProfileBinding
import com.doanducdat.shoppingapp.module.response.Status
import com.doanducdat.shoppingapp.module.user.Email
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
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
        setUpVerifyEmail()
        listenUpdateEmail()
        binding.imgAvatar.load(InfoUser.currentUser?.getUrlAvatar())
    }

    private fun setUpVerifyEmail() {
//        if (InfoUser.currentUser?.stateVerifyEmail == false) {
//            binding.txtVerifyEmail.visibility = View.VISIBLE
//            binding.txtVerifyEmail.setOnClickListener {
//                doActionClick(AppConstants.ActionClick.VERIFY_EMAIL)
//            }
//        } else {
//            binding.txtVerifyEmail.visibility = View.GONE
//        }
        binding.txtVerifyEmail.setOnClickListener {
            doActionClick(AppConstants.ActionClick.VERIFY_EMAIL)
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
                    InfoUser.currentUser?.email = email
                    InfoUser.currentUser?.stateVerifyEmail = true

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