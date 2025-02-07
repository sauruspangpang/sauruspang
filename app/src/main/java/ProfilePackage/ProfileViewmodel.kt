package ProfilePackage

import ProfilePackage.Room.AppDatabase
import ProfilePackage.Room.User
import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Profile(
    var name: String,
    var birth: String,
    var userprofile: Int,
    var selectedImage : Int
)

class ProfileViewmodel(application: Application) : AndroidViewModel(application) {
    var profiles = mutableStateListOf<Profile>()
    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun addProfile(name: String, birth: String, userprofile: Int, selectedImage: Int) {
        profiles.add(Profile(name, birth, userprofile, selectedImage))
        saveProfileToDatabase(name, birth, selectedImage)
    }

    private fun saveProfileToDatabase(name: String, birth: String, selectedImage: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(name = name, birth = birth, selectedImage = selectedImage)
            userDao.insertAll(user)
        }
    }
}