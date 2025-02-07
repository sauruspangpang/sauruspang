package ProfilePackage.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val name: String?,
    val birth: String?,
    @ColumnInfo(name = "selected_image") val selectedImage: Int?
)