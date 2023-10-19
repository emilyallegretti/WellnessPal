package com.bignerdranch.android.wellnesspal.ui.authenticate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.wellnesspal.MainActivity
import com.bignerdranch.android.wellnesspal.databinding.FragmentSignUpBinding
import com.bignerdranch.android.wellnesspal.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class SignUpFragment: Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var signUpViewModel: SignUpViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signUpViewModel =
            ViewModelProvider(this)[SignUpViewModel::class.java]
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonSignUp.setOnClickListener {
                signUp(fieldEmail.text.toString(), fieldPassword.text.toString())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
// Code sourced from:
// https://github.com/firebase/quickstart-android/blob/f96650c0944f2c85aeaa7b70c0a388d4cde73343/database/app/src/main/java/com/google/firebase/quickstart/database/kotlin/SignInFragment.kt#L129
    private fun validateForm(): Boolean {
        var result = true
        if (TextUtils.isEmpty(binding.fieldEmail.text.toString())) {
            binding.fieldEmail.error = "Required"
            result = false
        } else {
            binding.fieldEmail.error = null
        }

        if (TextUtils.isEmpty(binding.fieldPassword.text.toString())) {
            binding.fieldPassword.error = "Required"
            result = false
        } else {
            binding.fieldPassword.error = null
        }

        return result
    }

    private fun signUp(email: String, password: String) {
        try {
            if (!validateForm()) {
                return
            }
            // if sign-up successful, start MainActivity
            auth.createUserWithEmailAndPassword(email, password)
            if (auth.currentUser != null) {
                startActivity(Intent(activity, MainActivity::class.java))
                signUpViewModel.writeNewUser(email, password, auth.currentUser!!.uid)
            }
        } catch(e: FirebaseAuthException){  //TODO: different catch clauses for each FirebaseAuthException possible
                Toast.makeText(
                    context,
                    "Error creating new user",
                    Toast.LENGTH_SHORT).show()

            }
    }

}