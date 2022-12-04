package br.com.mappgame.models

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val token: String,
    val error: String,
    val user: User
)