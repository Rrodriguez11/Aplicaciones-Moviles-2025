package es.uam.eps.dadm.faunary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityHabitatSelectionBinding

class HabitatSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitatSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_habitat_selection)

        val habitatList = es.uam.eps.dadm.faunary.data.DataRepository.getTodosLosRecintos()

        habitatList.forEach { recinto ->
            val button = Button(this).apply {
                text = recinto.nombre
                textSize = 20f
                setPadding(32, 20, 32, 20)
                setOnClickListener {
                    val intent = Intent(this@HabitatSelectionActivity, HabitatActivity::class.java)
                    intent.putExtra("HABITAT_NAME", recinto.nombre)
                    startActivity(intent)
                }
            }
            binding.habitatButtonsContainer.addView(button)
        }

    }
}