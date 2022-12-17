package com.example.app_redes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_mascota.view.*

class MascotaAdapter(private val adapterContext:Context, private val listaMascotaAdopcion: List<MascotaAdopcion>):ArrayAdapter<MascotaAdopcion>(adapterContext, 0, listaMascotaAdopcion) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(adapterContext).inflate(R.layout.item_mascota, parent, false)

        val mascota = listaMascotaAdopcion[position]

        layout.nombre.text = mascota.nombre
        layout.raza.text   = mascota.raza

        return layout
    }
}