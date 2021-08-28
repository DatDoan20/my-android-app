package com.doanducdat.shoppingapp.ui.login.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentSignInBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.dialog.MyBasicDialog
import com.doanducdat.shoppingapp.utils.response.Status
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in), MyActionApp {

    private lateinit var binding: FragmentSignInBinding
    private val navHostFragment by lazy {
        requireActivity().supportFragmentManager.findFragmentById(R.id.container_login) as NavHostFragment
    }
    private val controller by lazy { navHostFragment.findNavController() }
    private val viewModel: SignInViewModel by viewModels()

    private var stateErrPhone: Boolean = true
    private var stateErrPassword: Boolean = true
    private val dialog: MyBasicDialog by lazy { MyBasicDialog(requireContext()) }

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
        subscribeListenLoadingForm()
        subscribeListenSignIn()

        checkLengthNumberPhone()
        checkLengthPassword()
        checkValidationNumberPhone()
        setUpActionClick()

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

    private fun setStateForm(isVisible: Int, isEnable: Boolean) {
        with(binding) {
            spinKitProgressBar.visibility = isVisible
            btnSignIn.isEnabled = isEnable
            txtInputEdtPhone.isEnabled = isEnable
            txtInputEdtPassword.isEnabled = isEnable
        }
    }

    private fun subscribeListenSignIn() {
        viewModel.dataState.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    dialog.setText(it.message!!)
                    dialog.show()
                    Log.e("TAG", "subscribeListenSignIn: ${it.message}", )
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    Log.e("TAG", "subscribeListenSignIn: ${it.response?.token}", )
                    viewModel.isLoading.value = false
                    //start activity main + save token
                }
            }
        })
    }

    //<editor-fold desc="Check Form Sign In">
    private fun checkLengthNumberPhone() {
        binding.txtInputEdtPhone.doOnTextChanged { text, _, _, _ ->
            binding.txtInputLayoutPhone.error =
                FormValidation.checkLengthNumberPhone(text.toString().trim().length > 20)
        }
    }

    private fun checkValidationNumberPhone() {
        with(binding) {
            ccpSignIn.registerCarrierNumberEditText(txtInputEdtPhone)
            ccpSignIn.setPhoneNumberValidityChangeListener {
                // if phone valid, it = true -> stateErrPhone = false
                stateErrPhone = !it
                imgCheckPhone.setImageResource(FormValidation.checkValidationNumberPhone(it))
            }
        }
    }

    private fun checkLengthPassword() {
        with(binding) {
            txtInputLayoutPassword.errorIconDrawable = null
            txtInputEdtPassword.doOnTextChanged { text, _, _, _ ->
                stateErrPassword = text!!.length < 8 || text.contains(" ")
                txtInputLayoutPassword.error = FormValidation.checkLengthPassword(stateErrPassword)
            }
        }
    }
    //</editor-fold>


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
            AppConstants.ActionClick.SIGN_IN -> {
                checkValidForm()
            }
            AppConstants.ActionClick.NAV_SIGN_UP -> {
                controller.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
        }
    }

    private fun checkValidForm() {
        when {
            stateErrPhone -> {
                return focusView(binding.txtInputEdtPhone, AppConstants.MsgErr.PHONE_ERR_MSG)
            }
            stateErrPassword -> {
                return focusView(binding.txtInputEdtPassword, AppConstants.MsgErr.PASSWORD_ERR_MSG)
            }
        }
        signIn()
    }

    private fun focusView(requestedView: View, msg: String) {
        requestedView.requestFocus()
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun signIn() {
        viewModel.isLoading.value = true
        viewModel.phone = binding.txtInputEdtPhone.text.toString()
        viewModel.password = binding.txtInputEdtPassword.text.toString()
        viewModel.signInUser()
    }
}