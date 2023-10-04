package com.bignerdranch.android.wellnesspal.ui.gradpets

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bignerdranch.android.wellnesspal.databinding.FragmentGradPetsBinding


class GradPetsFragment : Fragment() {

    private var _binding: FragmentGradPetsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gradPetsViewModel =
            ViewModelProvider(this).get(GradPetsViewModel::class.java)

        _binding = FragmentGradPetsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGradPets
        gradPetsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}