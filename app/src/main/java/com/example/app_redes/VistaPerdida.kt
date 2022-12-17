package com.example.app_redes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_vista_mascota.*
import kotlinx.android.synthetic.main.activity_vista_mascota.correo
import kotlinx.android.synthetic.main.activity_vista_mascota.descripcion
import kotlinx.android.synthetic.main.activity_vista_mascota.edadMascota
import kotlinx.android.synthetic.main.activity_vista_mascota.nombreMascota
import kotlinx.android.synthetic.main.activity_vista_perdida.*
import kotlinx.android.synthetic.main.item_mascota_perdida.*
import kotlinx.android.synthetic.main.item_mascota_perdida.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

class VistaPerdida : AppCompatActivity() {
    val url = "http://192.168.43.225:3000/usuario/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_perdida)

        Toast.makeText(this, "Wait a second", Toast.LENGTH_LONG).show()

        lifecycleScope.launch {

            val mascota = intent.getSerializableExtra("mascota") as MascotaPerdida

            nombreMascota.text = mascota.nombre
            edadMascota.text = mascota.edad.toString()
            descripcion.text = mascota.descripcion
            fecha_desaparicion.text = mascota.fechaDesaparicion

            correo.text = getContacto(mascota.userID.toString())
        }
    }
    suspend fun getContacto(userID:String):String{
        var correo:String = ""
        val request = StringRequest(
            Request.Method.GET,
            url+userID,
            {response->
                val data = response.toString()
                var jsonObject= JSONObject(data)
                correo = jsonObject.getString("correo")
                Log.e("Correo:",jsonObject.getString("correo"))

            },
            {error-> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                Log.e("Error", error.toString())
            }
        )
        val rQueue = Volley.newRequestQueue(this@VistaPerdida)
        rQueue.add(request)
        delay(1000L)
        return correo
    }
}