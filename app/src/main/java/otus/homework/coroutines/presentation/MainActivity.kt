package otus.homework.coroutines.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import otus.homework.coroutines.state.Error
import otus.homework.coroutines.R
import otus.homework.coroutines.state.Success

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)
        catsView.presenter = viewModel
        viewModel.catsModel.observe(this) { result ->
            when (result) {
                is Success -> catsView.populate(result.modelFact)
                Error -> Toast
                    .makeText(this, TEXT_TOAST, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    companion object {
        private const val TEXT_TOAST = "Не удалось получить ответ от сервера"
    }
}