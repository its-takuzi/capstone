package dicoding.bangkit.capstone_project.ui.Upload

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import dicoding.bangkit.capstone_project.Helper.getImageUri
import dicoding.bangkit.capstone_project.databinding.KlasifikasiActifityBinding
import dicoding.bangkit.capstone_project.ui.homepage.Homepage

class Upload : AppCompatActivity() {

    private lateinit var binding : KlasifikasiActifityBinding
    private var currentImageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KlasifikasiActifityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        binding.buttonGalery.setOnClickListener{Startgalery() }
        binding.buttonCamera.setOnClickListener{Startcamera()}
        binding.backbutton.setOnClickListener {
            startActivity(Intent(this@Upload , Homepage::class.java))
        }
    }


    private fun Startgalery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun Startcamera(){
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){ isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }



    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.buttonGalery.setImageURI(it)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}