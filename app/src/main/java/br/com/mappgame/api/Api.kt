package br.com.mappgame.api

import br.com.mappgame.models.DefaultResponse
import br.com.mappgame.models.LoginResponse
import br.com.mappgame.models.PatientAnswersResponse
import br.com.mappgame.models.UserPatientsIndexResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.Date

interface Api {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("phone") phone: String
    ):retrofit2.Call<DefaultResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @PUT("user/update")
    fun updateUser(
        @Field("id") id: Int,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("password_confirmation") passwordConfirmation: String
    ):retrofit2.Call<DefaultResponse>

    @GET("patients/index/{user_id}")
    fun userPatients(
        @Path("user_id") number: Int
    ):Call<UserPatientsIndexResponse>

    @FormUrlEncoded
    @POST("patients/store/{user_id}")
    fun storePatient(
        @Field("name") name: String,
        @Field("birth_date") birth_date: String,
        @Field("user_id") user_id: Int
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("answers")
    fun answersUser(
        @Field("answers") answers: String,
        @Field("patient_id") patient_id: Int
    ):retrofit2.Call<DefaultResponse>

    @GET("patients/answers/{patient_id}")
    fun patientAnswers(
        @Path("patient_id") patient_id: Int
    ):Call<PatientAnswersResponse>
}