package com.bignerdranch.android.wellnesspal.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                findNavController().navigate(R.id.to_sign_in)
            }
            buttonSignUp.setOnClickListener {
                findNavController().navigate(R.id.to_sign_up)
            }
        }

    }

        override fun onDestroyView() {
            Log.d("TAG", "onDestroyView() called")
            super.onDestroyView()
            _binding = null
        }

    }