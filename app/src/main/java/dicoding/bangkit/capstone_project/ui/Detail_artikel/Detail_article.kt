package dicoding.bangkit.capstone_project.ui.Detail_artikel

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.bumptech.glide.Glide
import dicoding.bangkit.capstone_project.Data.Data_artikel
import dicoding.bangkit.capstone_project.databinding.ArticleActifityBinding

class Detail_article : AppCompatActivity() {

    private lateinit var binding: ArticleActifityBinding
    companion object{
        const val EXTRA_DATA_ARTIKEL = "ektra data artikel"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ArticleActifityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataartikel = intent.getParcelableExtra<Data_artikel>(EXTRA_DATA_ARTIKEL) as Data_artikel

        setupView()
        Glide.with(applicationContext)
            .load(dataartikel.image)
            .into(binding.imageArtikel)
        binding.judulArtikel.text = dataartikel.name
        binding.deksripsiArtikel.text = dataartikel.deksripsi

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