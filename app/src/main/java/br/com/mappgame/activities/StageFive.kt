package br.com.mappgame.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StageFive : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_five)

        var patient_id = intent.getIntExtra("patient_id", 0)

        var answers = intent.getStringExtra("questionValue")


        val nextStageQ = findViewById<ImageButton>(R.id.imageButton17)
        nextStageQ.setOnClickListener {
            answers = answers.plus("l5:o1")
            nextStage(answers!!, patient_id)
        }

        val nextStageR = findViewById<ImageButton>(R.id.imageButton18)
        nextStageR.setOnClickListener {
            answers = answers.plus("l5:o2")
            nextStage(answers!!, patient_id)
        }

        val nextStageS = findViewById<ImageButton>(R.id.imageButton19)
        nextStageS.setOnClickListener {
            answers = answers.plus("l5:o3")
            nextStage(answers!!, patient_id)
        }

        val nextStageT = findViewById<ImageButton>(R.id.imageButton20)
        nextStageT.setOnClickListener {
            answers = answers.plus("l5:o4")
            nextStage(answers!!, patient_id)
        }

    }

    private fun nextStage(answers: String, patient_id: Int) {

        RetrofitClient.instance.answersUser(answers, patient_id)
            .enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    val code = response.code()

                    if (code == 200) {
                        Toast.makeText(
                            applicationContext,
                            "Temos que implementar um redirecionamento",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                    if (response.code() == 500) {
                        Toast.makeText(
                            applicationContext,
                            "Internal Server error, try again latter!",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Internal App Error, try again letter or verify your connection",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }


}