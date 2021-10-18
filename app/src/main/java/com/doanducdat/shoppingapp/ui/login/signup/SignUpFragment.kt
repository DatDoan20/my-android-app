package com.doanducdat.shoppingapp.ui.login.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentSignUpBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.myinterface.MyPhoneAuth
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.dialog.MyBasicDialog
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(), MyActionApp {

    val dialog: MyBasicDialog by lazy { MyBasicDialog(requireContext()) }

    private val viewModel: SignUpViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeListenLoadingForm()
        setUpCheckForm()
        setUpActionClick()
    }

    private fun subscribeListenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            with(binding) {
                setStateEnableViews(
                    !it, btnBack, btnSignUp, txtInputEdtPhone, txtInputEdtEmail,
                    txtInputEdtName, txtInputEdtPassword
                )
                if (it) {
                    setStateVisibleView(View.VISIBLE, spinKitProgressBar)
                } else {
                    setStateVisibleView(View.GONE, spinKitProgressBar)
                }
            }
        })
    }

    private fun setUpCheckForm() {
        checkLengthNumberPhone()
        checkValidationNumberPhone()
        checkLengthAndValidationName()
        checkValidationEmail()
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
        with(binding) {
            ccpSignUp.registerCarrierNumberEditText(txtInputEdtPhone)
            ccpSignUp.setPhoneNumberValidityChangeListener {
                // if phone valid, it = true -> stateErrPhone = false
                viewModel.stateErrPhone = !it
                imgCheckPhone.setImageResource(FormValidation.checkValidationNumberPhone(it))
            }
        }
    }

    private fun checkLengthAndValidationName() {
        with(binding) {
            txtInputEdtName.doOnTextChanged { text, _, _, _ ->
                val errMsgValid = FormValidation.checkValidationName(text.toString().trim())
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

    private fun checkValidationEmail() {
        binding.txtInputEdtEmail.doAfterTextChanged {
            val errMsgEmail = FormValidation.checkValidationEmail(it.toString())
            viewModel.stateErrEmail = errMsgEmail != null
            binding.txtInputLayoutEmail.error = errMsgEmail
        }
    }

    private fun checkLengthPassword() {
        with(binding) {
            txtInputLayoutPassword.errorIconDrawable = null
            txtInputEdtPassword.doOnTextChanged { text, _, _, _ ->
                viewModel.stateErrPassword = text!!.length < 8 || text.contains(" ")
                txtInputLayoutPassword.error =
                    FormValidation.checkLengthPassword(viewModel.stateErrPassword)
            }
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
                handleSendOTP()
            }
            AppConstants.ActionClick.NAV_SIGN_IN -> {
                controllerLogin.navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            }
        }
    }

    //<editor-fold desc="CLick button Sign Up">
    private fun handleSendOTP() {
        when {
            viewModel.stateErrPhone -> {
                return focusView(binding.txtInputEdtPhone, AppConstants.MsgErr.PHONE_ERR_MSG)
            }
            viewModel.stateErrName -> {
                return focusView(binding.txtInputEdtName, AppConstants.MsgErr.NAME_ERR_MSG)
            }
            viewModel.stateErrEmail -> {
                return focusView(binding.txtInputEdtEmail, AppConstants.MsgErr.EMAIL_ERR_MSG)
            }
            viewModel.stateErrPassword -> {
                return focusView(binding.txtInputEdtPassword, AppConstants.MsgErr.PASSWORD_ERR_MSG)
            }
        }
        sendOTP()
    }

    //</editor-fold>

    private fun sendOTP() {
        viewModel.isLoading.value = true
        val phoneNumberWithCountryCode =
            binding.ccpSignUp.selectedCountryCodeWithPlus + binding.txtInputEdtPhone.text.toString()
        val phoneNumber = binding.txtInputEdtPhone.text.toString()
        val formattedName = FormValidation.formatName(binding.txtInputEdtName.text.toString())
        val email = binding.txtInputEdtEmail.text.toString().trim()
        val password = binding.txtInputEdtPassword.text.toString()
        binding.txtInputEdtName.setText(formattedName)

        val callbackResultGenerateOTP = callbackResultGenerateOTP(formattedName, password, email)

        viewModel.generateOTP(
            phoneNumberWithCountryCode,
            phoneNumber,
            callbackResultGenerateOTP,
            requireActivity()
        )
    }

    private fun callbackResultGenerateOTP(
        formattedName: String,
        password: String,
        email:String
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
                    "EMAIL" to email,
                    "PASSWORD" to password,
                    "VERIFICATION_ID" to verificationId
                )
                viewModel.isLoading.value = false
                controllerLogin.navigate(R.id.verifyOTPFragment, bundle)
            }

            override fun onCodeSentFailed(msg: String) {
                dialog.setText(msg)
                dialog.show()
                viewModel.isLoading.value = false
            }
        }
    }
}