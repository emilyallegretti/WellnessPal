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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class ProfileFragment : Fragment() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var profileViewModel: ProfileViewModel
    private var database = Firebase.database.reference
    private var userReference = database.child("users").child(auth.currentUser!!.uid)
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

        val textViewFieldFirstName: TextView = binding.textViewFieldFirstName
        val textViewFieldLastName: TextView = binding.textViewFieldLastName
        val textViewFieldUsername: TextView = binding.textViewFieldUsername
        val textViewFieldPetsGraduated: TextView = binding.textViewFieldPetsGraduated


        profileViewModel.userData.observe(viewLifecycleOwner) {
            textViewFieldFirstName.text = it.fname
            textViewFieldLastName.text = it.lname
            textViewFieldUsername.text = it.email
            textViewFieldPetsGraduated.text = "Adding Later"
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.addUserEventListener(userReference)

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
                    val bool = profileViewModel.resetPassword(editTextFieldOldPassword.toString(),
                        editTextFieldNewPassword.toString(), editTextFieldReEnterNewPass.toString())
                    if(!bool){
                        Toast.makeText(context, "Cannot Reset Password, Try Again", Toast.LENGTH_LONG)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}