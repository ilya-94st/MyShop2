package com.example.myshop.presentation.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.myshop.R
import com.example.myshop.common.EventClass
import com.example.myshop.databinding.FragmentLoginBinding
import com.example.myshop.presentation.base.BaseFragment
import com.example.myshop.presentation.ui.prefs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()


    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.btLogin.setOnClickListener {
            showProgressDialog("Please wait...")
            viewModel.validLoginDetails(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        binding.tvForgetPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        viewModel.result.observe(viewLifecycleOwner) { event ->
            when (event) {
                 is EventClass.Success -> {
                    viewModel.getUserDetails()
                    userLoggedInSuccessful()
                }
                is EventClass.ErrorIn -> {
                    hideProgressDialog()
                    errorSnackBar(event.error, true)
                    if (event.error == requireContext().getString(R.string.checkedEmail)) {
                        binding.etEmail.error = event.error
                    }
                    if (event.error == requireContext().getString(R.string.checkedPassword)) {
                        binding.etPassword.error = event.error
                    }
                    if (event.error == requireContext().getString(R.string.checkedEmailCorrect)) {
                        binding.etEmail.error = event.error
                    }
                }
                else -> Unit
            }
        }

    }



    private fun userLoggedInSuccessful() {
                viewModel.users.observe(viewLifecycleOwner){ user ->
                    if(user.profileCompleted == 0) {
                        // if user profile is incomplete then launch the UserProfileFragment
                            prefs.idUser = user.id
                        val bundle = Bundle().apply {
                            putSerializable("users", user)
                        }
                        findNavController().navigate(R.id.action_loginFragment_to_userProfileFragment, bundle)
                        hideProgressDialog()
                    } else {
                        // Redirect the user to Main screen after log in
                        prefs.idUser = user.id
                        findNavController().navigate(R.id.action_loginFragment_to_dashBoardFragment)
                        hideProgressDialog()
                    }
                }
        }
}