package br.com.mappgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.com.mappgame.activities.DataViewPatientActivity

class PatientListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_list)

        val user1ActBtn = findViewById<Button>(R.id.user1Btn)
        user1ActBtn.setOnClickListener {
            val Intent = Intent(this, DataViewPatientActivity::class.java)
            startActivity(Intent)
        }
    }

}