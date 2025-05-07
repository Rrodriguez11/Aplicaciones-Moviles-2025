package es.uam.eps.dadm.faunary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityHabitatSelectionBinding
import es.uam.eps.dadm.faunary.viewmodel.HabitatSelectionViewModel

class HabitatSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitatSelectionBinding
    private val viewModel: HabitatSelectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_habitat_selection)


        viewModel.habitats.observe(this) { habitatList ->
            binding.habitatButtonsContainer.removeAllViews()

            habitatList.forEach { habitat ->
                val button = Button(this).apply {
                    text = habitat.nombre
                    textSize = 20f
                    setPadding(32, 20, 32, 20)
                    setOnClickListener {
                        val intent = Intent(this@HabitatSelectionActivity, HabitatActivity::class.java)
                        intent.putExtra("HABITAT_ID", habitat.id)
                        startActivity(intent)
                    }
                }
                binding.habitatButtonsContainer.addView(button)
            }
        }

    }
}
