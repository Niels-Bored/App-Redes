package com.example.app_redes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class lost_friends : AppCompatActivity() {
    var url                 = "http://192.168.43.225:3000/mascotas/perdida"
    var urlBusqueda         = "http://192.168.43.225:3000/mascota/perdida/"
    val listaMascotaPerdida = mutableListOf<MascotaPerdida>()
    private lateinit var busqueda: Button
    private lateinit var mascotaBusqueda: EditText

    var mascota = MascotaPerdida("", "", 0, "", "", 0, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_friends)

        Toast.makeText(this, "Wait a second", Toast.LENGTH_LONG).show()

        mascotaBusqueda = findViewById(R.id.busquedaPerdida)
        busqueda = findViewById(R.id.buscarPerdida)

        val btnBuscar   : Button = findViewById(R.id.buscarPerdida)
        val btnPerdidos : Button = findViewById(R.id.perdidos)
        val btnAdopcion : Button = findViewById(R.id.adopcion)

        lifecycleScope.launch{
            getMascotasPerdidas()

            lista.adapter = PerdidaAdapter(this@lost_friends, listaMascotaPerdida)


            lista.setOnItemClickListener{ parent, view, position, id->

                val intent = Intent(this@lost_friends, VistaPerdida::class.java)
                intent.putExtra("mascota", listaMascotaPerdida[position])
                startActivity(intent)
            }


        }
        btnBuscar.setOnClickListener{
            lifecycleScope.launch{
                getMascotaPerdida()
                val intent = Intent(this@lost_friends, VistaPerdida::class.java)
                intent.putExtra("mascota", mascota)
                startActivity(intent)
            }
        }

        btnPerdidos.setOnClickListener{
            Toast.makeText(this, "Lost friends!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@lost_friends, lost_friends::class.java)
            startActivity(intent)
        }

        btnAdopcion.setOnClickListener {
            Toast.makeText(this, "Find ur mate!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@lost_friends, MainActivity::class.java)
            startActivity(intent)
        }
    }

    suspend fun getMascotasPerdidas(){
        val request = StringRequest(
            Request.Method.GET,
            url,
            {response->
                val data = response.toString()
                var JArray = JSONArray(data)

                for(i in 0..JArray.length()-1){
                    var jsonObject = JArray.getJSONObject(i)
                    Log.e("Nombre:",jsonObject.getString("Nombre"))
                    val mascota = MascotaPerdida(jsonObject.getString("Nombre"), jsonObject.getString("Raza"), jsonObject.getInt("edad"), jsonObject.getString("Color"), jsonObject.getString("Descripcion"), jsonObject.getInt("Id_Usuario"), jsonObject.getString("fecha_desaparicion"))
                    listaMascotaPerdida.add(mascota)
                }
            },
            {error-> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                Log.e("Error", error.toString())
            }
        )
        val rQueue = Volley.newRequestQueue(this@lost_friends)
        rQueue.add(request)
        delay(1000L)
    }

    suspend fun getMascotaPerdida(){
        val request = StringRequest(
            Request.Method.GET,
            urlBusqueda+mascotaBusqueda.text,
            {response->
                val data = response.toString()
                var jsonObject = JSONObject(data)

                mascota.nombre      = jsonObject.getString("Nombre")
                mascota.raza        = jsonObject.getString("Raza")
                mascota.edad        = jsonObject.getInt("edad")
                mascota.color       = jsonObject.getString("Color")
                mascota.descripcion = jsonObject.getString("Descripcion")
                mascota.userID      = jsonObject.getInt("Id_Usuario")
                mascota.fechaDesaparicion = jsonObject.getString("fecha_desaparicion")
            },
            {error-> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                Log.e("Error", error.toString())
            }
        )
        val rQueue = Volley.newRequestQueue(this@lost_friends)
        rQueue.add(request)
        delay(1000L)
    }
}