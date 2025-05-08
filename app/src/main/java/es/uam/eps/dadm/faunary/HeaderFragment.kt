package es.uam.eps.dadm.faunary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

/**
 * Fragmento que representa el encabezado común de la app.
 * Contiene botones para navegación rápida: Inicio (Home) y Usuario.
 */
class HeaderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento (fragment_header.xml)
        val view = inflater.inflate(R.layout.fragment_header, container, false)

        // Configura el botón que lleva a la pantalla principal (MainActivity)
        val homeButton = view.findViewById<ImageButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        // Configura el botón que lleva a la pantalla de usuario (UserActivity)
        val userButton = view.findViewById<ImageButton>(R.id.userButton)
        userButton.setOnClickListener {
            startActivity(Intent(requireContext(), UserActivity::class.java))
        }

        return view
    }
}
