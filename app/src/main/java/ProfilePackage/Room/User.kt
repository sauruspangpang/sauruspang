package ProfilePackage.Room

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "Name") val name: String?,
    @ColumnInfo(name = "Birth") val birth: String?,
    @ColumnInfo(name = "selectedImage") val selectedImage: Int?
)