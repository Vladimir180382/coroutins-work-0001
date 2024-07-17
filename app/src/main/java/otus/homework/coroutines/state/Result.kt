package otus.homework.coroutines.state

import otus.homework.coroutines.models.ModelFact

sealed class Result
data class Success(val modelFact: ModelFact) : Result()
object Error : Result()