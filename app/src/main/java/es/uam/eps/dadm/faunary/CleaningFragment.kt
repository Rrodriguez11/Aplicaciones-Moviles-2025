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

/**
 * Fragmento encargado de mostrar y gestionar el estado de limpieza del recinto.
 * Se comunica con HabitatViewModel para realizar y reflejar cambios.
 */
class CleaningFragment : Fragment() {

    private lateinit var binding: FragmentCleaningBinding
    private lateinit var viewModel: HabitatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el layout XML utilizando DataBinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cleaning, container, false)

        // Obtiene el ViewModel compartido con el resto de fragmentos
        viewModel = ViewModelProvider(requireActivity())[HabitatViewModel::class.java]

        // Asocia el ViewModel y el lifecycle con el layout
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa el evento LiveData que indica que debe mostrarse el Toast
        viewModel.showCleaningToast.observe(viewLifecycleOwner) { show ->
            if (show == true) {
                // Muestra un mensaje breve indicando que la limpieza fue exitosa
                Toast.makeText(requireContext(), R.string.clean_success_toast, Toast.LENGTH_SHORT).show()
                // Reinicia el flag para que el Toast no se repita innecesariamente
                viewModel.resetCleaningToast()
            }
        }

        // Observa cambios que requieren una actualización visual de los datos vinculados
        viewModel.actualizarUI.observe(viewLifecycleOwner) {
            // Forza el refresco de todas las variables de binding
            binding.invalidateAll()
        }
    }
}
