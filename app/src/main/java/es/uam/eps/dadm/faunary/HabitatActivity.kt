package es.uam.eps.dadm.faunary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityHabitatBinding
import es.uam.eps.dadm.faunary.viewmodel.HabitatViewModel

import android.widget.Toast

class HabitatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitatBinding
    private lateinit var viewModel: HabitatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_habitat)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(HabitatViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.showCleaningToast.observe(this) { show ->
            if (show == true) {
                Toast.makeText(this, R.string.clean_success_toast, Toast.LENGTH_SHORT).show()
                viewModel.resetCleaningToast()
            }
        }
    }
}