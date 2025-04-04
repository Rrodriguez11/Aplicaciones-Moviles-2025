package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityMainBinding

/**
 * Actividad principal de la aplicación (menú principal).
 * Muestra una pantalla inicial con el logo, botones y elementos visuales.
 */
class MainActivity : AppCompatActivity() {

    // Enlace al layout activity_main.xml mediante Data Binding
    private lateinit var binding: ActivityMainBinding

    /**
     * Inicializa la vista principal, configura el diseño de borde a borde y la navegación.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla el layout usando DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón que lleva a la vista del hábitat
        binding.btnTest.setOnClickListener {
            val intent = Intent(this, HabitatActivity::class.java)
            startActivity(intent)
        }
    }
}