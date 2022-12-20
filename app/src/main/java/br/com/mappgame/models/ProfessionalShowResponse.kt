package br.com.mappgame.models

// Case the response keys don't mach with the attribute names, need use @SerializedName("name_in_response")
data class ProfessionalShowResponse(
    val status: Boolean,
    val professional: Professional
)