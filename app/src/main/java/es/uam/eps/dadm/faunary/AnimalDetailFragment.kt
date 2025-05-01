package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import es.uam.eps.dadm.faunary.databinding.FragmentAnimalDetailBinding

class AnimalDetailFragment : Fragment() {

    private lateinit var binding: FragmentAnimalDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimalDetailBinding.inflate(inflater, container, false)

        // Obtener el argumento del nombre del animal
        val animalName = arguments?.getString("animalName") ?: "Animal"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToSummaryButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}