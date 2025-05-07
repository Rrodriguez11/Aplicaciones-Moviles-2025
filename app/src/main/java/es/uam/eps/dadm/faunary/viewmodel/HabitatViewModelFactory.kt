package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HabitatViewModelFactory(
    private val application: Application,
    private val habitatName: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitatViewModel(application, habitatName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
