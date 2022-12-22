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

        var patient_id = intent.getIntExtra("patient_id", 0)
        val profileName = intent.getStringExtra("profileName")
        val profileBirthDate = intent.getStringExtra("profileBirthDate")
        val patientUserId = intent.getIntExtra("patientUserID", 0)

        var answers = ""

        val nextStageA = findViewById<ImageButton>(R.id.imageButton1)
        nextStageA.setOnClickListener {
            answers = answers.plus("l1:o1|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val nextStageB = findViewById<ImageButton>(R.id.imageButton2)
        nextStageB.setOnClickListener {
            answers = answers.plus("l1:o2|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val nextStageC = findViewById<ImageButton>(R.id.imageButton3)
        nextStageC.setOnClickListener {
            answers = answers.plus("l1:o3|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val nextStageD = findViewById<ImageButton>(R.id.imageButton4)
        nextStageD.setOnClickListener {
            answers = answers.plus("l1:o4|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val profileButtonReturn = findViewById<ImageButton>(R.id.profileButtonReturn)
        profileButtonReturn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("id", patient_id)
            intent.putExtra("name", profileName)
            intent.putExtra("birthDate", profileBirthDate)
            intent.putExtra("userId", patientUserId)
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
        val intent = Intent(this, StageTwo::class.java)
        intent.putExtra("questionValue", answers)
        intent.putExtra("patient_id", patient_id)
        intent.putExtra("profileName", profileName)
        intent.putExtra("profileBirthDate".toString(), profileBirthDate)
        intent.putExtra("patientUserID", patientUserId)
        startActivity(intent)

    }

}