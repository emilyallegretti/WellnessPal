package com.bignerdranch.android.wellnesspal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.wellnesspal.databinding.ListItemGradPetsBinding
import com.bignerdranch.android.wellnesspal.models.Pet


class GradPetHolder(
    val binding: ListItemGradPetsBinding
): RecyclerView.ViewHolder(binding.root){
    fun bind(pet:Pet){
        binding.petName.text = pet.name
    }
}

class ArchiveListAdapter(private val gradPets: List<Pet>): RecyclerView.Adapter<GradPetHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradPetHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGradPetsBinding.inflate(inflater, parent, false)
        return GradPetHolder(binding)
    }

    override fun getItemCount(): Int {
        return gradPets.size
    }

    override fun onBindViewHolder(holder: GradPetHolder, position: Int) {
        val pet = gradPets[position]
        holder.bind(pet)
    }

}