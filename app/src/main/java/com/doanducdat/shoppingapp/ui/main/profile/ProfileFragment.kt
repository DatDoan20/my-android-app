package com.doanducdat.shoppingapp.ui.main.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.doanducdat.shoppingapp.databinding.FragmentProfileBinding
import com.doanducdat.shoppingapp.myinterface.MyActionApp
import com.doanducdat.shoppingapp.ui.base.BaseFragment
import com.doanducdat.shoppingapp.utils.AppConstants
import com.doanducdat.shoppingapp.utils.EmailAuthentication
import com.doanducdat.shoppingapp.utils.InfoUser
import com.doanducdat.shoppingapp.utils.dialog.MyVerifyEmailDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(), MyActionApp {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpVerifyEmail()
    }

    private fun setUpVerifyEmail() {
//        if (InfoUser.currentUser?.stateVerifyEmail == false) {
//            binding.txtVerifyEmail.visibility = View.VISIBLE
//            binding.txtVerifyEmail.setOnClickListener {
//                doActionClick(AppConstants.ActionClick.VERIFY_EMAIL)
//            }
//        } else {
//            binding.txtVerifyEmail.visibility = View.GONE
//        }
        binding.txtVerifyEmail.setOnClickListener {
            doActionClick(AppConstants.ActionClick.VERIFY_EMAIL)
        }
    }

    override fun doActionClick(CODE_ACTION_CLICK: Int) {
        when (CODE_ACTION_CLICK) {
            AppConstants.ActionClick.VERIFY_EMAIL -> {
                val verifyEmailDialog: MyVerifyEmailDialog = MyVerifyEmailDialog(requireActivity())
                verifyEmailDialog.show()
            }
        }
    }

}