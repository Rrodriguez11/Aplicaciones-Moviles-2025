package es.uam.eps.dadm.faunary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityAnimalDetailBinding

/**
 * Actividad para mostrar los detalles de un animal específico.
 * Utiliza DataBinding y un botón para volver al resumen del hábitat.
 */
class AnimalDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla la vista usando DataBinding y asocia el layout con esta actividad
        val binding = DataBindingUtil.setContentView<ActivityAnimalDetailBinding>(
            this,
            R.layout.activity_animal_detail
        )

        // Acción del botón "Volver al resumen": finaliza la actividad actual
        binding.backToSummaryButton.setOnClickListener {
            finish() // Vuelve a la actividad anterior
        }
    }
}
