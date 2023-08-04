import android.content.Context
import android.widget.Toast
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.proyecto_movil_crisol.R

fun Context.mensaje(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}