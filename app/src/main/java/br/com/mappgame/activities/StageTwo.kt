package br.com.mappgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import br.com.mappgame.R

class StageTwo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage_two)

        var patient_id = intent.getIntExtra("patient_id",0)

        var answers = intent.getStringExtra("questionValue")

        val nextStageE = findViewById<ImageButton>(R.id.imageButton5)
        nextStageE.setOnClickListener {
            answers = answers.plus("l2:o1|")
            nextStage(answers!!,patient_id)
        }

        val nextStageF = findViewById<ImageButton>(R.id.imageButton6)
        nextStageF.setOnClickListener {
            answers = answers.plus("l2:o2|")
            nextStage(answers!!,patient_id)
        }

        val nextStageG = findViewById<ImageButton>(R.id.imageButton7)
        nextStageG.setOnClickListener {
            answers = answers.plus("l2:o3|")
            nextStage(answers!!,patient_id)
        }

        val nextStageH = findViewById<ImageButton>(R.id.imageButton8)
        nextStageH.setOnClickListener {
            answers = answers.plus("l2:o4|")
            nextStage(answers!!,patient_id)
        }
    }

    private fun nextStage(answers: String, patient_id: Int) {
        val intent = Intent(this, StageThree::class.java)
        intent.putExtra("questionValue", answers)
        intent.putExtra("patient_id",patient_id)
        startActivity(intent)

    }


}