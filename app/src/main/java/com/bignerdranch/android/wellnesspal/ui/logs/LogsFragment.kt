package com.bignerdranch.android.wellnesspal.ui.logs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.wellnesspal.LogListAdapter
import com.bignerdranch.android.wellnesspal.databinding.FragmentLogsBinding
import com.bignerdranch.android.wellnesspal.models.UserLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

private var TAG = "LogsFragment"

class LogsFragment : Fragment() {

    private var _binding: FragmentLogsBinding? = null
    private lateinit var adapter : LogListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var logsViewModel: LogsViewModel
    private var database = Firebase.database.reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var logsReference = database.child("users").child(auth.currentUser!!.uid).child("logs")
    private lateinit var logsList : List<UserLog>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logsViewModel =
            ViewModelProvider(this).get(LogsViewModel::class.java)

        _binding = FragmentLogsBinding.inflate(inflater, container, false)
        binding.logsRecyclerView.layoutManager = LinearLayoutManager(context)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logsViewModel.addLogEventListener(logsReference)

            logsViewModel.logData.observe(viewLifecycleOwner) {
                //logsList = it
                adapter = LogListAdapter(it)
                //Log.d(TAG, "Log List")
                //Log.d(TAG, logsList.toString())
                //Log.d(TAG, "Live Data list")
                //Log.d(TAG, logsViewModel.logData.value.toString())
                binding.logsRecyclerView.adapter = adapter
            }

    }

    private fun observeData(callback:() -> Unit){
        Log.d(TAG, "inside observe data ")
        callback()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        logsReference.removeEventListener(logsViewModel.LogListener)
    }

}