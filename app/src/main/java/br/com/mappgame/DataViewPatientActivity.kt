package br.com.mappgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.mappgame.models.PatientAnswersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class DataViewPatientActivity : AppCompatActivity() {

    // Defina a interface da API
    interface MyApi {
        @GET("patients/answers/$patientId")
        fun getData(): Call<PatientAnswersResponse>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view_patient)

        val patientId = intent.getStringExtra("patientId")

        // Crie um objeto Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://d4dc-2804-14d-5cd5-43a4-1977-b7c6-332d-1972.sa.ngrok.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crie uma instância da interface da API
        val api = retrofit.create(MyApi::class.java)

        // Faça a solicitação GET à API
        api.getData().enqueue(object: Callback<PatientAnswersResponse> {
            override fun onResponse(call: Call<PatientAnswersResponse>, response: Response<PatientAnswersResponse>) {
                // Receba os dados de resposta aqui
                val data = response.body()
            }

            override fun onFailure(call: Call<PatientAnswersResponse>, t: Throwable) {
                // Trate falhas aqui
            }
        })

    }
}