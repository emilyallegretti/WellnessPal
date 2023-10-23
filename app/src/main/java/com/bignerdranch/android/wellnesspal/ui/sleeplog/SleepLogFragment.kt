package com.bignerdranch.android.wellnesspal.ui.sleeplog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.databinding.FragmentSleepLogBinding
import com.bignerdranch.android.wellnesspal.ui.foodlog.FoodLogViewModel
import com.bignerdranch.android.wellnesspal.ui.sleeplog.SleepLogViewModel

class SleepLogFragment : Fragment() {
    private var _binding: FragmentSleepLogBinding? = null
    private lateinit var sleepLogViewModel: SleepLogViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sleepLogViewModel =
            ViewModelProvider(this)[SleepLogViewModel::class.java]

        _binding = FragmentSleepLogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSleepLog

        sleepLogViewModel.text.observe(viewLifecycleOwner) {
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
            buttonSubmitSleepLog.setOnClickListener {
                sleepLogViewModel.writeNewSleepLog(fieldSleepLog.text.toString())
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}