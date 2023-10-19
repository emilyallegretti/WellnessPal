package com.bignerdranch.android.wellnesspal.ui.foodlog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.wellnesspal.databinding.FragmentFoodLogBinding
import com.bignerdranch.android.wellnesspal.ui.authenticate.SignUpViewModel


class FoodLogFragment : Fragment() {
    private var _binding: FragmentFoodLogBinding? = null
    private lateinit var foodLogViewModel: FoodLogViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        foodLogViewModel =
            ViewModelProvider(this)[FoodLogViewModel::class.java]

        _binding = FragmentFoodLogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFoodLog
        foodLogViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonSubmitFoodLog.setOnClickListener {
                foodLogViewModel.writeNewFoodLog(fieldFoodLog.text.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}