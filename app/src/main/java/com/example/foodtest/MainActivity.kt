package com.example.foodtest

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://open.neis.go.kr/hub/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()


        val service = retrofit.create(Service::class.java)


        breakFast.setOnClickListener {
            val call: Call<Meal> = service.ApiService()
            call.enqueue(object : Callback<Meal> {
                override fun onFailure(call: Call<Meal>, t: Throwable) {
                    Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Meal>, response: Response<Meal>) {
                    if (response.body() != null) {
                        Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()
                        mCalender.setText(response.body()!!.MLSV_YMD + " 입니다")
                        breakFast.text = response.body()!!.DDISH_NM
                    }
                }

            })

        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        return builder.build()
    }


}
