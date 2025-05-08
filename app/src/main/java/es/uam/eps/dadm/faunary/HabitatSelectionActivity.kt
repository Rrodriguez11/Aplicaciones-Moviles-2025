package es.uam.eps.dadm.faunary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityHabitatSelectionBinding

/**
 * Actividad que permite al usuario seleccionar un hábitat para gestionarlo.
 * Genera botones dinámicamente a partir de los recintos disponibles en el repositorio.
 */
class HabitatSelectionActivity : AppCompatActivity() {

    // Binding del layout XML (activity_habitat_selection.xml)
    private lateinit var binding: ActivityHabitatSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Data Binding con el layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_habitat_selection)

        // Obtiene la lista de recintos definidos en el repositorio
        val habitatList = es.uam.eps.dadm.faunary.data.DataRepository.getTodosLosRecintos()

        // Crea dinámicamente un botón para cada recinto
        habitatList.forEach { recinto ->
            val button = Button(this).apply {
                text = recinto.nombre      // Nombre visible en el botón
                textSize = 20f
                setPadding(32, 20, 32, 20)

                // Al hacer clic, se abre HabitatActivity con el nombre del recinto como parámetro
                setOnClickListener {
                    val intent = Intent(this@HabitatSelectionActivity, HabitatActivity::class.java)
                    intent.putExtra("HABITAT_NAME", recinto.nombre)
                    startActivity(intent)
                }
            }

            // Agrega el botón al contenedor del layout
            binding.habitatButtonsContainer.addView(button)
        }
    }
}
