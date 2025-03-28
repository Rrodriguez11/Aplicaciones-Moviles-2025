package es.uam.eps.dadm.faunary

import android.app.Application
import timber.log.Timber

class FaunaryApplication: Application() {
    override fun onCreate(){
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}