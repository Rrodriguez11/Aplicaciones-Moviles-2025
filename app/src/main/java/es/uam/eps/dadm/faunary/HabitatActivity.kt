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

    // Enlace al layout activity_habitat.xml mediante Data Binding
    private lateinit var binding: ActivityHabitatBinding

    // ViewModel que gestiona los datos del hábitat y su lógica
    private lateinit var viewModel: HabitatViewModel

    /**
     * Método llamado al crear la actividad.
     * Configura el DataBinding, el ViewModel y observa eventos de UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Infla el layout usando DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_habitat)
        binding.lifecycleOwner = this

        // Recupera el nombre del hábitat desde el Intent
        val habitatName = intent.getStringExtra("HABITAT_NAME") ?: ""

        // Usa el ViewModelFactory para pasar el nombre al ViewModel
        val factory = es.uam.eps.dadm.faunary.viewmodel.HabitatViewModelFactory(application, habitatName)
        viewModel = ViewModelProvider(this, factory).get(HabitatViewModel::class.java)

        binding.viewModel = viewModel


        Timber.i("onCreate called")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        viewModel.forzarActualizacion()
        binding.invalidateAll()
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