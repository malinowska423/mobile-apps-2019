package com.example.list6_countingmachine

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var functions:Array<String>
    private lateinit var codes:Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        functions = resources.getStringArray(R.array.functions)
        codes = resources.getStringArray(R.array.functions_codes)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        for (x in 0..2) {
            for (y in 0..4) {
                val button = getButtonAt(x,y)
                button.text = functions[3*y+x]
                button.setOnClickListener { count(button) }
            }
        }
    }

    private fun count(view: View) {
        val expression = expression.text.toString()
        if (expression.isNotEmpty()) {
            val code = findCodeFor((view as Button).text.toString())
            val retrofit = Retrofit.Builder()
                .baseUrl("http://156.17.7.48:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            if (code != codes[4]) {
                val newton = retrofit.create(NewtonAPI::class.java)
                val call = newton.getResultOf(code, expression)

                call.enqueue(object : Callback<NewtonObject> {
                    override fun onFailure(call: Call<NewtonObject>, t: Throwable) {
                        result.text = R.string.error_message.toString()
                    }

                    override fun onResponse(call: Call<NewtonObject>, response: Response<NewtonObject>) {
                        val newtonObject = response.body()
                        result.text = newtonObject!!.result
                    }
                })
            } else {
                val newton = retrofit.create(NewtonAPISpecial::class.java)
                val call = newton.getResultOf(code, expression)

                call.enqueue(object : Callback<NewtonObjectSpecial> {
                    override fun onFailure(call: Call<NewtonObjectSpecial>, t: Throwable) {
                        result.text = R.string.error_message.toString()
                    }

                    override fun onResponse(call: Call<NewtonObjectSpecial>, response: Response<NewtonObjectSpecial>) {
                        val newtonObject = response.body()
                        result.text = newtonObject!!.result.toString()
                    }
                })
            }
        } else {
            Toast.makeText(this, "Put an expression first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getButtonAt(x: Int, y: Int):Button {
        return (keyboard.getChildAt(y) as TableRow).getChildAt(x) as Button
    }

    private fun findCodeFor(function: String) : String {
        for (i in 0 until functions.size) {
            if (function.equals(functions[i])) {
                return codes[i]
            }
        }
        return "not a function"
    }
}
