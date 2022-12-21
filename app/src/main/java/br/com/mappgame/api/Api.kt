package br.com.mappgame.api

import br.com.mappgame.models.*
import okhttp3.ResponseBody
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
    fun createPatient(
        @Path("user_id") user_id: Int,
        @Field("birth_date") birth_date: String,
        @Field("name") name: String
    ):retrofit2.Call<DefaultResponse>

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

    @DELETE("patients/destroy/{patient_id}")
    fun deletePatient(
        @Path("patient_id") patient_id: Int
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @PUT("patients/update/{patient_id}")
    fun updatePatient(
        @Path("patient_id") patient_id: Int,
        @Field("name") name: String,
        @Field("birth_date") birth_date: String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("professionals/store/{user_id}")
    fun storeLicense(
        @Path("user_id") user_id: Int,
        @Field("license") license: String
    ):Call<DefaultResponse>

    @GET("patients/email/{user_email}")
    fun searchPatientsByUserEmail(
        @Path("user_email") user_email: String
    ):Call<ProfessionalSearchPatientsByEmailResponse>

    @FormUrlEncoded
    @POST("professionals/patient/store")
    fun storePatientFromProfessional(
        @Field("patient_id") patient_id: Int,
        @Field("user_id") user_id: Int,
    ):Call<DefaultResponse>

    @GET("professionals/patients/{user_id}")
    fun professionalPatients(
        @Path("user_id") user_id: Int
    ):Call<UserPatientsIndexResponse>

    @GET("professionals/{professional_id}")
    fun getProfessional(
        @Path("professional_id") professional_id: Int
    ):Call<ProfessionalShowResponse>

    @FormUrlEncoded
    @PUT("professionals/{user_id}")
    fun storeProfessional(
        @Path("user_id") user_id: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("license") license: String,
        @Field("password_confirmation") password_confirmation: String,
        @Field("password") password: String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("forgot_password")
    fun forgotPassword(
        @Field("email") email: String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("professionals/patient/destroy")
    fun deletePatientProfessionalRelation(
        @Field("patient_id") patient_id: Int,
        @Field("user_id") user_id: Int
    ):Call<DefaultResponse>

    @GET("download/pdf/{patient_id}/{user_id}")
    fun downloadAnswersPdf(
        @Path("patient_id") patient_id: Int,
        @Path("user_id") user_id: Int
    ):Call<ResponseBody>
}