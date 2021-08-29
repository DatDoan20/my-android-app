package com.doanducdat.shoppingapp.ui.login.signup

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentVerifyOTPBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.dialog.MyBasicDialog
import com.doanducdat.shoppingapp.utils.response.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOTPFragment : BaseFragment<FragmentVerifyOTPBinding>(), MyActionApp {

    private val dialog: MyBasicDialog by lazy { MyBasicDialog(requireContext()) }
    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_login) as NavHostFragment).findNavController()
    }
    private val viewModel: SignUpViewModel by viewModels()

    private var verificationId: String? = null
    private var otp: String? = null
    private lateinit var callbackVerifyOTP: MyPhoneAuth.VerifyOTP

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVerifyOTPBinding = FragmentVerifyOTPBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataSignUpFragment()
        subscribeListenLoadingForm()
        subscribeListenSignUp()
        setUpActionClick()
        callbackVerifyOTP = callbackVerifyOTP()
    }

    private fun getDataSignUpFragment() {
        val bundle: Bundle? = arguments
        if (bundle != null) {
            verificationId = bundle.getString("VERIFICATION_ID", null)
            viewModel.phone = bundle.getString("PHONE", null)
            viewModel.name = bundle.getString("NAME", null)
            viewModel.email = bundle.getString("NAME", null)
            viewModel.password = bundle.getString("PASSWORD", null)
        }
    }

    private fun subscribeListenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            with(binding) {
                setStateViews(!it, btnVerifyOtp)
                if (it) {
                    setStateProgressBar(View.VISIBLE, spinKitProgressBar)
                } else {
                    setStateProgressBar(View.GONE, spinKitProgressBar)
                }
            }
        })
    }

    private fun subscribeListenSignUp() {
        viewModel.dataState.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    dialog.setText(it.message!!)
                    dialog.show()
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), it.response?.message, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.isLoading.value = false
                    controller.navigate(VerifyOTPFragmentDirections.actionVerifyOTPFragmentToSignInFragment())
                }
            }
        })
    }

    private fun setUpActionClick() {
        binding.btnVerifyOtp.setOnClickListener {
            doActionClick(AppConstants.ActionClick.VERIFY_OTP)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.VERIFY_OTP -> {
                verifyOTP()
            }
        }
    }

    private fun verifyOTP() {
        if (checkOTP() && verificationId != null && otp != null) {
            viewModel.isLoading.value = true
            viewModel.verifyOTP(verificationId!!, otp!!, requireActivity(), callbackVerifyOTP)
        }
    }

    private fun checkOTP(): Boolean {
        otp = binding.pinViewOtp.text.toString()
        if (TextUtils.isEmpty(otp)) {
            binding.pinViewOtp.error = AppConstants.PhoneAuth.OTP_ERR_MSG_EMPTY
            return false
        } else if (otp!!.length < 6) {
            binding.pinViewOtp.error = AppConstants.PhoneAuth.OTP_ERR_MSG_NOT_ENOUGH
            return false
        }
        return true
    }

    private fun callbackVerifyOTP(): MyPhoneAuth.VerifyOTP {
        return object : MyPhoneAuth.VerifyOTP {
            //Match OTP - send API signUpUser to server
            override fun onVerifySuccess() {
                signUpUser()
            }

            override fun onVerifyFailed(msg: String) {
                dialog.setText(msg)
                dialog.show()
                viewModel.isLoading.value = false
            }
        }
    }

    private fun signUpUser() {
        viewModel.signUpUser()
    }
}