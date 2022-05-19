package com.sahilpc.bookie.presentation.signup_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import at.favre.lib.crypto.bcrypt.BCrypt
import com.sahilpc.bookie.R
import com.sahilpc.bookie.databinding.FragmentSignInBinding
import com.sahilpc.bookie.databinding.FragmentSignUpBinding
import com.sahilpc.bookie.domain.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.btnSignUp.setOnClickListener {

            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone= binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

           val result =  viewModel.signUp(
                User(
                    name = username,
                    email = email,
                    password = password,
                    phone = phone
                )
            )

            if (result)findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())

            viewModel.errorState.observe(viewLifecycleOwner){ errorState ->
                binding.etEmail.error = errorState.emailError
                binding.etPassword.error = errorState.passwordError
                binding.etPhone.error = errorState.phoneError
            }
        }

        binding.loginFragmentBtn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }
    }

}