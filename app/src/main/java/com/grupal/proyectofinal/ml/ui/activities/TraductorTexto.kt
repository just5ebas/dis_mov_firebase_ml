package com.grupal.proyectofinal.ml.ui.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

import com.grupal.proyectofinal.databinding.ActivityTraductorTextoBinding
import com.grupal.proyectofinal.ml.data.Idioma
import java.util.Locale

class TraductorTexto : AppCompatActivity() {

    private lateinit var binding: ActivityTraductorTextoBinding

    private lateinit var idiomas: ArrayList<Idioma>

    private var codigoIdiomaOrigen: String = "es"
    private var tituloIdiomaOrigen: String = "Español"
    private var codigoIdiomaDestino: String = "en"
    private var tituloIdiomaDestino: String = "Inglés"

    private lateinit var progressDialog: ProgressDialog

    private lateinit var translatorOption: TranslatorOptions
    private lateinit var translator: Translator
    private var textoIdiomaOrigen: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraductorTextoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idiomasDisponibles()
        iniciar()

        binding.elegirIdioma.setOnClickListener {
//            Toast.makeText(
//                baseContext,
//                "Elegir idioma",
//                Toast.LENGTH_SHORT,
//            ).show()

            elegirIdiomaOrigen()
        }

        binding.IdiomaElegido.setOnClickListener {
//            Toast.makeText(
//                baseContext,
//                "Idioma elegido",
//                Toast.LENGTH_SHORT,
//            ).show()

            elegirIdiomaDestino()
        }

        binding.btnTraducir.setOnClickListener {
            ValidarDatos();
        }
    }

    private fun ValidarDatos() {
        textoIdiomaOrigen = binding.idiomaOrigen.text.toString().trim()

        if (textoIdiomaOrigen.isEmpty()) {
            Toast.makeText(
                baseContext,
                "Ingrese texto",
                Toast.LENGTH_SHORT,
            ).show()
        } else {
            traducirTexto()
        }
    }

    private fun traducirTexto() {
        progressDialog.setMessage("Procesando")
        progressDialog.show()
        translatorOption = TranslatorOptions.Builder().setSourceLanguage(codigoIdiomaOrigen)
            .setTargetLanguage(codigoIdiomaDestino).build()

        translator = Translation.getClient(translatorOption)

        var downloadConditions: DownloadConditions =
            DownloadConditions.Builder().requireWifi().build()

        translator.downloadModelIfNeeded(downloadConditions)
            .addOnSuccessListener {
                progressDialog.setMessage("Traduciendo Texto de $tituloIdiomaOrigen a $tituloIdiomaDestino")
                translator.translate(textoIdiomaOrigen)
                    .addOnSuccessListener {
                        binding.IdiomaDestino.text = it
                        progressDialog.dismiss()
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(
                            baseContext,
                            "Parece que ha habido un error",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(
                    baseContext,
                    "Parece que ha habido un error",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    private fun iniciar() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)
    }

    private fun idiomasDisponibles() {
        idiomas = ArrayList()
        var listaCodigoIdioma: List<String> = TranslateLanguage.getAllLanguages()

        for (codLenguaje in listaCodigoIdioma) {
            var tituloLenguaje: String = Locale(codLenguaje).displayLanguage

            var modeloIdioma = Idioma(codLenguaje, tituloLenguaje)
            idiomas.add(modeloIdioma)
        }
    }

    private fun elegirIdiomaOrigen() {
        var popuMenu = PopupMenu(this, binding.elegirIdioma)

        for (i in 0 until idiomas.size) {
            popuMenu.menu.add(Menu.NONE, i, i, idiomas[i].tituloIdioma)
        }
        popuMenu.show()
        popuMenu.setOnMenuItemClickListener { menuItem ->
            val posicion: Int = menuItem.itemId

            codigoIdiomaOrigen = idiomas[posicion].codigoIdioma
            tituloIdiomaOrigen = idiomas[posicion].tituloIdioma

            binding.elegirIdioma.text = tituloIdiomaOrigen
            binding.idiomaOrigen.hint = "Ingrese texto en: $tituloIdiomaOrigen"
            false
        }

    }

    private fun elegirIdiomaDestino() {
        var popuMenu = PopupMenu(this, binding.IdiomaElegido)

        for (i in 0 until idiomas.size) {
            popuMenu.menu.add(Menu.NONE, i, i, idiomas[i].tituloIdioma)
        }
        popuMenu.show()
        popuMenu.setOnMenuItemClickListener { menuItem ->
            val posicion: Int = menuItem.itemId

            codigoIdiomaDestino = idiomas[posicion].codigoIdioma
            tituloIdiomaDestino = idiomas[posicion].tituloIdioma

            binding.IdiomaElegido.text = tituloIdiomaDestino

            false
        }
    }
}








