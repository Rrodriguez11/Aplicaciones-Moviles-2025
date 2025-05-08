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
 * Actividad principal de la aplicación (menú de inicio).
 * Permite navegar a secciones como hábitats y simular funciones de test.
 */
class MainActivity : AppCompatActivity() {

    // Data Binding para acceder directamente a las vistas del layout activity_main.xml
    private lateinit var binding: ActivityMainBinding

    /**
     * Se llama al crear la actividad.
     * Configura la interfaz principal, navegación y botones de desarrollo.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Permite que el contenido ocupe todo el espacio de pantalla

        // Infla el layout usando Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Ajusta el padding del layout para evitar solapamiento con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón que lleva a la selección de hábitats
        binding.btnHabitats.setOnClickListener {
            val intent = Intent(this, HabitatSelectionActivity::class.java)
            startActivity(intent)
        }

        // Botón para la sección de Almacén (no implementado)
        binding.btnAlmacen.setOnClickListener {
            Toast.makeText(this, "Sección de Almacén no disponible aún", Toast.LENGTH_SHORT).show()
        }

        // Botón de depuración para simular el paso de un día
        findViewById<Button>(R.id.advanceDayButton).setOnClickListener {
            // Aplica lógica de avance diario (como alimentar, medicar, limpiar...)
            DataRepository.actualizarEstadoDiario(applicationContext)

            // Borra la fecha almacenada para forzar futuras actualizaciones
            val prefs = getSharedPreferences("faunary_prefs", MODE_PRIVATE)
            prefs.edit().remove("last_update_date").apply()

            Toast.makeText(this, "¡Simulado paso de un día!", Toast.LENGTH_SHORT).show()

            // Opcional: si hay un ViewModel activo, forzar actualización de UI
            val viewModel = try {
                ViewModelProvider(this)[es.uam.eps.dadm.faunary.viewmodel.HabitatViewModel::class.java]
            } catch (_: Exception) {
                null
            }
            viewModel?.forzarActualizacion()
        }

        // Botón de depuración para reiniciar todos los datos
        findViewById<Button>(R.id.advanceDayButton2).setOnClickListener {
            DataRepository.reiniciarDatosParaTest()

            val prefs = getSharedPreferences("faunary_prefs", MODE_PRIVATE)
            prefs.edit().clear().apply()

            Toast.makeText(this, "TODO reiniciado para test", Toast.LENGTH_SHORT).show()
        }
    }
}
