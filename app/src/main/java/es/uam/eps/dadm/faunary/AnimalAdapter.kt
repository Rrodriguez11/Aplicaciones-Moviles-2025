package es.uam.eps.dadm.faunary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.faunary.databinding.ItemAnimalBinding
import es.uam.eps.dadm.faunary.database.AnimalEntity

class AnimalAdapter(
    private val animales: List<AnimalEntity>,
    private val onFeedClick: (AnimalEntity) -> Unit,
    private val onDetailsClick: (AnimalEntity) -> Unit,
    private val onMedicateClick: (AnimalEntity) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(val binding: ItemAnimalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            animal: AnimalEntity,
            onFeedClick: (AnimalEntity) -> Unit,
            onDetailsClick: (AnimalEntity) -> Unit,
            onMedicateClick: (AnimalEntity) -> Unit
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

            //binding.medicateButton.setOnClickListener {
                //onMedicateClick(animal)
            //}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAnimalBinding.inflate(layoutInflater, parent, false)
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(animales[position], onFeedClick, onDetailsClick, onMedicateClick)
    }

    override fun getItemCount(): Int = animales.size
}