package com.example.app_redes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.*
import org.json.JSONArray
import java.nio.charset.Charset
import kotlin.coroutines.CoroutineContext
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity(){
    var url         = "http://192.168.43.225:3000/mascotas/adopcion"
    var urlBusqueda = "http://192.168.43.225:3000/mascota/adopcion/"
    val listaMascotaAdopcion = mutableListOf<MascotaAdopcion>()

    private lateinit var busqueda: Button
    private lateinit var mascotaBusqueda: EditText

    var mascota = MascotaAdopcion("", "", 0, "", "", 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "Wait a second", Toast.LENGTH_LONG).show()

        mascotaBusqueda = findViewById(R.id.busquedaAdopcion)
        busqueda = findViewById(R.id.buscarAdopcion)

        val btnBuscar   : Button = findViewById(R.id.buscarAdopcion)
        val btnPerdidos : Button = findViewById(R.id.perdidos)
        val btnAdopcion : Button = findViewById(R.id.adopcion)

        lifecycleScope.launch{
            getMascotasAdopcion()

            lista.adapter = MascotaAdapter(this@MainActivity, listaMascotaAdopcion)


            lista.setOnItemClickListener{ parent, view, position, id->

                val intent = Intent(this@MainActivity, VistaMascota::class.java)
                intent.putExtra("mascota", listaMascotaAdopcion[position])
                startActivity(intent)
            }


        }

        btnBuscar.setOnClickListener{
            lifecycleScope.launch {
                getMascotaAdopcion()
                val intent = Intent(this@MainActivity, VistaMascota::class.java)
                intent.putExtra("mascota", mascota)
                startActivity(intent)
            }
        }

        btnPerdidos.setOnClickListener{
            Toast.makeText(this, "Lost friends!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@MainActivity, lost_friends::class.java)
            startActivity(intent)
        }

        btnAdopcion.setOnClickListener {
            Toast.makeText(this, "Find ur mate!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    suspend fun getMascotasAdopcion(){
        val request = StringRequest(
            Request.Method.GET,
            url,
            {response->
                val data = response.toString()
                var JArray = JSONArray(data)

                for(i in 0..JArray.length()-1){
                    var jsonObject = JArray.getJSONObject(i)
                    Log.e("Nombre:",jsonObject.getString("Nombre"))
                    val mascota = MascotaAdopcion(jsonObject.getString("Nombre"), jsonObject.getString("Raza"), jsonObject.getInt("Edad"), jsonObject.getString("Color"), jsonObject.getString("Descripcion"), jsonObject.getInt("Id_Usuario"))
                    listaMascotaAdopcion.add(mascota)
                }
            },
            {error-> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                Log.e("Error", error.toString())
            }
        )
        val rQueue = Volley.newRequestQueue(this@MainActivity)
        rQueue.add(request)
        delay(1000L)
    }

    suspend fun getMascotaAdopcion(){
        val request = StringRequest(
            Request.Method.GET,
            urlBusqueda+mascotaBusqueda.text,
            {response->
                val data = response.toString()
                var jsonObject = JSONObject(data)

                mascota.nombre      = jsonObject.getString("Nombre")
                mascota.raza        = jsonObject.getString("Raza")
                mascota.edad        = jsonObject.getInt("Edad")
                mascota.color       = jsonObject.getString("Color")
                mascota.descripcion = jsonObject.getString("Descripcion")
                mascota.userID      = jsonObject.getInt("Id_Usuario")
                Log.e("Mascota Encontrada: ", mascota.nombre)
            },
            {error-> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                Log.e("Error", error.toString())
            }
        )
        val rQueue = Volley.newRequestQueue(this@MainActivity)
        rQueue.add(request)
        delay(1000L)
    }
}