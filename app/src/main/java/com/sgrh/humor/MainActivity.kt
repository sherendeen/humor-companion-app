package com.sgrh.humor

import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.sgrh.humor.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL


val baseUrl: String = "https://memeaware.com/"
val endpoint : String = "api.php"
val index = -1


class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val btn_getNextPost = findViewById<Button>(R.id.button_GetNextPost)
//        val txt_description = findViewById<TextView>(R.id.textField_Description)
//        val txt_comboField = findViewById<TextView>(R.id.textField_Combo)
//        val img_imageView = findViewById<ImageView>(R.id.imageView_Main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)



        binding.buttonGetNextPost.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                Log.i(TAG, "Entered onCLick")
                getNextImage(view)
            }
        })

    }

    fun getNextImage(view: View) {

        GlobalScope.launch (Dispatchers.IO) {
            val url = URL("$baseUrl$endpoint?id=-1")
            val response = StringBuilder()

            with(url.openConnection() as HttpURLConnection) {
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"  // optional default is GET

                    println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                    inputStream.bufferedReader().use {
                        it.lines().forEach { line ->
                            println(line)
                            response.append(line)
                        }
                    }
                }
            }

            Log.d(TAG, "response: ${response.toString()}")
            val post = Gson().fromJson(response.toString(), Post::class.java)
            Log.i(TAG, "Setting up textviews")


            val imageUrl = URL("$baseUrl${post.filename}")
            val httpConnection: HttpURLConnection =
                imageUrl.openConnection() as HttpURLConnection
            httpConnection.doInput = true
            httpConnection.connect()

            Log.i(
                TAG,
                "getting image with url $imageUrl: ${httpConnection.responseCode}"
            )

            val inputStream = httpConnection.inputStream
            val bitmapImage = BitmapFactory.decodeStream(inputStream)
            launch(Dispatchers.Main) {
                Log.i("UI", "thread ${Thread.currentThread().name}setting the image")
                Log.i(TAG, "Let there be visibility.")
                binding.textFieldCombo.text = "${post.displayname} ${post.timeposted}"
                binding.textFieldDescription.text = post.description
//                binding.scrolView.visibility = View.VISIBLE
                binding.imageViewMain.visibility = View.VISIBLE

                binding.imageViewMain.setImageBitmap(bitmapImage)
                binding.textFieldDescription.visibility = View.VISIBLE
                binding.textFieldCombo.visibility = View.VISIBLE

            }




        }
    }




}