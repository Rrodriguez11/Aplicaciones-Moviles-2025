package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.uam.eps.dadm.faunary.databinding.FragmentAnimalsBinding
import es.uam.eps.dadm.faunary.model.Animal
import es.uam.eps.dadm.faunary.viewmodel.HabitatViewModel

/**
 * Fragmento que muestra una lista de animales del hábitat actual.
 * Permite alimentar, medicar o ver detalles de cada animal.
 */
class AnimalsFragment : Fragment() {

    private lateinit var binding: FragmentAnimalsBinding
    private lateinit var viewModel: HabitatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el layout con DataBinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_animals, container, false)

        // Obtiene una instancia compartida del ViewModel
        viewModel = ViewModelProvider(requireActivity())[HabitatViewModel::class.java]

        // Vincula el ViewModel y lifecycle al binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Asegura que el contenedor de limpieza esté visible (por si fue ocultado)
        view.post {
            requireActivity().findViewById<View>(R.id.cleaningFragmentContainer)?.visibility = View.VISIBLE
        }

        // Observa cambios en el recinto y actualiza la lista de animales
        viewModel.recinto.observe(viewLifecycleOwner) { recinto ->
            Log.d("AnimalsFragment", "Recinto observado: ${recinto?.nombre ?: "null"}")

            if (recinto != null) {
                // Configura el adaptador con acciones para cada botón
                val adapter = AnimalAdapter(
                    recinto.animales,
                    onFeedClick = { animal ->
                        viewModel.alimentarAnimal(animal)
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    },
                    onDetailsClick = { animal ->
                        // Navega al fragmento de detalles del animal
                        val fragment = AnimalDetailFragment()
                        val args = Bundle().apply {
                            putSerializable(AnimalDetailFragment.ANIMAL_KEY, animal)
                        }
                        fragment.arguments = args

                        // Oculta la vista de limpieza durante los detalles
                        requireActivity().findViewById<View>(R.id.cleaningFragmentContainer)?.visibility = View.GONE

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.animalFragmentContainer, fragment)
                            .addToBackStack(null)
                            .commit()
                    },
                    onMedicateClick = { animal ->
                        viewModel.medicarAnimal(animal)
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    }
                )

                // Configura el RecyclerView
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter
            }
        }

        // Si se fuerza actualización desde el ViewModel, refresca el RecyclerView
        viewModel.actualizarUI.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }
}
