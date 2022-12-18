package br.com.mappgame.models

// Case the response keys don't mach with the attribute names, need use @SerializedName("name_in_response")
data class PatientAnswersResponse(
    val status: Boolean,
    val patient: Patient,
    val count_of_games: Int,
    val answers: Array<Answer>
)