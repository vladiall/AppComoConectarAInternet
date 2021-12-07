package com.example.comoconectarseainternet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.example.comoconectarseainternet.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var urlList: List<String>
    private lateinit var progessBarList: List<ProgressBar>
    private lateinit var imageViewList: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageLoad()
        initProgessBar()
        initImageViews()

        binding.btnMessage.setOnClickListener {
            AlertDialog
                .Builder(this)
                .setMessage("Si puede ver este mensaje, se demuestra el funcionamiento de las corrutinas en background")
                .setCancelable(true)
                .show()
        }

        CoroutineScope(Dispatchers.Main).launch {
            for (i in urlList.indices) {
                val image = doInBackground(urlList[i], progessBarList[i])
                Log.d("Corrutinas", image.toString())

                if (image != null) {
                    updateView(image, progessBarList[i], imageViewList[i])
                }
            }
        }

    }

    private fun imageLoad() {
        urlList = listOf<String>(
            "https://www.ngenespanol.com/wp-content/uploads/2018/08/Desierto-de-Atacama-podr%C3%ADa-albergar-microorganismos-marcianos.jpg",
            "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg",
            "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631290503689E01_DXXX.jpg",
            "https://www3.gobiernodecanarias.org/medusa/ecoblog/msuaump/files/2012/11/desierto-atacama.jpg"
        )
    }

    private fun initProgessBar() {
        progessBarList = listOf(binding.pBarImg1, binding.pBarImg2, binding.pBarImg3, binding.pBarImg4)
    }

    private fun initImageViews() {
        imageViewList = listOf(binding.img1, binding.img2, binding.img3, binding.img4)
    }

    private suspend fun doInBackground(url: String, progressBar: ProgressBar): Bitmap {
        lateinit var bmp: Bitmap
        withContext(Dispatchers.Default) {
            try {
                progressBar.visibility = View.VISIBLE
                val newURL = URL(url)
                val inputStram = newURL.openConnection().getInputStream()

                Log.d("Corrutinas", inputStram.toString())

                bmp = BitmapFactory.decodeStream(inputStram)
            } catch (e: Exception) {
                Log.d("Corrutinas", e.message.toString())
                e.printStackTrace()
            }
        }
        return bmp
    }

    private fun updateView(image: Bitmap, progressBar: ProgressBar, imageView: ImageView) {
        Log.d("Corrutinas", "Ahora se puede ver lo del updateView ")
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(image)
    }
}