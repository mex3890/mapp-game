package br.com.mappgame.models

data class PatientAnswersResponse(
    val status: Boolean,
    val patient: Patient,
    val count_of_games: Int,
    val answers: String,
    val error: String
)