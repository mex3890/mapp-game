package br.com.mappgame.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.*
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.PatientAnswersResponse
import br.com.mappgame.models.UserPatientsIndexResponse
import kotlinx.android.synthetic.main.activity_user_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view)

        RetrofitClient.instance.patientAnswers(1)
            .enqueue(object : Callback<PatientAnswersResponse> {

                override fun onFailure(call: Call<PatientAnswersResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Internal App Error, try again letter or verify your connection",
                        Toast.LENGTH_LONG
                    ).show()
                }

                @SuppressLint("ResourceAsColor")
                override fun onResponse(
                    call: Call<PatientAnswersResponse>,
                    response: Response<PatientAnswersResponse>
                ) {
                    val code = response.code()

                    if (code == 200) {

                        val answers =  response.body()?.answers
                        val hits = answers?.get(0)?.hits
                        val errors = answers?.get(0)?.errors
                        val date = answers?.get(0)?.created_at

                        Toast.makeText(applicationContext,
                            hits.toString(), Toast.LENGTH_LONG).show()
                    }


                    if (response.code() == 500) {
                        Toast.makeText(
                            applicationContext,
                            "Internal Server error, try again latter!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }
}