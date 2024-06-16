package dicoding.bangkit.capstone_project.Api

import dicoding.bangkit.capstone_project.Api.Response.KlasifikasiResponse
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Header("Authorization")token :String,
        @Part file: MultipartBody.Part,
    ): KlasifikasiResponse
}