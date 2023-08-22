package com.grupal.proyectofinal.ml.ui.activities


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.grupal.proyectofinal.databinding.ActivityMainBinding
import com.grupal.proyectofinal.ml.logic.IdentificadorLenguaje

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private var ultimoIdioma: String = "und"

    private lateinit var firebaseImage : String
    private lateinit var firebaseImage2 : String
    private lateinit var firebaseImage3 : String
    private lateinit var firebaseImage4 : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //g1
        firebaseImage = binding.imagenF1.toString()
        var url1 = "https://media4.giphy.com/media/NS1agLkVGWjuYjhoQC/source.gif"
        var urlparse1 : Uri = Uri.parse(url1)
        Glide.with(this).load(urlparse1).into(binding.imagenF1)

        //g2
        firebaseImage2 = binding.imagenF2.toString()
        var url2 = "https://raw.githubusercontent.com/StewartGF/StewartGF/master/images/firebase.gif"
        var urlparse2 : Uri = Uri.parse(url2)
        Glide.with(this).load(urlparse2).into(binding.imagenF2)

        //g3
        firebaseImage3 = binding.imagenF3.toString()
        var url3 = "https://firebase.google.com/static/images/community/stories/campfire.gif"
        var urlparse3 : Uri = Uri.parse(url3)
        Glide.with(this).load(urlparse3).into(binding.imagenF3)

        //g4
        firebaseImage4 = binding.imagenF4.toString()
        var url4 = "https://pa1.aminoapps.com/7672/839525d1cd346f59c481c2fdc94638599357bedcr1-223-240_hq.gif"
        var urlparse4 : Uri = Uri.parse(url4)
        Glide.with(this).load(urlparse4).into(binding.imagenF4)

        binding.captureButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1)
        }

        binding.verIdioma.setOnClickListener {
            gestionSnackbars(ultimoIdioma)
        }

        binding.traductor.setOnClickListener {
            startActivity(Intent(this, TraductorTexto::class.java))
        }

        binding.detector.setOnClickListener {
            startActivity(Intent(this, IdiomaTexto::class.java))
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

        textRecognizer.process(image)
            .addOnSuccessListener { visionText ->

                val recognizedText = processTextRecognitionResult(visionText)

                binding.textView.text = recognizedText

                val languageIdentifier = LanguageIdentification.getClient()

                identificarLenguaje(languageIdentifier, recognizedText)
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

    private fun identificarLenguaje(languageIdentifier: LanguageIdentifier, text: String) {
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                ultimoIdioma = languageCode
                gestionSnackbars(ultimoIdioma)
            }
            .addOnFailureListener {
                Toast.makeText(
                    baseContext,
                    "No se puede identificar el idioma",
                    Toast.LENGTH_SHORT,
                ).show()
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

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

}




