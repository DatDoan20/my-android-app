package com.doanducdat.shoppingapp.ui.login.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentSignUpBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.FormValidation
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
    private var stateErrEmail: Boolean = true
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
        setUpCheckForm()
        setUpActionClick()
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

    private fun checkValidationEmail() {
        binding.txtInputEdtEmail.doAfterTextChanged {
            val errMsgEmail = FormValidation.checkValidationEmail(it.toString())
            stateErrEmail = errMsgEmail != null
            binding.txtInputLayoutEmail.error = errMsgEmail
        }
    }

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
        binding.imgBack.setOnClickListener {
            controller.navigate(R.id.verifyOTPFragment)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.SIGN_UP -> {
                checkValidForm()
            }
        }
    }

    private fun checkValidForm() {
        when {
            stateErrPhone -> {
                return requestFocusForm(
                    binding.txtInputEdtPhone,
                    AppConstants.MsgError.PHONE_ERR_MSG
                )
            }
            stateErrName -> {
                return requestFocusForm(
                    binding.txtInputEdtName,
                    AppConstants.MsgError.NAME_ERR_MSG_EMPTY
                )
            }
            stateErrEmail -> {
                return requestFocusForm(
                    binding.txtInputEdtEmail,
                    AppConstants.MsgError.EMAIL_ERR_MSG
                )
            }
            stateErrPassword -> {
                return requestFocusForm(
                    binding.txtInputEdtPassword,
                    AppConstants.MsgError.PASSWORD_ERR_MSG
                )
            }
        }
        signUp()
    }

    private fun requestFocusForm(view: View, msg: String) {
        view.requestFocus()
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun signUp() {
        val formattedName = FormValidation.formatName(binding.txtInputEdtName.text.toString())

        binding.txtInputEdtName.setText(formattedName)
    }


}