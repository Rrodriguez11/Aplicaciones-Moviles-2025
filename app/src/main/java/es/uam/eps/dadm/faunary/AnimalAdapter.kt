package es.uam.eps.dadm.faunary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.faunary.databinding.ItemAnimalBinding
import es.uam.eps.dadm.faunary.model.Animal

/**
 * Adaptador para mostrar una lista de animales en un RecyclerView.
 * Incluye callbacks para alimentar, ver detalles y medicar a cada animal.
 */
class AnimalAdapter(
    private val animales: List<Animal>,                         // Lista de animales a mostrar
    private val onFeedClick: (Animal) -> Unit,                  // Acción al presionar el botón de alimentar
    private val onDetailsClick: (Animal) -> Unit,               // Acción al hacer clic sobre el item
    private val onMedicateClick: (Animal) -> Unit               // Acción al presionar el botón de medicar
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    // ViewHolder que contiene la lógica de enlace con la vista
    class AnimalViewHolder(val binding: ItemAnimalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Asocia un objeto Animal con la vista e implementa los listeners
        fun bind(
            animal: Animal,
            onFeedClick: (Animal) -> Unit,
            onDetailsClick: (Animal) -> Unit,
            onMedicateClick: (Animal) -> Unit
        ) {
            binding.animal = animal  // Enlaza el objeto animal con la vista (data binding)
            binding.executePendingBindings() // Ejecuta los bindings pendientes

            // Listener para alimentar al animal
            binding.feedButton.setOnClickListener {
                onFeedClick(animal)
            }

            // Listener para ver detalles (al hacer clic en la tarjeta completa)
            binding.root.setOnClickListener {
                onDetailsClick(animal)
            }

            // Listener para medicar al animal
            binding.medicateButton.setOnClickListener {
                onMedicateClick(animal)
            }
        }
    }

    // Infla el layout de cada item y crea el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAnimalBinding.inflate(layoutInflater, parent, false)
        return AnimalViewHolder(binding)
    }

    // Llama a bind() para asociar los datos del animal con el ViewHolder
    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(animales[position], onFeedClick, onDetailsClick, onMedicateClick)
    }

    // Devuelve la cantidad total de elementos en la lista
    override fun getItemCount(): Int = animales.size
}
