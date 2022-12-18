package br.com.mappgame.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.content.Intent
import br.com.mappgame.R

class StageOne : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_one)

        var patient_id = intent.getIntExtra("patient_id",0)

        var answers = ""

        val nextStageA = findViewById<ImageButton>(R.id.imageButton1)
        nextStageA.setOnClickListener {
            answers = answers.plus("l1:o1|")
            nextStage(answers,patient_id)
        }

        val nextStageB = findViewById<ImageButton>(R.id.imageButton2)
        nextStageB.setOnClickListener {
            answers = answers.plus("l1:o2|")
            nextStage(answers,patient_id)
        }

        val nextStageC = findViewById<ImageButton>(R.id.imageButton3)
        nextStageC.setOnClickListener {
            answers = answers.plus("l1:o3|")
            nextStage(answers,patient_id)
        }

        val nextStageD = findViewById<ImageButton>(R.id.imageButton4)
        nextStageD.setOnClickListener {
            answers = answers.plus("l1:o4|")
            nextStage(answers,patient_id)
        }


    }

    private fun nextStage(answers: String, patient_id: Int) {
        val intent = Intent(this, StageTwo::class.java)
        intent.putExtra("questionValue", answers)
        intent.putExtra("patient_id",patient_id)
        startActivity(intent)

    }

}