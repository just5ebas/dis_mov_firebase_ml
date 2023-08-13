package com.grupal.proyectofinal.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.grupal.proyectofinal.R
import com.grupal.proyectofinal.databinding.ActivityIdiomaTextoBinding
import com.grupal.proyectofinal.databinding.ActivityMainBinding
import java.util.Locale

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
                    if (languageCode == "und") {
                        Toast.makeText(
                            baseContext,
                            "No se puede identificar el idioma",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        val nombreLenguaje = getLanguageName(languageCode)
                        Toast.makeText(
                            baseContext,
                            "Idioma $nombreLenguaje",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
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

    private fun getLanguageName(languageCode: String): String {
        val locale = Locale(languageCode)
        return locale.displayLanguage
    }


}