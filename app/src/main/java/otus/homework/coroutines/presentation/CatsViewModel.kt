package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.data.DiContainer
import otus.homework.coroutines.state.Error
import otus.homework.coroutines.data.ImageCatService
import otus.homework.coroutines.models.ModelFact
import otus.homework.coroutines.state.Result
import otus.homework.coroutines.state.Success
import java.net.SocketTimeoutException

@HiltViewModel
class CatsViewModel : ViewModel() {
    private var catsService: CatsService? = null
    private var imageCatService: ImageCatService? = null
    private val diContainer: DiContainer = DiContainer()
    private val _catsModel = MutableLiveData<Result>()
    val catsModel: LiveData<Result> = _catsModel

    private val handler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is SocketTimeoutException) _catsModel.value = Error
        else CrashMonitor.trackWarning()
    }

    init {
        onInitComplete()
    }

    fun onInitComplete() {
        catsService = diContainer.service
        imageCatService = diContainer.service2
        viewModelScope.launch(context = handler + CoroutineName(COROUTINE_NAME)) {
            val fact = async { catsService?.getCatFact() }.await()
            val image = async { imageCatService?.getCatImage() }.await()
            if (fact?.isSuccessful == true && image?.isSuccessful == true) {
                _catsModel.value = Success(
                    ModelFact(
                        fact = fact.body(),
                        imageCat = image.body()?.first()
                    )
                )
            } else {
                _catsModel.value = Error
            }
        }
    }

    companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
    }
}