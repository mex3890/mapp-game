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

        var patient_id = intent.getIntExtra("patient_id", 0)
        val profileName = intent.getStringExtra("profileName")
        val profileBirthDate = intent.getStringExtra("profileBirthDate")
        val patientUserId = intent.getIntExtra("patientUserID", 0)
        var answers = intent.getStringExtra("questionValue")


        val nextStageI = findViewById<ImageButton>(R.id.imageButton9)
        nextStageI.setOnClickListener{
            answers = answers.plus("l3:o1|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val nextStageJ = findViewById<ImageButton>(R.id.imageButton10)
        nextStageJ.setOnClickListener{
            answers = answers.plus("l3:o2|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val nextStageK = findViewById<ImageButton>(R.id.imageButton11)
        nextStageK.setOnClickListener{
            answers = answers.plus("l3:o3|")
            if (profileBirthDate != null) {
                if (profileName != null) {
                    nextStage(answers!!, patient_id, profileName, profileBirthDate.toString(), patientUserId)
                }
            }
        }

        val nextStageL = findViewById<ImageButton>(R.id.imageButton12)
        nextStageL.setOnClickListener{
            answers = answers.plus("l3:o4|")
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
            intent.putExtra("birthdate", profileBirthDate.toString())
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
        val intent = Intent(this, StageFour::class.java)
        intent.putExtra("questionValue", answers)
        intent.putExtra("patient_id", patient_id)
        intent.putExtra("profileName", profileName)
        intent.putExtra("profileBirthDate", profileBirthDate.toString())
        intent.putExtra("patientUserID", patientUserId)
        startActivity(intent)

    }
}