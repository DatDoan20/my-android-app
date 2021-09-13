package com.doanducdat.shoppingapp.ui.login.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentSignInBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.ui.main.MainActivity
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.MyDataStore
import com.doanducdat.shoppingapp.utils.dialog.MyBasicDialog
import com.doanducdat.shoppingapp.module.response.Status
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(), MyActionApp {

    @Inject
    lateinit var myDataStore: MyDataStore

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_login) as NavHostFragment).findNavController()
    }

    private val viewModel: SignInViewModel by viewModels()

    private val dialog: MyBasicDialog by lazy { MyBasicDialog(requireContext()) }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignInBinding = FragmentSignInBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeListenLoadingForm()
        subscribeListenSignIn()

        setUpCheckForm()
        setUpActionClick()

    }

    private fun subscribeListenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            with(binding) {
                setStateViews(!it, btnSignIn, txtInputEdtPhone, txtInputEdtPassword)
                if (it) {
                    setStateProgressBar(View.VISIBLE, spinKitProgressBar)
                } else {
                    setStateProgressBar(View.GONE, spinKitProgressBar)
                }
            }
        })
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
//                    Log.e("TAG", "subscribeListenSignIn: ${it.message}")
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    //write token + navigate mainActivity
                    CoroutineScope(Dispatchers.Main).launch {
                        myDataStore.writeJwt(it.response!!.token)
                        InfoUser.token.append(it.response.token)
//                        Log.e("TAG", "token: ${InfoUser.token}")
                        viewModel.isLoading.value = false
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        })
    }

    private fun setUpCheckForm() {
        checkLengthNumberPhone()
        checkLengthPassword()
        checkValidationNumberPhone()
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
                viewModel.stateErrPhone = !it
                imgCheckPhone.setImageResource(FormValidation.checkValidationNumberPhone(it))
            }
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
                handleSignIn()
            }
            AppConstants.ActionClick.NAV_SIGN_UP -> {
                controller.navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
        }
    }

    private fun handleSignIn() {
        //handle result checking valid from
        when {
            viewModel.stateErrPhone -> {
                return focusView(binding.txtInputEdtPhone, AppConstants.MsgErr.PHONE_ERR_MSG)
            }
            viewModel.stateErrPassword -> {
                return focusView(binding.txtInputEdtPassword, AppConstants.MsgErr.PASSWORD_ERR_MSG)
            }
        }
        signIn()
    }

    private fun signIn() {
        viewModel.isLoading.value = true
        viewModel.phone = binding.txtInputEdtPhone.text.toString()
        viewModel.password = binding.txtInputEdtPassword.text.toString()
        viewModel.signInUser()
    }
}