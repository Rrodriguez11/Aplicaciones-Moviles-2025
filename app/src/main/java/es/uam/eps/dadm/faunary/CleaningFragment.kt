package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa el evento para mostrar el Toast de limpieza realizada
        viewModel.showCleaningToast.observe(viewLifecycleOwner) { show ->
            if (show == true) {
                Toast.makeText(requireContext(), R.string.clean_success_toast, Toast.LENGTH_SHORT).show()
                viewModel.resetCleaningToast()
            }
        }
    }
}