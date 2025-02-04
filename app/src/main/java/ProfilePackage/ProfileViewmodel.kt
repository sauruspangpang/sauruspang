package ProfilePackage

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Profile(
    var name: String,
    var birth: String,
    var userprofile: Int,
    var selectedImage : Int
)

class ProfileViewmodel : ViewModel() {
    var profiles = mutableStateListOf<Profile>()

    fun addProfile(name: String, birth: String, userprofile: Int, selectedImage: Int) {
        profiles.add(Profile(name, birth, userprofile, selectedImage ))
    }
}