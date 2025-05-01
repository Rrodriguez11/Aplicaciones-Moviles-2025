package es.uam.eps.dadm.faunary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class HeaderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_header, container, false)

        // Botón de ir al inicio
        val homeButton = view.findViewById<ImageButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        // Botón de usuario
        val userButton = view.findViewById<ImageButton>(R.id.userButton)
        userButton.setOnClickListener {
            startActivity(Intent(requireContext(), UserActivity::class.java))
        }

        return view
    }
}