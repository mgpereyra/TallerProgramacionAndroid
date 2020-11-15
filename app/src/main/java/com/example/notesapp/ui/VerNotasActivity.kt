package com.example.notesapp.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.databinding.ActivityVerNotasBinding
import org.koin.android.viewmodel.ext.android.viewModel

class VerNotasActivity : AppCompatActivity() {

    private lateinit var adapter: NotasAdapter
    private lateinit var binding: ActivityVerNotasBinding
    private val viewModelVer: VerNotasViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerNotasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = NotasAdapter()
        validarPermisos()
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@VerNotasActivity, LinearLayoutManager.VERTICAL, false)
            this.adapter = this@VerNotasActivity.adapter
        }
        viewModelVer.notasLiveData.observe(this, Observer {
            adapter.submitList(it.reversed(), viewModelVer, this)
            adapter.notifyDataSetChanged()
        })
        binding.btnVolver.setOnClickListener { onBackPressed() }
    }

    override fun onResume(){
        super.onResume()
        viewModelVer.actualizarLista()
    }

    private fun validarPermisos(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permisosCamara = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permisosCamara,101)
        }
    }
}