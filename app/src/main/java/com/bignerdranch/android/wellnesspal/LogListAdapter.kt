package com.bignerdranch.android.wellnesspal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.wellnesspal.databinding.ListItemLogBinding
import com.bignerdranch.android.wellnesspal.models.User
import com.bignerdranch.android.wellnesspal.models.UserLog

class LogHolder (
    private val binding: ListItemLogBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(log: UserLog){
            binding.logInfo.text = log.toString()
        }
}

class LogListAdapter(private val logs: MutableList<UserLog>): RecyclerView.Adapter<LogHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemLogBinding.inflate(inflater, parent, false)
        return LogHolder(binding)
    }

    override fun getItemCount() = logs.size

    override fun onBindViewHolder(holder: LogHolder, position: Int) {
       val log = logs[position]
        holder.bind(log)
    }
    fun setLogs(newLogs: List<UserLog>){
        logs.clear()
        logs.addAll(newLogs)
        notifyDataSetChanged()
    }
}