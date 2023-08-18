package com.grupal.proyectofinal.ml.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.grupal.proyectofinal.databinding.ActivityIdiomaTextoBinding
import com.grupal.proyectofinal.ml.logic.IdentificadorLenguaje

class IdiomaTexto : AppCompatActivity() {

    private lateinit var binding: ActivityIdiomaTextoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIdiomaTextoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.btnTraducir.setOnClickListener {
            val languageIdentifier = LanguageIdentification.getClient()

            // Identifica un solo idioma
            languageIdentifier.identifyLanguage(binding.txtTraduccion.text.toString())
                .addOnSuccessListener { languageCode ->
                    gestionSnackbars(languageCode)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        baseContext,
                        "No se puede identificar el idioma",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

            //Para varios idiomas
//            languageIdentifier.identifyPossibleLanguages(text)
//                .addOnSuccessListener { identifiedLanguages ->
//                    for (identifiedLanguage in identifiedLanguages) {
//                        val language = identifiedLanguage.languageTag
//                        val confidence = identifiedLanguage.confidence
//                        Log.i(TAG, "$language $confidence")
//                    }
//                }
//                .addOnFailureListener {
//                    // Model couldn’t be loaded or other internal error.
//                    // ...
//                }
        }
    }

    private fun gestionSnackbars(codigoLenguaje: String) {
        if (codigoLenguaje == "und") {
            Toast.makeText(
                baseContext,
                "No se puede identificar el idioma",
                Toast.LENGTH_SHORT,
            ).show()
        } else {
            val nombreLenguaje = IdentificadorLenguaje().obtenerNombreLenguaje(codigoLenguaje)
            Toast.makeText(
                baseContext,
                "Idioma $nombreLenguaje",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

}