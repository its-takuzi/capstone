package dicoding.bangkit.capstone_project.Api

import dicoding.bangkit.capstone_project.Api.Response.KlasifikasiResponse
import dicoding.bangkit.capstone_project.Api.Response.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Header("Authorization")token :String,
        @Part file: MultipartBody.Part,
    ): KlasifikasiResponse


    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}

