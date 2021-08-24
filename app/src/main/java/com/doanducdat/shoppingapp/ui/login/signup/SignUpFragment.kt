package com.doanducdat.shoppingapp.ui.login.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentSignUpBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.FormValidation
import com.doanducdat.shoppingapp.utils.MyDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up), MyActionApp {

    private lateinit var binding: FragmentSignUpBinding

    private val controller by lazy {
        (requireActivity().supportFragmentManager.findFragmentById(R.id.container_login) as NavHostFragment).findNavController()
    }
    private val viewModel: SignUpViewModel by viewModels()


    private var stateErrPhone: Boolean = true
    private var stateErrName: Boolean = true
//    private var stateErrEmail: Boolean = true
    private var stateErrPassword: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeListenLoadingForm()
        setUpCheckForm()
        setUpActionClick()

    }

    private fun subscribeListenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            if(it) {
                setStateForm(View.VISIBLE, isEnable = false)
            }else{
                setStateForm(View.GONE, isEnable = true)
            }
        })
    }

    private fun setStateForm(isVisible: Int, isEnable: Boolean) {
        with(binding) {
            spinKitProgressBar.visibility = isVisible
            btnBack.isEnabled = isEnable
            btnSignUp.isEnabled = isEnable
            txtInputEdtPhone.isEnabled = isEnable
            txtInputEdtName.isEnabled = isEnable
            txtInputEdtPassword.isEnabled = isEnable
        }
    }

    private fun setUpCheckForm() {
        checkLengthNumberPhone()
        checkValidationNumberPhone()
        checkLengthAndValidationName()
        checkLengthPassword()
    }

    //<editor-fold desc="Check Form Sign Up">

    private fun checkLengthNumberPhone() {
        binding.txtInputEdtPhone.doOnTextChanged { text, _, _, _ ->
            binding.txtInputLayoutPhone.error =
                FormValidation.checkLengthNumberPhone(text.toString().trim().length > 20)
        }
    }

    private fun checkValidationNumberPhone() {
        binding.ccpSignUp.registerCarrierNumberEditText(binding.txtInputEdtPhone)
        binding.ccpSignUp.setPhoneNumberValidityChangeListener {
            // if phone valid, it = true -> stateErrPhone = false
            stateErrPhone = !it
            binding.imgCheckPhone.setImageResource(FormValidation.checkValidationNumberPhone(it))
        }
    }

    private fun checkLengthAndValidationName() {
        binding.txtInputEdtName.doOnTextChanged { text, _, _, _ ->
            val errMsgValid = FormValidation.checkValidationName(text.toString())
            val errMsgLength = FormValidation.checkLengthName(text.toString().trim().length > 50)
            when {
                errMsgLength == null && errMsgValid == null -> { // not any err
                    stateErrName = false
                    binding.txtInputLayoutName.error = null
                }
                errMsgLength != null -> { // err length
                    stateErrName = true
                    binding.txtInputLayoutName.error = errMsgLength
                }
                errMsgValid != null -> { // err contains number or special character
                    stateErrName = true
                    binding.txtInputLayoutName.error = errMsgValid
                }
            }
        }
    }

//    private fun checkValidationEmail() {
//        binding.txtInputEdtEmail.doAfterTextChanged {
//            val errMsgEmail = FormValidation.checkValidationEmail(it.toString())
//            stateErrEmail = errMsgEmail != null
//            binding.txtInputLayoutEmail.error = errMsgEmail
//        }
//    }

    private fun checkLengthPassword() {
        binding.txtInputLayoutPassword.errorIconDrawable = null
        binding.txtInputEdtPassword.doOnTextChanged { text, _, _, _ ->
            stateErrPassword = text!!.length < 8 || text.contains(" ")
            binding.txtInputLayoutPassword.error =
                FormValidation.checkLengthPassword(stateErrPassword)
        }
    }
    //</editor-fold>


    private fun setUpActionClick() {
        binding.btnSignUp.setOnClickListener {
            doActionClick(AppConstants.ActionClick.SIGN_UP)
        }
        binding.btnBack.setOnClickListener {
            doActionClick(AppConstants.ActionClick.NAV_SIGN_IN)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.SIGN_UP -> {
                checkValidForm()
            }
            AppConstants.ActionClick.NAV_SIGN_IN -> {
                controller.navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            }
        }
    }

    //<editor-fold desc="Check Form Sign Up">
    private fun checkValidForm() {
        when {
            stateErrPhone -> {
                return focusView(binding.txtInputEdtPhone, AppConstants.MsgError.PHONE_ERR_MSG)
            }
            stateErrName -> {
                return focusView(binding.txtInputEdtName, AppConstants.MsgError.NAME_ERR_MSG)
            }
//            stateErrEmail -> {
//                return focusView(binding.txtInputEdtEmail, AppConstants.MsgError.EMAIL_ERR_MSG)
//            }
            stateErrPassword -> {
                return focusView(
                    binding.txtInputEdtPassword,
                    AppConstants.MsgError.PASSWORD_ERR_MSG
                )
            }
        }
        signUp()
    }

    private fun focusView(requestedView: View, msg: String) {
        requestedView.requestFocus()
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
    //</editor-fold>

    private fun signUp() {
        viewModel.isLoading.value = true
        val phoneNumberWithCountryCode =
            binding.ccpSignUp.selectedCountryCodeWithPlus + binding.txtInputEdtPhone.text.toString()
        val phoneNumber = binding.txtInputEdtPhone.text.toString()
        val formattedName = FormValidation.formatName(binding.txtInputEdtName.text.toString())
//        val email = binding.txtInputEdtEmail.text.toString().trim()
        val password = binding.txtInputEdtPassword.text.toString()
        binding.txtInputEdtName.setText(formattedName)

        val callbackResultGenerateOTP = callbackResultGenerateOTP(formattedName, password)

        viewModel.generateOTP(
            phoneNumberWithCountryCode,
            phoneNumber,
            callbackResultGenerateOTP,
            requireActivity()
        )
    }

    private fun callbackResultGenerateOTP(
        formattedName: String,
        password: String
    ): MyPhoneAuth.ResultGenerateOTP {
        return object : MyPhoneAuth.ResultGenerateOTP {
            override fun onCodeSentSuccess(
                verificationId: String,
                phoneNumberWithCountryCode: String,
                phoneNumber: String,
                msg: String
            ) {
                val bundle: Bundle = bundleOf(
                    "PHONE_COUNTRY_WITH_PLUSH" to phoneNumberWithCountryCode,
                    "PHONE" to phoneNumber,
                    "NAME" to formattedName,
                    "PASSWORD" to password,
                    "VERIFICATION_ID" to verificationId
                )
                viewModel.isLoading.value = false
                controller.navigate(R.id.verifyOTPFragment, bundle)
            }

            override fun onCodeSentFailed(msg: String) {
                val onClick: () -> Unit = {}
                MyDialog.build(requireContext(), msg, onClick)
                MyDialog.show()
                viewModel.isLoading.value = false
            }
        }
    }
}