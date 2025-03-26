package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import es.uam.eps.dadm.faunary.R

class HabitatViewModel(application: Application) : AndroidViewModel(application) {

    val foodType = MutableLiveData<Int>(R.string.meat)
    val fedCount = MutableLiveData<Int>(11)
    val totalCount = MutableLiveData<Int>(12)
    val medicatedCount = MutableLiveData<Int>(3)
    val sickCount = MutableLiveData<Int>(3)

    val cleanDelayDays = MutableLiveData<Int>(2)
    val cleanFrequency = MutableLiveData<Int>(4)
    val cleaningLabel = MutableLiveData<String>()

    private val _cleaningDone = MutableLiveData(false)
    val cleaningDone: LiveData<Boolean> get() = _cleaningDone

    private val _showCleaningToast = MutableLiveData<Boolean>()
    val showCleaningToast: LiveData<Boolean> get() = _showCleaningToast

    init {
        updateCleaningLabel()
    }

    private fun updateCleaningLabel() {
        val done = cleaningDone.value ?: false
        val context = getApplication<Application>()
        val text = if (done) {
            context.getString(R.string.clean_next, cleanFrequency.value)
        } else {
            context.getString(R.string.clean_delay, cleanDelayDays.value)
        }
        cleaningLabel.value = text
    }

    fun markCleaningDone() {
        _cleaningDone.value = true
        _showCleaningToast.value = true
        updateCleaningLabel()
    }

    fun resetCleaningToast() {
        _showCleaningToast.value = false
    }
}