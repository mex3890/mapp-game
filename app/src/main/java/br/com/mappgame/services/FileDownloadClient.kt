package br.com.mappgame.services

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

public interface FileDownloadClient {

    @GET("download/pdf/{patient_id}/{user_id}")
    fun downloadAnswersPdf(
        @Path("patient_id") patient_id: Int,
        @Path("user_id") user_id: Int
    ):Call<ResponseBody>
}