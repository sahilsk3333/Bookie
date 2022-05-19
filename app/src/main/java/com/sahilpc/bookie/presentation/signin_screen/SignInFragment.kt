package com.sahilpc.bookie.presentation.signin_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sahilpc.bookie.R
import com.sahilpc.bookie.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.btnLogin.setOnClickListener {
            val user = binding.etUser.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.signIn(user, password)

            viewModel.errorState.observe(viewLifecycleOwner) { errorState ->
                binding.etUser.error = errorState.userError
                binding.etPassword.error = errorState.passwordError
            }

        }

        viewModel.isLoginSuccess.observe(viewLifecycleOwner){
            if (it)findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment())
        }

        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }

}