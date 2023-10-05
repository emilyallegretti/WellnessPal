package com.bignerdranch.android.wellnesspal.ui.logs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bignerdranch.android.wellnesspal.databinding.FragmentLogsBinding


class LogsFragment : Fragment() {

    private var _binding: FragmentLogsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val logsViewModel =
            ViewModelProvider(this).get(LogsViewModel::class.java)

        _binding = FragmentLogsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLogs
        logsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}