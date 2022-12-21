package br.com.mappgame.api

import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val AUTH = "Basic " + Base64.encodeToString("belalkhan:123456".toByteArray(), Base64.NO_WRAP)

    // The API URL need finish in '/' like: https://c86b-2804-14d-5cd5-42b0-2889-e0d9-cf9-1047.sa.ngrok.io/api/
    private const val BASE_URL = "https://7084-2804-14d-5cd5-43a4-1d5c-e838-5b5d-8c95.sa.ngrok.io/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", AUTH)
                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(Api::class.java)
    }

}