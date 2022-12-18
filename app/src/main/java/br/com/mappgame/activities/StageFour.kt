package br.com.mappgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import br.com.mappgame.R

class StageFour : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_four)

        var patient_id = intent.getIntExtra("patient_id",0)

        var answers = intent.getStringExtra("questionValue")

        val nextStageM = findViewById<ImageButton>(R.id.imageButton13)
        nextStageM.setOnClickListener{
            answers = answers.plus("l4:o1|")
            nextStage(answers!!,patient_id)
        }

        val nextStageN = findViewById<ImageButton>(R.id.imageButton14)
        nextStageN.setOnClickListener{
            answers = answers.plus("l4:o2|")
            nextStage(answers!!,patient_id)
        }

        val nextStageO = findViewById<ImageButton>(R.id.imageButton15)
        nextStageO.setOnClickListener{
            answers = answers.plus("l4:o3|")
            nextStage(answers!!,patient_id)
        }

        val nextStageP = findViewById<ImageButton>(R.id.imageButton16)
        nextStageP.setOnClickListener{
            answers = answers.plus("l4:o4|")
            nextStage(answers!!,patient_id)
        }

    }

    private fun nextStage(answers: String, patient_id: Int) {
        val intent = Intent(this, StageFive::class.java)
        intent.putExtra("questionValue", answers)
        intent.putExtra("patient_id",patient_id)
        startActivity(intent)

    }
}