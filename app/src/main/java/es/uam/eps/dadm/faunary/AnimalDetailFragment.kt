package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import es.uam.eps.dadm.faunary.databinding.FragmentAnimalDetailBinding
import es.uam.eps.dadm.faunary.model.Animal

class AnimalDetailFragment : Fragment() {

    companion object {
        const val ANIMAL_KEY = "animal_key"
    }

    private lateinit var binding: FragmentAnimalDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_animal_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animal = arguments?.getSerializable(ANIMAL_KEY) as? Animal

        animal?.let {
            binding.animal = it
        }

        binding.backToSummaryButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
