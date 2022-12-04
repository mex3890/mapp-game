package br.com.mappgame.models

data class DefaultResponse(
    val status: Boolean,
    val message: String,
    val token: String,
    val error: String
)