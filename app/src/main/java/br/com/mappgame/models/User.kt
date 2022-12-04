package br.com.mappgame.models

// Case the response keys don't mach with the attribute names, need use @SerializedName("name_in_response")
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String
)