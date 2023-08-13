package com.grupal.proyectofinal.ui.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

import com.grupal.proyectofinal.databinding.ActivityTraductorTextoBinding
import com.grupal.proyectofinal.ui.modelo.Idioma
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
            Toast.makeText(
                baseContext,
                "Elegir idioma",
                Toast.LENGTH_SHORT,
            ).show()

            elegirIdiomaOrigen()
        }

        binding.IdiomaElegido.setOnClickListener {
            Toast.makeText(
                baseContext,
                "idioma elegido",
                Toast.LENGTH_SHORT,
            ).show()

            elegirIdiomaDestino()
        }

        binding.btnTraducir.setOnClickListener {
            ValidarDatos();
        }


    }

    private fun ValidarDatos() {
        textoIdiomaOrigen = binding.idiomaOrigen.text.toString().trim()
        Log.d("UCE3", "---$textoIdiomaOrigen")

        if (textoIdiomaOrigen.isEmpty()) {
            Toast.makeText(
                baseContext,
                "Ingrese Texto",
                Toast.LENGTH_SHORT,
            ).show()

        } else {
            traducirTexto()
        }


    }

    private fun traducirTexto() {
        progressDialog.setMessage("Procesasndo")
        progressDialog.show()
        translatorOption = TranslatorOptions.Builder().setSourceLanguage(codigoIdiomaOrigen)
            .setTargetLanguage(codigoIdiomaDestino).build()

        translator = Translation.getClient(translatorOption)

        var downloadConditions: DownloadConditions =
            DownloadConditions.Builder().requireWifi().build()

        translator.downloadModelIfNeeded(downloadConditions).addOnSuccessListener {
            Log.d("DISMISS", "paquete descargado con exito")
            progressDialog.setMessage("Traduciendo Texto")
            translator.translate(textoIdiomaOrigen).addOnSuccessListener {
                Log.d("DISMISS", "Se ha logrado con exito  $it")
                binding.IdiomaDestino.text = it
            }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Log.d("DISMISS", "onFailure $it")
                    Toast.makeText(
                        baseContext,
                        "Oh no!!",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

        }.addOnFailureListener {
            progressDialog.dismiss()
            Log.d("DISMISS", "onFailure $it")
            Toast.makeText(
                baseContext,
                "Oh no!!",
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

        for (codLenguje in listaCodigoIdioma) {
            var tituloLenguaje: String = Locale(codLenguje).displayLanguage

            //  Log.d("UCE", "idioma registro $codLenguje")
            //  Log.d("UCE", "idioma registro $tituloLenguaje")

            var modeloIdioma = Idioma(codLenguje, tituloLenguaje)
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

            Log.d("UCE", "idioma registro $codigoIdiomaOrigen")
            Log.d("UCE", "idioma registro $tituloIdiomaOrigen")


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


            Log.d("UCE2", "idioma registro $codigoIdiomaDestino")
            Log.d("UCE2", "idioma registro $tituloIdiomaDestino")


            false
        }

    }
}








