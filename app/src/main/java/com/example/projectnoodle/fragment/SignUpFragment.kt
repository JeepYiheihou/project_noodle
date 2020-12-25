package com.example.projectnoodle.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.projectnoodle.*
import com.example.projectnoodle.customobject.VolleySingleton
import com.example.projectnoodle.databinding.FragmentLoginBinding
import com.example.projectnoodle.databinding.FragmentSignUpBinding
import com.google.gson.Gson

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val noodleViewModel: NoodleViewModel by activityViewModels()
    var confirmedPassword : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            noodleViewModel.signUpUser.name?.also { signUpEditName.setText(it) }
            signUpEditName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    noodleViewModel.signUpUser.name = p0.toString()
                }
                override fun afterTextChanged(p0: Editable?) { }
            })

            noodleViewModel.signUpUser.email?.also { signUpEditEmail.setText(it) }
            signUpEditEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    noodleViewModel.signUpUser.email = p0.toString()
                }
                override fun afterTextChanged(p0: Editable?) {}
            })

            noodleViewModel.signUpUser.password?.also { signUpEditPassword.setText(it) }
            signUpEditPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    noodleViewModel.signUpUser.password = p0.toString()
                }
                override fun afterTextChanged(p0: Editable?) {}
            })

            signUpConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    confirmedPassword = p0.toString()
                }
                override fun afterTextChanged(p0: Editable?) {}
            })

            signUpConfirmButton.setOnClickListener {
                if (checkSignUpDataValidity()) {
                    noodleViewModel.signUp()
                }
            }
        }
    }

    private fun checkSignUpDataValidity(): Boolean {
        with(noodleViewModel) {
            if (signUpUser.name == null) {
                Toast.makeText(requireContext(), "Please provide a user name.", Toast.LENGTH_SHORT).show()
                return false
            }

            if (signUpUser.email == null) {
                Toast.makeText(requireContext(), "Please provide an email address.", Toast.LENGTH_SHORT).show()
                return false
            }

            if (signUpUser.password == null) {
                Toast.makeText(requireContext(), "Please provide a password.", Toast.LENGTH_SHORT).show()
                return false
            }

            if (confirmedPassword == null) {
                Toast.makeText(requireContext(), "Please confirm password.", Toast.LENGTH_SHORT).show()
                return false
            }

            if (signUpUser.password != confirmedPassword) {
                Toast.makeText(requireContext(), "Password doesn't match with confirmed password.", Toast.LENGTH_SHORT).show()
                return false
            }

            return true
        }
    }
}