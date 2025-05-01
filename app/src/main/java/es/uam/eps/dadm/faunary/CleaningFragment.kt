package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.uam.eps.dadm.faunary.databinding.FragmentCleaningBinding
import es.uam.eps.dadm.faunary.viewmodel.HabitatViewModel

class CleaningFragment : Fragment() {

    private lateinit var binding: FragmentCleaningBinding
    private lateinit var viewModel: HabitatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cleaning, container, false)
        viewModel = ViewModelProvider(requireActivity())[HabitatViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}