package com.bignerdranch.android.wellnesspal.ui.authenticate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bignerdranch.android.wellnesspal.MainActivity
import com.bignerdranch.android.wellnesspal.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

private const val TAG = "SignInFragment"
class SignInFragment : Fragment() {
        private lateinit var auth: FirebaseAuth
        private var _binding: FragmentSignInBinding? = null

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

        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonSignIn.setOnClickListener {
                signIn(fieldEmail.text.toString(), fieldPassword.text.toString())
            }
        }
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    private fun signIn(email: String, password: String) {
        if (!validateForm()) {
            return
        }
    // attempt to sign in with the given email and password, otherwise display error message
        try {
            auth.signInWithEmailAndPassword(email, password)
            if (auth.currentUser != null ) {
                startActivity(Intent(activity, MainActivity::class.java))
            }

        } catch(e: Exception){  //TODO: different catch clauses for each FirebaseAuthException
            Toast.makeText(
                context,
                "Sign-In Failed",
                Toast.LENGTH_SHORT).show()

        }
    }

    // This code was sourced from:
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


    }
