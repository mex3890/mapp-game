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

        var patient_id = intent.getIntExtra("patient_id", 0)
        val profileName = intent.getStringExtra("profileName")
        val profileBirthDate = intent.getStringExtra("profileBirthDate")
        val patientUserId = intent.getIntExtra("patientUserID", 0)
        var answers = intent.getStringExtra("questionValue")

        val nextStageE = findViewById<ImageButton>(R.id.imageButton5)
        nextStageE.setOnClickListener {
            answers = answers.plus("l2:o1|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate, patientUserId)
                }
            }
        }

        val nextStageF = findViewById<ImageButton>(R.id.imageButton6)
        nextStageF.setOnClickListener {
            answers = answers.plus("l2:o2|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate, patientUserId)
                }
            }
        }

        val nextStageG = findViewById<ImageButton>(R.id.imageButton7)
        nextStageG.setOnClickListener {
            answers = answers.plus("l2:o3|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate, patientUserId)
                }
            }
        }

        val nextStageH = findViewById<ImageButton>(R.id.imageButton8)
        nextStageH.setOnClickListener {
            answers = answers.plus("l2:o4|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate, patientUserId)
                }
            }
        }

        val profileButtonReturn = findViewById<ImageButton>(R.id.profileButtonReturn)
        profileButtonReturn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("id", patient_id)
            intent.putExtra("name", profileName)
            intent.putExtra("birthdate", profileBirthDate)
            intent.putExtra("userID", patientUserId)
            startActivity(intent)
        }
    }

    private fun nextStage(
        answers: String,
        patient_id: Int,
        profileName: String,
        profileBirthDate: String,
        patientUserId: Int
    ) {
        val intent = Intent(this, StageThree::class.java)
        intent.putExtra("questionValue", answers)
        intent.putExtra("patient_id", patient_id)
        intent.putExtra("profileName", profileName)
        intent.putExtra("profileBirthDate", profileBirthDate)
        intent.putExtra("patientUserID", patientUserId)
        startActivity(intent)

    }


}