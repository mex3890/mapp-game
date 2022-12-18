package br.com.mappgame.models

data class UserPatientsIndexResponse(
    val status: Boolean,
    val patients: List<Patient>
)