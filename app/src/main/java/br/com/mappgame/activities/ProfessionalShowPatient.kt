package br.com.mappgame.activities

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import kotlin.random.Random


class ProfessionalShowPatient : AppCompatActivity() {

    private val channelId = Random.nextInt(100).toString()
    private val channelName = "MappGameChannel"


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(br.com.mappgame.R.layout.activity_professional_show_patient)

        val name = intent.getStringExtra("name")
        val id = intent.getIntExtra("id", 0)
        val user = SharedPrefManager.getInstance(applicationContext).user
        text_name_patient.text = "Patient: ".plus(name)
        editTextPerViewCount.setText(
            SharedPrefManager.getInstance(applicationContext).countPerAreaViewGraph.toString()
                ?: "10"
        )

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

                        @RequiresApi(Build.VERSION_CODES.S)
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
                                response.body()
                                    ?.let { it1 ->
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

    @RequiresApi(Build.VERSION_CODES.S)
    fun writeResponseBodyToDisk(body: ResponseBody, name: String) {
        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val array: String = WRITE_EXTERNAL_STORAGE
                ActivityCompat.requestPermissions(this, arrayOf(array), 100)
            }

            val replaced_name = name.lowercase().replace(' ', '_')

            val pdfFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "patient_".plus(replaced_name).plus("_answers.pdf")
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

                sendNotification(pdfFile, name)
            } catch (_: IOException) {
            } finally {
                if (inputStream !== null) {
                    inputStream.close()
                }

                if (outputStream !== null) {
                    outputStream.close()
                }

            }
        } catch (_: IOException) {

        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun sendNotification(file: File, name: String) {
        createNotificationChannel()

        val notificationId = Random.nextInt(100)

        val attachmentUri =
            FileProvider.getUriForFile(applicationContext, "com.freshdesk.helpdesk.provider", file);
        val openAttachmentIntent = Intent(Intent.ACTION_VIEW);
        openAttachmentIntent.setDataAndType(attachmentUri, "application/pdf");
        openAttachmentIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;

        var flag: Int? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flag = PendingIntent.FLAG_IMMUTABLE
        } else {
            flag = PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAttachmentIntent,
            flag
        );

//        val intentOpenPdf = PendingIntent.getActivity(
//            applicationContext,
//            System.currentTimeMillis().toInt(),
//            openAttachmentIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )

        val notification = NotificationCompat.Builder(this@ProfessionalShowPatient, channelId)
            .setContentTitle("Download PDF Complete")
            .setContentText(
                "PDF for patient ".plus(name).plus(" available on your download directory!")
            )
            .setSmallIcon(R.drawable.main_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.main_logo, "Open PDF", pendingIntent)
            .setAutoCancel(true)
            .build()


        val notificationManager = NotificationManagerCompat.from(this@ProfessionalShowPatient)

        notificationManager.notify(notificationId, notification)

    }
}