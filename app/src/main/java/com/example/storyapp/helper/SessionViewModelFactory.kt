import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.ui.screen.activity.main.MainViewModel

class SessionViewModelFactory(private val sessionPreferences: SessionPreferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(sessionPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
