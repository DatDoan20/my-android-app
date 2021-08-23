package com.doanducdat.shoppingapp.ui.login.signup

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentVerifyOTPBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.utils.AppConstants
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
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
        setUpActionClick()
    }

    private fun getDataSignUpFragment() {
        val bundle: Bundle? = arguments
        if (bundle != null) {
            verificationId = bundle.getString("VERIFICATION_ID", null)
        }
    }

    private fun setUpActionClick() {
        binding.btnVerifyOtp.setOnClickListener {
            doActionClick(AppConstants.ActionClick.VERIFY_OTP)
        }
    }

    private fun checkOTP(): Boolean {
        otp = binding.pinViewOtp.text.toString()
        if (TextUtils.isEmpty(otp)) {
            binding.pinViewOtp.error = AppConstants.MsgError.OTP_ERR_MSG_EMPTY
            return false
        } else if (otp!!.length < 6) {
            binding.pinViewOtp.error = AppConstants.MsgError.OTP_ERR_MSG_NOT_ENOUGH
            return false
        }
        return true
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

            val callbackVerifyOTP = object : MyPhoneAuth.VerifyOTP {
                override fun setVerifyOTP() {
                    //create user use API
                    controller.navigate(VerifyOTPFragmentDirections.actionVerifyOTPFragmentToSignInFragment())
                }
            }
            viewModel.verifyOTP(
                verificationId!!,
                otp!!,
                requireActivity(),
                requireContext(),
                callbackVerifyOTP
            )
        }
    }


}