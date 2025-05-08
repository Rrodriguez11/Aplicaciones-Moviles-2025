package es.uam.eps.dadm.faunary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityHabitatBinding
import es.uam.eps.dadm.faunary.viewmodel.HabitatViewModel
import timber.log.Timber
import android.util.Log

/**
 * Actividad que muestra la vista detallada de un hábitat.
 * Permite visualizar y actualizar el estado de limpieza, alimentación y medicación.
 * Utiliza DataBinding, LiveData y ViewModel.
 */
class HabitatActivity : AppCompatActivity() {

    // Enlace con el layout XML (activity_habitat.xml) a través de Data Binding
    private lateinit var binding: ActivityHabitatBinding

    // ViewModel que contiene y gestiona la lógica de estado del hábitat
    private lateinit var viewModel: HabitatViewModel

    /**
     * Se ejecuta al crear la actividad.
     * Configura el layout, recupera parámetros del Intent, y vincula el ViewModel.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Extiende el contenido detrás de la barra de estado (opcional visual)

        // Carga el layout usando Data Binding y lo vincula al ciclo de vida
        binding = DataBindingUtil.setContentView(this, R.layout.activity_habitat)
        binding.lifecycleOwner = this

        // Recupera el nombre del hábitat desde el Intent
        val habitatName = intent.getStringExtra("HABITAT_NAME") ?: ""

        // Crea el ViewModel usando una Factory que recibe el nombre del hábitat
        val factory = es.uam.eps.dadm.faunary.viewmodel.HabitatViewModelFactory(application, habitatName)
        viewModel = ViewModelProvider(this, factory).get(HabitatViewModel::class.java)

        // Vincula el ViewModel al layout
        binding.viewModel = viewModel

        Timber.i("onCreate called")
    }

    // Métodos del ciclo de vida con logs útiles para depuración o métricas
    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        // Refresca los datos del ViewModel cada vez que la actividad entra en primer plano
        viewModel.forzarActualizacion()
        binding.invalidateAll() // Fuerza refresco visual del binding
        Timber.i("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy called")
    }
}
