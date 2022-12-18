package br.com.mappgame.models

// Case the response keys don't mach with the attribute names, need use @SerializedName("name_in_response")
data class Answer(
    val created_at: String,
    val hits: Int,
    val errors: Int
)