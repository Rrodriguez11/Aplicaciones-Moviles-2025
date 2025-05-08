package es.uam.eps.dadm.faunary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Actividad que representa la sección del usuario.
 * Actualmente solo carga un layout vacío con soporte para diseño de borde a borde.
 */
class UserActivity : AppCompatActivity() {

    /**
     * Método llamado al crear la actividad.
     * Configura el layout y ajusta los márgenes del sistema.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita que el contenido ocupe toda la pantalla, detrás de barras de sistema
        enableEdgeToEdge()

        // Establece el layout asociado a esta actividad
        setContentView(R.layout.activity_user)

        // Aplica padding dinámico para evitar que el contenido se superponga a la barra de estado o navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
