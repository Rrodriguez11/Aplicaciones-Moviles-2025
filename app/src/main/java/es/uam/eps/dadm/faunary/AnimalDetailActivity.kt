package es.uam.eps.dadm.faunary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import es.uam.eps.dadm.faunary.databinding.ActivityAnimalDetailBinding

class AnimalDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityAnimalDetailBinding>(
            this,
            R.layout.activity_animal_detail
        )

        binding.backToSummaryButton.setOnClickListener {
            finish()
        }
    }
}
