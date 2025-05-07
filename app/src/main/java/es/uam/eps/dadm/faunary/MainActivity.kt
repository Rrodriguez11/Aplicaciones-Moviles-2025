package es.uam.eps.dadm.faunary

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityMainBinding
import android.widget.Toast
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import es.uam.eps.dadm.faunary.data.DataRepository



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

        // Aplica padding la layout para que el contenido no solape con las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón de hábitats
        binding.btnHabitats.setOnClickListener {
            val intent = Intent(this, HabitatSelectionActivity::class.java)
            startActivity(intent)
        }

        // Botón de Almacén
        binding.btnAlmacen.setOnClickListener {
            // De momento mostramos un mensaje
            Toast.makeText(this, "Sección de Almacén no disponible aún", Toast.LENGTH_SHORT).show()
        }

        // Botón Debug para cambiar de día por el testeo.
        findViewById<Button>(R.id.advanceDayButton).setOnClickListener {
            DataRepository.actualizarEstadoDiario(applicationContext)

            val prefs = getSharedPreferences("faunary_prefs", MODE_PRIVATE)
            prefs.edit().remove("last_update_date").apply()

            Toast.makeText(this, "¡Simulado paso de un día!", Toast.LENGTH_SHORT).show()

            // Intentar notificar al ViewModel si estamos en un Habitat (opcional)
            val viewModel = try {
                ViewModelProvider(this)[es.uam.eps.dadm.faunary.viewmodel.HabitatViewModel::class.java]
            } catch (_: Exception) {
                null
            }
            viewModel?.forzarActualizacion()
        }



        findViewById<Button>(R.id.advanceDayButton2).setOnClickListener {
            es.uam.eps.dadm.faunary.data.DataRepository.reiniciarDatosParaTest()

            val prefs = getSharedPreferences("faunary_prefs", MODE_PRIVATE)
            prefs.edit().clear().apply()

            Toast.makeText(this, "TODO reiniciado para test", Toast.LENGTH_SHORT).show()
        }



    }
}