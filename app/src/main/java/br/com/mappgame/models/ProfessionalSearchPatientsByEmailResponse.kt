package br.com.mappgame.models

// Case the response keys don't mach with the attribute names, need use @SerializedName("name_in_response")
data class ProfessionalSearchPatientsByEmailResponse(
    val status: Boolean,
    val patients: List<Patient>,
    val error: String,
    val message: String,
)