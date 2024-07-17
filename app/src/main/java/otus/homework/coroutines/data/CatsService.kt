package otus.homework.coroutines.data

import otus.homework.coroutines.models.Fact
import otus.homework.coroutines.models.ImageCat
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Response<Fact>
}

interface ImageCatService {

    @GET("search")
    suspend fun getCatImage() : Response<List<ImageCat>>
}