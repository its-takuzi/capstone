package dicoding.bangkit.capstone_project.ui.Hasil_klasifikasi

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import dicoding.bangkit.capstone_project.databinding.HasilKlasifikasiBinding
import androidx.activity.viewModels
import dicoding.bangkit.capstone_project.R

class Hasil_klasifikasi : AppCompatActivity() {

    private val viewModel: HasilKlasifikasiViewModel by viewModels()

    private lateinit var binding : HasilKlasifikasiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HasilKlasifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.result.observe(this) { result ->
            findViewById<TextView>(R.id.hasil_tipe).text = result
        }

        viewModel.suggestion.observe(this) { suggestion ->
            findViewById<TextView>(R.id.hasil_sugestion).text = suggestion
        }
        setupView()
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