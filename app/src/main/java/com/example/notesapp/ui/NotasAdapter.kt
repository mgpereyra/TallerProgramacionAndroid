package com.example.notesapp.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.model.Nota
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notas_items.view.*

class NotasAdapter : RecyclerView.Adapter<ViewHolder>() {

    lateinit var context: Context
    var listaNotas: MutableList<Nota> = mutableListOf()
    lateinit var VM: VerNotasViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.notas_items,parent,false))
    }

    override fun getItemCount(): Int {
        return this.listaNotas.size
    }

    fun submitList(it: List<Nota>, viewModelVer: VerNotasViewModel, contextVerNotas: Context) {
        context = contextVerNotas
        VM = viewModelVer
        listaNotas.clear()
        listaNotas.addAll(it)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.description.setText(listaNotas.get(position).description)
        holder.imageView.loadUrl(listaNotas.get(position).srcImagen)
        holder.botonBorrarNota.setOnClickListener { borrarNota(Nota((listaNotas.get(position).id),
                                                               (listaNotas.get(position).description),
                                                                    (listaNotas.get(position).srcImagen)))}
        holder.botonModificarNota.setOnClickListener {
                                    val intent = Intent(context, CrearNotasActivity::class.java)
                                    intent.putExtra("id",(listaNotas.get(position).id))
                                    intent.putExtra("desc",(listaNotas.get(position).description))
                                    intent.putExtra("srcImg",(listaNotas.get(position).srcImagen))
                                    context.startActivity(intent) }
    }

    private fun borrarNota(nota: Nota) {
        VM.borrarNota(nota)
    }

    private fun ImageView.loadUrl(url: String) {
        Picasso.get().load(url).into(imageView)
    }
}