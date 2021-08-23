package com.doanducdat.shoppingapp.ui.login.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentSignInBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.utils.AppConstants


class SignInFragment : Fragment(R.layout.fragment_sign_in), MyActionApp {

    private lateinit var binding: FragmentSignInBinding
    private var validationPhone: Boolean = false
    private var validationPassword: Boolean = false
    private val navHostFragment by lazy {
        requireActivity().supportFragmentManager.findFragmentById(R.id.container_login) as NavHostFragment
    }
    private val controller by lazy { navHostFragment.findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLengthNumberPhone()
        checkLengthPassword()
        checkValidationNumberPhone()
        setUpActionClick()

    }

    private fun checkLengthNumberPhone() {
        binding.txtInputEdtPhone.doOnTextChanged { text, _, _, _ ->
            if (text!!.length > 20) {
                binding.txtInputLayoutPhone.error = AppConstants.MsgError.PHONE_ERR_LENGTH
            } else if (text.length < 21) {
                binding.txtInputLayoutPhone.error = null
            }
        }
    }

    private fun checkLengthPassword() {
        binding.txtInputLayoutPassword.errorIconDrawable = null
        binding.txtInputEdtPassword.doOnTextChanged { text, _, _, _ ->
            if (text!!.length < 8 || text.contains(" ")) {
                validationPassword = false
                binding.txtInputLayoutPassword.error = AppConstants.MsgError.PASSWORD_ERR_LENGTH
            } else {
                validationPassword = true
                binding.txtInputLayoutPassword.error = null
            }
        }
    }

    private fun checkValidationNumberPhone() {
        binding.ccpSignIn.registerCarrierNumberEditText(binding.txtInputEdtPhone)
        binding.ccpSignIn.setPhoneNumberValidityChangeListener {
            if (it) {
                validationPhone = true
                binding.imgCheckPhone.setImageResource(R.drawable.ic_valid)
            } else {
                validationPhone = false
                binding.imgCheckPhone.setImageResource(R.drawable.ic_unvalid)
            }
        }
    }

    private fun setUpActionClick() {
        binding.btnSignIn.setOnClickListener {
            doActionClick(AppConstants.ActionClick.SIGN_IN)
        }
        binding.txtSignUp.setOnClickListener {
            doActionClick(AppConstants.ActionClick.NAV_SIGN_UP)
        }
        binding.txtForgotPassword.setOnClickListener {
            doActionClick(AppConstants.ActionClick.NAV_FORGOT)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.SIGN_IN-> {
                login()
            }
            AppConstants.ActionClick.NAV_SIGN_UP -> {
                controller.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
        }
    }

    private fun login() {
        if(!validationPhone){
            Toast.makeText(requireContext(), AppConstants.MsgError.PHONE_ERR_MSG, Toast.LENGTH_SHORT).show()
            binding.txtInputEdtPhone.requestFocus()
        }
        if(!validationPassword){
            Toast.makeText(requireContext(), AppConstants.MsgError.PASSWORD_ERR_MSG, Toast.LENGTH_SHORT).show()
            binding.txtInputEdtPassword.requestFocus()
        }
        
    }
}