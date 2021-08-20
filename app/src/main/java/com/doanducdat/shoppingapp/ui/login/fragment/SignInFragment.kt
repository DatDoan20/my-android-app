package com.doanducdat.shoppingapp.ui.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.databinding.FragmentSignInBinding


class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding

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
    }
    private fun checkLengthNumberPhone() {
        binding.txtInputEdtPhone.doOnTextChanged { text, start, before, count ->
            if (text!!.length > 20) {
                binding.txtInputLayout.error = "No More!"
            } else if (text.length < 21) {
                binding.txtInputLayout.error = null
            }
        }
    }
}