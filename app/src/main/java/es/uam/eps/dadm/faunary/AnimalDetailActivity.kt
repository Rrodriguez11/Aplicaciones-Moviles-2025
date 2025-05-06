package es.uam.eps.dadm.faunary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityAnimalDetailBinding
import es.uam.eps.dadm.faunary.model.Animal

class AnimalDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ANIMAL = "animal_extra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityAnimalDetailBinding>(
            this,
            R.layout.activity_animal_detail
        )

        binding.animal = intent.getSerializableExtra(EXTRA_ANIMAL) as Animal
        binding.activity = this  // Para que funcione activity.finish() en el XML

        binding.backToSummaryButton.setOnClickListener {
            finish()
        }
    }
}
