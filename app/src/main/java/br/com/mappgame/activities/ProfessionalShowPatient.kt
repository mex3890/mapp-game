package br.com.mappgame.activities

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.Answer
import br.com.mappgame.models.DefaultResponse
import br.com.mappgame.models.PatientAnswersResponse
import br.com.mappgame.storage.SharedPrefManager
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_professional_show_patient.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class ProfessionalShowPatient : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(br.com.mappgame.R.layout.activity_professional_show_patient)

        val name = intent.getStringExtra("name")
        val id = intent.getIntExtra("id", 0)
        val user = SharedPrefManager.getInstance(applicationContext).user
        text_name_patient.text = "Patient: ".plus(name)

        RetrofitClient.instance.patientAnswers(id)
            .enqueue(object : Callback<PatientAnswersResponse> {
                override fun onFailure(call: Call<PatientAnswersResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Internal App Error, try again letter or verify your connection",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<PatientAnswersResponse>,
                    response: Response<PatientAnswersResponse>
                ) {
                    if (response.code() == 202) {
                        Toast.makeText(
                            applicationContext,
                            response.body()?.error,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    if (response.code() == 500) {
                        Toast.makeText(
                            applicationContext,
                            "Internal Server error, try again latter!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    if (response.code() == 200) {
                        filterWithErrors.setOnClickListener {
                            mountGraph(response, mountWithHits = false)
                            return@setOnClickListener
                        }

                        filterAll.setOnClickListener {
                            mountGraph(response)
                            return@setOnClickListener
                        }

                        filterLastFive.setOnClickListener {
                            mountGraph(response, mountWithHits = true, lastFive = true)
                            return@setOnClickListener
                        }

                        filterWithHits.setOnClickListener {
                            mountGraph(response)
                            return@setOnClickListener
                        }

                        mountGraph(response)
                    }
                }
            })

        profileButtonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@ProfessionalShowPatient)
            builder.setMessage("Do you really want to unlink the profile?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    if (user != null) {
                        RetrofitClient.instance.deletePatientProfessionalRelation(id, user.id)
                            .enqueue(object : Callback<DefaultResponse> {
                                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Internal App Error, try again letter or verify your connection",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onResponse(
                                    call: Call<DefaultResponse>,
                                    response: Response<DefaultResponse>
                                ) {
                                    if (response.code() == 202) {
                                        Toast.makeText(
                                            applicationContext,
                                            response.body()?.error,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                    if (response.code() == 500) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Internal Server error, try again latter!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                    if (response.code() == 200) {
                                        Toast.makeText(
                                            applicationContext,
                                            response.body()?.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                        startActivity(
                                            Intent(
                                                this@ProfessionalShowPatient,
                                                ProfessionalActivity::class.java
                                            )
                                        )
                                    }
                                }
                            })
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        profileButtonReturn.setOnClickListener {
            startActivity(Intent(applicationContext, ProfessionalActivity::class.java))
        }

        profileButtonProfiles.setOnClickListener {
            startActivity(Intent(applicationContext, ProfessionalActivity::class.java))
        }

        profileButtonPdf.setOnClickListener {
            if (user != null) {
                RetrofitClient.instance.downloadAnswersPdf(patient_id = id, user_id = user.id)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(
                                applicationContext,
                                "Internal App Error, try again letter or verify your connection",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.code() == 500) {
                                Toast.makeText(
                                    applicationContext,
                                    "Internal Server error, try again latter!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            if (response.isSuccessful) {
                                val success = response.body()?.let { it1 ->
                                    if (name != null) {
                                        writeResponseBodyToDisk(it1, name)
                                    }
                                }

                                Toast.makeText(
                                    applicationContext,
                                    "Download Success",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
            }
        }

        buttonSetPerView.setOnClickListener {
            var count = editTextPerViewCount.text.toString().trim()

            if (count.isEmpty()) {
                count = "10"
            }

            SharedPrefManager.getInstance(applicationContext)
                .savePerAreaViewGraphValue(count.toInt())
            val intent = Intent(applicationContext, ProfessionalShowPatient::class.java)
            intent.putExtra("name", name)
            intent.putExtra("id", id)
            if (user != null) {
                intent.putExtra("userId", user.id)
            }
            startActivity(intent)
        }
    }

    fun mountGraph(
        response: Response<PatientAnswersResponse>,
        mountWithHits: Boolean = true,
        lastFive: Boolean = false
    ) {
        val graph = findViewById<View>(R.id.graph) as GraphView

        graph.removeAllSeries()
        var arrayData = arrayOf(DataPoint(0.0, 0.0))
        var i: Double = 1.0
        var answers = response.body()?.answers

        if (answers.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "No answers available for this profile!",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (lastFive && answers.size >= 5) {
            val newAnswers: MutableList<Answer> = mutableListOf()
            for (index in 0..4) {
                newAnswers += answers[index]
            }
            answers = newAnswers
        }

        answers.forEach { answer: Answer ->
            arrayData += if (!mountWithHits) {
                DataPoint(i, answer.errors.toDouble())
            } else {
                DataPoint(i, answer.hits.toDouble())
            }

            i += 1.0
        }


        val series: LineGraphSeries<DataPoint> = LineGraphSeries(arrayData)
        series.isDrawDataPoints = true
        series.isDrawBackground = true
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10F
        paint.color = Color.parseColor("#FF9800")
        paint.pathEffect = DashPathEffect(floatArrayOf(8f, 5f), 0F)
        series.isDrawAsPath = true
        series.setCustomPaint(paint)
        series.thickness = 8

        graph.addSeries(series)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMaxY(6.0)
        graph.viewport.setMinY(0.0)
        graph.viewport.setMinX(0.0)
        SharedPrefManager.getInstance(applicationContext).countPerAreaViewGraph?.let {
            graph.viewport.setMaxX(
                it.toDouble()
            )
        }
        graph.viewport.isScalable = true
        if (mountWithHits) {
            graph.title = "Answers Hits"
        } else {
            graph.title = "Answers Errors"
        }
        graph.titleColor = Color.WHITE
        graph.titleTextSize = 50F
        graph.scrollX = 1
        graph.gridLabelRenderer.gridColor = Color.WHITE
        graph.gridLabelRenderer.isHighlightZeroLines = false
        graph.gridLabelRenderer.verticalLabelsColor = Color.WHITE
        graph.gridLabelRenderer.horizontalLabelsColor = Color.WHITE
        graph.gridLabelRenderer.labelVerticalWidth = 100
        graph.gridLabelRenderer.horizontalAxisTitle = "Game count"
        graph.gridLabelRenderer.verticalAxisTitle = "Hits"
        graph.gridLabelRenderer.verticalAxisTitleColor = Color.WHITE
        graph.gridLabelRenderer.horizontalAxisTitleColor = Color.WHITE
    }

    fun writeResponseBodyToDisk(body: ResponseBody, name: String): Boolean {
        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val array: String = WRITE_EXTERNAL_STORAGE
                ActivityCompat.requestPermissions(this, arrayOf(array), 100)
            }

            val pdfFile: File = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "patient_".plus(name).plus("_answers.pdf")
            )

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize: Long = body.contentLength();
                var fileSizeDownload = 0;

                inputStream = body.byteStream();
                outputStream = FileOutputStream(pdfFile)

                while (true) {
                    val read: Int? = inputStream?.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    if (read != null) {
                        outputStream.write(fileReader, 0, read)
                        fileSizeDownload += read
                        Log.d(
                            "PDF download",
                            "file: ".plus(fileSizeDownload).plus(" of ").plus(fileSize)
                        )
                    }

                }
                outputStream.flush()

                return true
            } catch (exception: IOException) {
                return false
            } finally {
                if (inputStream !== null) {
                    inputStream.close()
                }

                if (outputStream !== null) {
                    outputStream.close()
                }

            }
        } catch (exception: IOException) {
            return false
        }
    }
}