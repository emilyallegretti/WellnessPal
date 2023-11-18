package com.bignerdranch.android.wellnesspal.ui.authenticate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

        private var _binding: FragmentWelcomeBinding? = null

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonSignIn.setOnClickListener {
                if(checkForInternet(view.context)){
                    findNavController().navigate(R.id.to_sign_in)
                }
               else{
                   Toast.makeText(view.context, "No Internet Connenction", Toast.LENGTH_SHORT).show()
                }
            }
            buttonSignUp.setOnClickListener {
                if (checkForInternet(view.context)) {
                    findNavController().navigate(R.id.to_sign_up)
                }else{
                    Toast.makeText(view.context, "No Internet Connenction", Toast.LENGTH_SHORT).show()

                }
            }
        }

    }

        override fun onDestroyView() {
            Log.d("TAG", "onDestroyView() called")
            super.onDestroyView()
            _binding = null
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