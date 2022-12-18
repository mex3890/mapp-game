package br.com.mappgame.models

// Case the response keys don't mach with the attribute names, need use @SerializedName("name_in_response")
data class Patient(
    val id: Int,
    val name: String,
    val birth_date: String,
    val user_id: Int,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String
)