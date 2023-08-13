package com.grupal.proyectofinal.ui.activities


import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.mlkit.nl.languageid.LanguageIdentification

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions

import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.grupal.proyectofinal.databinding.ActivityMainBinding

import java.util.Arrays
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var textRecognizerEnglish: TextRecognizer
    private lateinit var textRecognizerChinese: TextRecognizer

    private val recognizerEnglish = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textRecognizerEnglish = recognizerEnglish

        binding.captureButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            recognizeText(imageBitmap)
        }
    }

    private fun recognizeText(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)

        textRecognizerEnglish.process(image)
            .addOnSuccessListener { visionText ->

                val recognizedText = processTextRecognitionResult(visionText)

                Log.d("UCE", recognizedText)

                val languageIdentifier = LanguageIdentification.getClient()

                // Identifica un solo idioma
                languageIdentifier.identifyLanguage(recognizedText)
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


//                binding.textView.text = recognizedText
//                Toast.makeText(
//                    baseContext,
//                    "Idioma Ingles.",
//                    Toast.LENGTH_SHORT,
//                ).show()
//
//            }
//            .addOnFailureListener { e ->
//                if(e is FirebaseException){
//                    Toast.makeText(
//                        baseContext,
//                        "Ha ocurrido un error.",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                }else {
//
//                    Toast.makeText(
//                        baseContext,
//                        "Idioma Desconocido.",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                }
            }




    }

    private fun processTextRecognitionResult(visionText: com.google.mlkit.vision.text.Text): String {
        val recognizedText = StringBuilder()
        for (block in visionText.textBlocks) {
            for (line in block.lines) {
                for (element in line.elements) {
                    recognizedText.append(element.text).append(" ")
                }
            }
            recognizedText.append("\n")
        }


        return recognizedText.toString()
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private fun getLanguageName(languageCode: String): String {
        val locale = Locale(languageCode)
        return locale.displayLanguage
    }
}




