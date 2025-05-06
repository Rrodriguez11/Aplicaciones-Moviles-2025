package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.uam.eps.dadm.faunary.adapter.AnimalAdapter
import es.uam.eps.dadm.faunary.databinding.FragmentAnimalsBinding
import es.uam.eps.dadm.faunary.model.Animal
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

        view.post {
            requireActivity().findViewById<View>(R.id.cleaningFragmentContainer)?.visibility = View.VISIBLE
        }

        val recinto = viewModel.recinto.value
        if (recinto != null) {
            val adapter = AnimalAdapter(
                recinto.animales,
                onFeedClick = { animal ->
                    viewModel.alimentarAnimal(animal)
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                },
                onDetailsClick = { animal ->
                    val fragment = AnimalDetailFragment()
                    val args = Bundle().apply {
                        putSerializable(AnimalDetailFragment.ANIMAL_KEY, animal)
                    }
                    fragment.arguments = args

                    requireActivity().findViewById<View>(R.id.cleaningFragmentContainer)?.visibility = View.GONE

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.animalFragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            )

            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
        }
    }
}
