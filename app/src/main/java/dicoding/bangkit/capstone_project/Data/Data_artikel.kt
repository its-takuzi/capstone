package dicoding.bangkit.capstone_project.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data_artikel(
    val name : String,
    val deksripsi : String,
    val image : Int
) : Parcelable
