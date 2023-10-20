package com.bignerdranch.android.wellnesspal.ui.waterlog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.wellnesspal.databinding.FragmentWaterLogBinding


class WaterLogFragment : Fragment() {
    private var _binding: FragmentWaterLogBinding? = null
    private lateinit var waterLogViewModel: WaterLogViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        waterLogViewModel =
            ViewModelProvider(this)[WaterLogViewModel::class.java]

        _binding = FragmentWaterLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonSubmitWaterLog.setOnClickListener {
                waterLogViewModel.writeNewWaterLog(fieldWaterLog.text.toString())
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

