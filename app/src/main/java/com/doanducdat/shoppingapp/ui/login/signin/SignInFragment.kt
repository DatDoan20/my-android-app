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
import com.doanducdat.shoppingapp.module.response.Status
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.ui.main.MainActivity
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.MyDataStore
import com.doanducdat.shoppingapp.utils.dialog.MyBasicDialog
import com.doanducdat.shoppingapp.utils.validation.FormValidation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
        listenLoadMe()

        //it work if token is exist and not expired
        autoSignInWithToken()

        // if token = null or token expired
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

    private fun autoSignInWithToken() {
        viewModel.isLoading.value = true
        with(binding) {
            setStateViews(false, btnSignIn, txtSignUp, txtForgotPassword)
        }

        //if token still use ok
        CoroutineScope(Dispatchers.Main).launch {
            val token = myDataStore.userJwtFlow.first().trim()
            delay(1000)

            //token is exist, assign token before call loadMe()
            if (!TextUtils.isEmpty(token)) {
                Log.d(AppConstants.TAG.LOAD_ME, "token in datastore is $token")

                //assign token to call loadm=Me, if err -> clear token (token is expired or err network)
                InfoUser.token.append(token)
                viewModel.loadMe()
            } else {
                Log.d(AppConstants.TAG.LOAD_ME, "token is empty in datastore")
                viewModel.isLoading.value = false
                with(binding) {
                    setStateViews(true, btnSignIn, txtSignUp, txtForgotPassword)
                }
            }
        }
    }

    private fun listenLoadMe() {
        viewModel.dataStateUser.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    viewModel.isLoading.value = true
                }
                Status.ERROR -> {
                    //default is err network, show generic msg
                    var errMsg = AppConstants.MsgErr.MSG_ERR_AUTO_SIGN_IN

                    // another err (network err can not response json)
                    if (it.response != null) {
                        if (it.response.error == AppConstants.Response.ERR_JWT_EXPIRED) {
                            errMsg = AppConstants.MsgErr.MSG_ERR_JWT_EXPIRED
                        }
                    }

                    //auto sign in fail -> clear token
                    InfoUser.token =
                        StringBuffer(AppConstants.HeaderRequest.BEARER).append(" ")

                    showLongToast(errMsg)
                    Log.e(AppConstants.TAG.LOAD_ME, "listenLoadMe: ${it.message}")

                    viewModel.isLoading.value = false
                    with(binding) {
                        setStateViews(true, btnSignIn, txtSignUp, txtForgotPassword)
                    }
                }
                Status.SUCCESS -> {
                    InfoUser.currentUser = it.response!!.data
                    viewModel.isLoading.value = false
                    startMain()
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

                    Log.e(AppConstants.TAG.SIGN_IN, "subscribeListenSignIn: ${it.message}")
                    viewModel.isLoading.value = false
                    with(binding) {
                        setStateViews(true, btnSignIn, txtSignUp, txtForgotPassword)
                    }
                }
                Status.SUCCESS -> {
                    //write token + navigate mainActivity
                    CoroutineScope(Dispatchers.Main).launch {
                        myDataStore.writeJwt(it.response!!.token)
                        InfoUser.token.append(it.response.token)

                        Log.e(AppConstants.TAG.SIGN_IN, "token: ${InfoUser.token}")
                        viewModel.loadMe()
                    }
                }
            }
        })
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
            setStateViews(false, btnSignIn, txtSignUp, txtForgotPassword)
        }

        viewModel.phone = binding.txtInputEdtPhone.text.toString()
        viewModel.password = binding.txtInputEdtPassword.text.toString()
        viewModel.signInUser()
    }
}