package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.uam.eps.dadm.faunary.databinding.FragmentAnimalsBinding
import es.uam.eps.dadm.faunary.viewmodel.HabitatViewModel

class AnimalsFragment : Fragment() {

    private lateinit var binding: FragmentAnimalsBinding
    private lateinit var viewModel: HabitatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_animals, container, false)
        viewModel = ViewModelProvider(requireActivity())[HabitatViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailsButton.setOnClickListener {
            val fragment = AnimalDetailFragment()

            val args = Bundle().apply {
                putString("animalName", "León")
            }
            fragment.arguments = args

            parentFragmentManager.beginTransaction()
                .replace(R.id.animalFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
