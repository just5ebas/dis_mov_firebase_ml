package com.grupal.proyectofinal.ml.logic

import java.util.Locale

class IdentificadorLenguaje {

    fun obtenerNombreLenguaje(codigoLenguaje: String): String {
        return Locale(codigoLenguaje).displayLanguage
    }

}
