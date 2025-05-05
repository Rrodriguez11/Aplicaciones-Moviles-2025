package es.uam.eps.dadm.faunary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.faunary.databinding.ItemAnimalBinding
import es.uam.eps.dadm.faunary.model.Animal

class AnimalAdapter(
    private val animales: List<Animal>,
    private val onFeedClick: (Animal) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(val binding: ItemAnimalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(animal: Animal, onFeedClick: (Animal) -> Unit) {
            binding.animal = animal
            binding.executePendingBindings()

            binding.feedButton.setOnClickListener {
                onFeedClick(animal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAnimalBinding.inflate(layoutInflater, parent, false)
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(animales[position], onFeedClick)
    }

    override fun getItemCount(): Int = animales.size
}
