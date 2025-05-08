package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import es.uam.eps.dadm.faunary.databinding.FragmentAnimalDetailBinding
import es.uam.eps.dadm.faunary.model.Animal

/**
 * Fragmento que muestra los detalles de un animal específico.
 * Utiliza Data Binding para vincular los datos del animal con la interfaz.
 */
class AnimalDetailFragment : Fragment() {

    companion object {
        // Clave para pasar el objeto Animal a través del Bundle
        const val ANIMAL_KEY = "animal_key"
    }

    private lateinit var binding: FragmentAnimalDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el layout usando Data Binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_animal_detail,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupera el objeto Animal del Bundle (debe ser Serializable)
        val animal = arguments?.getSerializable(ANIMAL_KEY) as? Animal

        // Asigna el animal al binding para que se muestren sus datos
        animal?.let {
            binding.animal = it
        }

        // Botón para volver al fragmento anterior (navegación hacia atrás)
        binding.backToSummaryButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
