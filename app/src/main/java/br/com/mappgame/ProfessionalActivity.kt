package br.com.mappgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ProfessionalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional)

        val patientListActBtn = findViewById<Button>(R.id.patientListBtn)
        patientListActBtn.setOnClickListener {
            val Intent = Intent(this, PatientListActivity::class.java)
            startActivity(Intent)
        }
    }
}