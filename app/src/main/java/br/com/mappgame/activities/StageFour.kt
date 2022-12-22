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

        var patient_id = intent.getIntExtra("patient_id", 0)
        val profileName = intent.getStringExtra("profileName")
        val profileBirthDate = intent.getStringExtra("profileBirthDate")
        val patientUserId = intent.getIntExtra("patientUserID", 0)
        var answers = intent.getStringExtra("questionValue")

        val nextStageM = findViewById<ImageButton>(R.id.imageButton13)
        nextStageM.setOnClickListener{
            answers = answers.plus("l4:o1|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val nextStageN = findViewById<ImageButton>(R.id.imageButton14)
        nextStageN.setOnClickListener{
            answers = answers.plus("l4:o2|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val nextStageO = findViewById<ImageButton>(R.id.imageButton15)
        nextStageO.setOnClickListener{
            answers = answers.plus("l4:o3|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val nextStageP = findViewById<ImageButton>(R.id.imageButton16)
        nextStageP.setOnClickListener{
            answers = answers.plus("l4:o4|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate.toString(), patientUserId)
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
        val intent = Intent(this, StageFive::class.java)
        intent.putExtra("questionValue", answers)
        intent.putExtra("patient_id", patient_id)
        intent.putExtra("profileName", profileName)
        intent.putExtra("profileBirthDate", profileBirthDate.toString())
        intent.putExtra("patientUserID", patientUserId)
        startActivity(intent)

    }
}