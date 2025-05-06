package es.uam.eps.dadm.faunary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.faunary.databinding.ItemAnimalBinding
import es.uam.eps.dadm.faunary.model.Animal

class AnimalAdapter(
    private val animales: List<Animal>,
    private val onFeedClick: (Animal) -> Unit,
    private val onDetailsClick: (Animal) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(val binding: ItemAnimalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            animal: Animal,
            onFeedClick: (Animal) -> Unit,
            onDetailsClick: (Animal) -> Unit
        ) {
            binding.animal = animal
            binding.executePendingBindings()

            // Botón de alimentar
            binding.feedButton.setOnClickListener {
                onFeedClick(animal)
            }

            // Al hacer clic en el item completo → ver detalles
            binding.root.setOnClickListener {
                onDetailsClick(animal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAnimalBinding.inflate(layoutInflater, parent, false)
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(animales[position], onFeedClick, onDetailsClick)
    }

    override fun getItemCount(): Int = animales.size
}
