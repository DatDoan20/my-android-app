package com.doanducdat.shoppingapp.ui.login.signup

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentVerifyOTPBinding
import com.doanducdat.shoppingapp.module.UserSignUp
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.MyDialog
import com.doanducdat.shoppingapp.utils.response.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOTPFragment : Fragment(R.layout.fragment_verify_o_t_p), MyActionApp {

    private lateinit var binding: FragmentVerifyOTPBinding

    private val controller by lazy {
        (requireActivity().supportFragmentManager.findFragmentById(R.id.container_login) as NavHostFragment).findNavController()
    }
    private val viewModel: SignUpViewModel by viewModels()

    private var verificationId: String? = null
    private var otp: String? = null
    private lateinit var userSignUp: UserSignUp
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataSignUpFragment()
        subscribeListenLoadingForm()
        setUpActionClick()
    }


    private fun getDataSignUpFragment() {
        val bundle: Bundle? = arguments
        if (bundle != null) {
            verificationId = bundle.getString("VERIFICATION_ID", null)
            userSignUp = UserSignUp(
                bundle.getString("PHONE", ""),
                bundle.getString("NAME", ""),
                bundle.getString("PASSWORD", "")
            )
        }
    }

    private fun setStateForm(progressBar: Int, isEnable: Boolean) {
        binding.spinKitProgressBar.visibility = progressBar
        binding.btnVerifyOtp.isEnabled = isEnable
    }

    private fun subscribeListenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                setStateForm(View.VISIBLE, isEnable = false)
            } else {
                setStateForm(View.GONE, isEnable = true)
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
            val callbackVerifyOTP = object : MyPhoneAuth.VerifyOTP {
                //Match OTP - send API
                override fun onVerifySuccess(msg: String) {

                    viewModel.dataState.observe(viewLifecycleOwner, {
                        when (it.status) {
                            Status.LOADING -> {
                                viewModel.isLoading.value = true
                            }
                            Status.ERROR -> {
                                viewModel.isLoading.value = false
                                val onClick: () -> Unit = {}
                                MyDialog.build(requireContext(), it.message!!, onClick)
                                MyDialog.show()
                            }
                            Status.SUCCESS -> {
                                Log.e("TAG", "onVerifySuccess: ${it.response}")
                            }
                        }
                    })
                    viewModel.signUpUser(userSignUp)
//                    controller.navigate(VerifyOTPFragmentDirections.actionVerifyOTPFragmentToSignInFragment())
                }

                override fun onVerifyFailed(msg: String) {
                    val onClick: () -> Unit = {}
                    MyDialog.build(requireContext(), msg, onClick)
                    MyDialog.show()
                    viewModel.isLoading.value = false
                }
            }
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
}