package com.bignerdranch.android.wellnesspal.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bignerdranch.android.wellnesspal.databinding.FragmentProfileBinding
import com.bignerdranch.android.wellnesspal.ui.authenticate.AuthActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textViewFieldUsername
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // when user signs out, return to the initial AuthActivity screen
            buttonSignOut.setOnClickListener {
                auth.signOut()
                if (auth.currentUser == null) {
                    startActivity(Intent(activity, AuthActivity::class.java))
                } else {
                    Toast.makeText(context, "Error Signing Out", Toast.LENGTH_SHORT)
                }
            }

            buttonResetPassword.setOnClickListener {
                if (auth.currentUser != null){

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}