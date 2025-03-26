package es.uam.eps.dadm.faunary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitatViewModel : ViewModel() {

    private val _cleaningDone = MutableLiveData(false)
    val cleaningDone: LiveData<Boolean> get() = _cleaningDone

    fun markCleaningDone() {
        _cleaningDone.value = true
    }

    fun resetCleaningStatus() {
        _cleaningDone.value = false
    }
}