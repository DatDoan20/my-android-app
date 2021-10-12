package com.doanducdat.shoppingapp.ui.login.signin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentSignInBinding
import com.doanducdat.shoppingapp.model.response.Status
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.ui.main.MainActivity
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoLocalUser
import com.doanducdat.shoppingapp.utils.dialog.MyBasicDialog
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(), MyActionApp {
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
        listenLoadingForm()
        listenLoadMe()

        //it work if localToken is exist and not expired
        autoSignInWithToken()

        // if localToken = null or localToken expired
        listenSignIn()

        setUpCheckForm()
        setUpActionClick()

    }

    private fun listenLoadingForm() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            with(binding) {
                setStateEnableViews(
                    !it, btnSignIn, txtInputEdtPhone, txtInputEdtPassword,
                    txtSignUp, txtForgotPassword
                )
                if (it) {
                    setStateVisibleView(View.VISIBLE, spinKitProgressBar)
                } else {
                    setStateVisibleView(View.GONE, spinKitProgressBar)
                }
            }
        })
    }

    private fun listenLoadMe() {
        viewModel.dataStateUser.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> viewModel.isLoading.value = true
                Status.ERROR -> {
                    //default is err network, show generic msg
                    var errMsg = AppConstants.MsgErr.MSG_ERR_AUTO_SIGN_IN

                    // another err (network err can not response json)
                    if (it.response != null && it.response.error == AppConstants.Response.ERR_JWT_EXPIRED) {
                        errMsg = AppConstants.MsgErr.MSG_ERR_JWT_EXPIRED
                    }
                    //auto sign in fail -> clear localToken
                    InfoLocalUser.localToken = StringBuffer(AppConstants.HeaderRequest.BEARER).append(" ")

                    showLongToast(errMsg)
                    Log.e(AppConstants.TAG.LOAD_ME, "listenLoadMe: ${it.message}")
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    InfoLocalUser.currentUser = it.response!!.data
                    viewModel.isLoading.value = false
                    startMain()
                }
            }
        })
    }

    private fun listenSignIn() {
        viewModel.dataStateSignIn.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> viewModel.isLoading.value = true
                Status.ERROR -> {
                    //err network
                    var msg = AppConstants.MsgErr.GENERIC_ERR_MSG
                    //err incorrect pass or phone
                    if (it.response != null) {
                        if (it.response.message.startsWith(AppConstants.Response.ERR_INCORRECT_PHONE_OR_PASS)) {
                            msg = AppConstants.MsgErr.MSG_ERR_INCORRECT_PHONE_OR_PASS
                        }
                    }
                    dialog.setText(msg)
                    dialog.show()
                    Log.e(AppConstants.TAG.SIGN_IN, "ListenSignIn Fail: ${it.message}")
                    viewModel.isLoading.value = false
                }
                Status.SUCCESS -> {
                    //write localToken + loadMe
                    viewModel.writeTokenLocal(it.response!!.token)
                }
            }
        })
    }

    private fun autoSignInWithToken() {
        viewModel.isLoading.value = true
        viewModel.token.observe(viewLifecycleOwner, { token ->
            token?.let { checkTokenToLoadMe(token) }
        })
        viewModel.getTokenLocal()
    }

    private fun checkTokenToLoadMe(token: String) {
        //localToken is exist, assign localToken before call loadMe()
        if (!TextUtils.isEmpty(token)) {
            Log.d(AppConstants.TAG.LOAD_ME, "localToken in datastore is $token")
            InfoLocalUser.localToken.append(token)
            viewModel.loadMe()
        } else {
            Log.d(AppConstants.TAG.LOAD_ME, "localToken is empty in datastore, localToken is $token")
            viewModel.isLoading.value = false
        }
    }

    private fun startMain() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
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
        with(binding) {
            setStateEnableViews(false, btnSignIn, txtSignUp, txtForgotPassword)
        }

        viewModel.phone = binding.txtInputEdtPhone.text.toString()
        viewModel.password = binding.txtInputEdtPassword.text.toString()
        viewModel.signInUser()
    }
}