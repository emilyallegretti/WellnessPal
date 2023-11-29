package com.bignerdranch.android.wellnesspal.ui.profile

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.databinding.FragmentProfileBinding
import com.bignerdranch.android.wellnesspal.ui.authenticate.AuthActivity
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import org.w3c.dom.Text


private var TAG = "ProfileFragment"

class ProfileFragment : Fragment() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var profileViewModel: ProfileViewModel
    private var database = Firebase.database.reference
    private var userReference = database.child("users").child(auth.currentUser!!.uid)
    private var _binding: FragmentProfileBinding? = null
    var gradPetsQuery = database.child("users").child(auth.currentUser!!.uid).child("pets").orderByChild("age").equalTo(9.0)


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
        }
        profileViewModel.gradPetsCountData.observe(viewLifecycleOwner) {
            textViewFieldPetsGraduated.text= it.toString()
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
        gradPetsQuery =
            database.child("users").child(auth.currentUser!!.uid).child("pets").orderByChild("age")
                .equalTo(9.0)
        profileViewModel.addGradPetCountEventListener(gradPetsQuery)

        binding.apply {
            // when user signs out, return to the initial AuthActivity screen
            buttonSignOut.setOnClickListener {
                if (checkForInternet(view.context)) {
                    auth.signOut()
                    if (auth.currentUser == null) {
                        startActivity(Intent(activity, AuthActivity::class.java))
                    } else {
                        Toast.makeText(context, "Error Signing Out", Toast.LENGTH_SHORT)
                    }
                } else {
                    Toast.makeText(view.context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }

            buttonResetPassword.setOnClickListener {
                if (checkForInternet(view.context)) {
                    auth.sendPasswordResetEmail(auth.currentUser!!.email.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "Email Send Success")
                                Toast.makeText(
                                    context,
                                    "Password Reset Email Sent",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                Log.d(TAG, "Email Send Failure")
                                Toast.makeText(
                                    context,
                                    "Unable to Send Password Reset Email",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(view.context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }
            buttonDeleteAccount.setOnClickListener {
                // first reauthenticate user TODO: ask user to re-enter credentials
                // auth.currentUser.reauthenticate(EmailAuthProvider.getCredential(auth.currentUser.email, auth.currentUser.pa))
                // first delete user entry from database
                if (checkForInternet(view.context)) {
                    findNavController().navigate(R.id.to_reauthenticateDialog)
                    profileViewModel.deleteUserEntry()
                } else {
                    Toast.makeText(view.context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }
            // Update goals based on user input.
            buttonChangeMeals.setOnClickListener {
                if (checkForInternet(view.context)) {
                    val text = editTextFieldChangeMeals.text.toString()
                    if (text == "") {
                        Toast.makeText(context, R.string.empty_field_err, Toast.LENGTH_LONG).show()
                    } else {
                        profileViewModel.updateGoal(text, "eat")
                        editTextFieldChangeMeals.text.clear()
                        Toast.makeText(context, "Goal successfully updated", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    editTextFieldChangeMeals.text.clear()
                    Toast.makeText(view.context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }
            buttonChangeWater.setOnClickListener {
                if(checkForInternet(view.context)) {
                    val text = editTextFieldChangeWater.text.toString()
                    if (text == "") {
                        Toast.makeText(context, R.string.empty_field_err, Toast.LENGTH_LONG).show()
                    } else {
                        profileViewModel.updateGoal(text, "water")
                        editTextFieldChangeWater.text.clear()
                        Toast.makeText(context, "Goal successfully updated", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    editTextFieldChangeWater.text.clear()
                    Toast.makeText(view.context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }

            buttonChangeSleep.setOnClickListener {
                if (checkForInternet(view.context)) {
                    val text = editTextFieldChangeSleep.text.toString()
                    if (text == "") {
                        Toast.makeText(context, R.string.empty_field_err, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        profileViewModel.updateGoal(text, "sleep")
                        editTextFieldChangeSleep.text.clear()
                        Toast.makeText(context, "Goal successfully updated", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        view.context,
                        "No Internet Connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        userReference.removeEventListener(profileViewModel.userListener)
    }


    override fun onStop() {
        super.onStop()
        gradPetsQuery.removeEventListener(profileViewModel.gradPetCountListener)
    }

    //Code from geeks for geeks geeksforgeeks.com
    private fun checkForInternet(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork?:return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network)?:return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }else{
            @Suppress("DEPRECATION") val networkInfo = connectivityManager.activeNetworkInfo?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}



