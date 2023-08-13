package com.grupal.proyectofinal.ui.modelo

class Idioma {

    lateinit var codigoIdioma: String
    lateinit var tituloIdioma: String

   constructor(codigoIdioma: String, tituloIdioma: String){
       this.codigoIdioma=codigoIdioma
       this.tituloIdioma=tituloIdioma
   }
}