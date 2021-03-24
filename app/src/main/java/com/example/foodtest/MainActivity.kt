package com.example.foodtest

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.foodtest.databinding.ActivityMainBinding
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)

    val now: Long = System.currentTimeMillis() // 현재시간을 msec 으로 구한다.
    val date = Date(now) // 현재시간을 Date에 저장한다
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale("ko", "KR")).format(date)
    val dateFormat2 = SimpleDateFormat("yyyy년 MM월 dd일 \nEE요일", Locale("ko", "KR")).format(date)
    val stringTime = dateFormat2.format(date)
    var meal: Int = 0
    val mbinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       mbinding.mCalender.setText(stringTime)
        mbinding.breakFast.setOnClickListener {
            meal = 1
            var thread = NetworkThread()
            thread.start()

        }
        mbinding.lunch.setOnClickListener {
            meal = 2
            var thread = NetworkThread()
            thread.start()

        }
        mbinding.dinner.setOnClickListener {
            meal = 3
            var thread = NetworkThread()
            thread.start()

        }

    }

    inner class NetworkThread : Thread() {
        private var TAG = "MainActivity"
        override fun run() {
            var site =
                "https://open.neis.go.kr/hub/mealServiceDietInfo?ATPT_OFCDC_SC_CODE=F10&SD_SCHUL_CODE=7380292&KEY=bde3d1d967c544409a90eab8b28d4aec&MMEAL_SC_CODE=${meal}&Type=json&MLSV_YMD=${dateFormat}"
            var url = URL(site)
            var conn = url.openConnection()
            var input = conn.getInputStream()
            var isr = InputStreamReader(input)
            var br = BufferedReader(isr)
            var str: String? = null
            var buf = StringBuffer()

            do {
                str = br.readLine()

                if (str != null) {
                    buf.append(str)
                }
            } while (str != null)
            val root = JSONObject(buf.toString())

            val mealServiceDietInfo =
                root.getJSONArray("mealServiceDietInfo").getJSONObject(1).getJSONArray("row")

            for (i in 0 until mealServiceDietInfo.length()) {
                val obj = mealServiceDietInfo.getJSONObject(i)
                val row = obj.getString("DDISH_NM").replace("<br/>", "\n").replace("/", "")
                Log.d(TAG, "$row")


                runOnUiThread {
                    if (meal == 1) {
                        mbinding.mBreak.setText(row)
                    } else if (meal == 2) {
                        mbinding.mLunch.setText(row)
                    } else {
                        mbinding.mDinner.setText(row)
                    }

                }
            }
        }


    }

}


