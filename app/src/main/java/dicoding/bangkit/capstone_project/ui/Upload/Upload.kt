package dicoding.bangkit.capstone_project.ui.Upload
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import dicoding.bangkit.capstone_project.Api.ApiConfig
import dicoding.bangkit.capstone_project.Helper.getImageUri
import dicoding.bangkit.capstone_project.Helper.reduceFileImage
import dicoding.bangkit.capstone_project.Helper.uriToFile
import dicoding.bangkit.capstone_project.databinding.KlasifikasiActifityBinding
import dicoding.bangkit.capstone_project.ui.Hasil_klasifikasi.Hasil_klasifikasi
import dicoding.bangkit.capstone_project.ui.homepage.Homepage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.load.HttpException
import dicoding.bangkit.capstone_project.R
import dicoding.bangkit.capstone_project.ui.Hasil_klasifikasi.HasilKlasifikasiViewModel
import kotlinx.coroutines.launch

class Upload : AppCompatActivity() {

    private lateinit var binding : KlasifikasiActifityBinding
    private var currentImageUri : Uri? = null
    private lateinit var viewmodel : HasilKlasifikasiViewModel

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
        binding.buttonAnalyze.setOnClickListener{
           classifyImage()
        }
    }

    private fun classifyImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            showLoading(true)

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImRmOGIxNTFiY2Q5MGQ1YjMwMjBlNTNhMzYyZTRiMzA3NTYzMzdhNjEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vY2Fwc3RvbmUtYzI0MS1wcjU1NCIsImF1ZCI6ImNhcHN0b25lLWMyNDEtcHI1NTQiLCJhdXRoX3RpbWUiOjE3MTg1NDg2MDUsInVzZXJfaWQiOiJVY2l2NmJETzFpUkU2SXE0dzBCRm5xZ2owZ24yIiwic3ViIjoiVWNpdjZiRE8xaVJFNklxNHcwQkZucWdqMGduMiIsImlhdCI6MTcxODU0ODYwNSwiZXhwIjoxNzE4NTUyMjA1LCJlbWFpbCI6InRlc3QyQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJ0ZXN0MkBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.LXjFt4mnqgmw4Zwo85-DnpKufKT8UFUoJI_DrcF9cyPXEJd76zi16PhkDDX8rNEzlIjtltm8OCxXiCQ9lq9oXRkBz_5f1fjWAYCfhtx3-6Sx20CcWG156ujM8Sjoh5lmm8MIaUlbKTYeCbAQwPW2eZqNMwzZC_ZP37OxjqFESYrHboF2hUsvuPQOxmYLfCriKvkO9aLwZ4BmSDPJ6HNpLRuKl8BlxaTo6RzhipNGXpupCTMSZWExXr71Eol9SVdh-Ltr5M13iypuRqakaxe7Icck-VO4_bCK1_kuLUhsWeSQzpbnh_nxQ6spGhbdnFHdt-cAOAGDqYkh_oKPrBsbgQ"
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService(token)
                    val klasifikasiResponse = apiService.uploadImage(token, multipartBody)
                    val result = klasifikasiResponse.data?.result
                    val suggestion = klasifikasiResponse.data?.suggestion

                    val intent = Intent(this@Upload, Hasil_klasifikasi::class.java).apply {
                        putExtra("EXTRA_RESULT", result)
                        putExtra("EXTRA_SUGGESTION", suggestion)
                    }
                    startActivity(intent)
                    showLoading(false)
                } catch (e: HttpException) {
                    showToast(e.message.toString())
                    showLoading(false)
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}