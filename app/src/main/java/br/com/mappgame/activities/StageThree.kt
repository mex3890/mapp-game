package br.com.mappgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import br.com.mappgame.R

class StageThree : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_three)

        var patient_id = intent.getIntExtra("patient_id",0)

        var answers = intent.getStringExtra("questionValue")

        val nextStageI = findViewById<ImageButton>(R.id.imageButton9)
        nextStageI.setOnClickListener{
            answers = answers.plus("l3:o1|")
            nextStage(answers!!,patient_id)
        }

        val nextStageJ = findViewById<ImageButton>(R.id.imageButton10)
        nextStageJ.setOnClickListener{
            answers = answers.plus("l3:o2|")
            nextStage(answers!!,patient_id)
        }

        val nextStageK = findViewById<ImageButton>(R.id.imageButton11)
        nextStageK.setOnClickListener{
            answers = answers.plus("l3:o3|")
            nextStage(answers!!,patient_id)
        }

        val nextStageL = findViewById<ImageButton>(R.id.imageButton12)
        nextStageL.setOnClickListener{
            answers = answers.plus("l3:o4|")
            nextStage(answers!!,patient_id)
        }
    }
    private fun nextStage(answers: String, patient_id: Int) {
        val intent = Intent(this, StageFour::class.java)
        intent.putExtra("questionValue", answers)
        intent.putExtra("patient_id",patient_id)
        startActivity(intent)

    }
}