package com.example.cme


data class Item(val id:Int, val imagem:String, val nome:String, val preco:Float, val precoUnidade:Float, val precoPromocao:Float, val precoPromocaoUnidade:Float, val promocao:Boolean, var quantidade:Int, var favorito:Boolean)