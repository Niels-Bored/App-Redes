package com.example.app_redes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_mascota.view.*
import kotlinx.android.synthetic.main.item_mascota.view.nombre
import kotlinx.android.synthetic.main.item_mascota.view.raza
import kotlinx.android.synthetic.main.item_mascota_perdida.view.*

class PerdidaAdapter (private val adapterContext:Context, private val listaMascotaPerdida: List<MascotaPerdida>):ArrayAdapter<MascotaPerdida>(adapterContext, 0, listaMascotaPerdida) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(adapterContext).inflate(R.layout.item_mascota_perdida, parent, false)

        val mascota = listaMascotaPerdida[position]

        layout.nombre.text = mascota.nombre
        layout.raza.text   = mascota.raza
        layout.fecha.text = mascota.fechaDesaparicion

        return layout
    }
}