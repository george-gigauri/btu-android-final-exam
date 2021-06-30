package btu.finalexam.georgegigauri.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Car(
    var id: String = "",
    var brand: String = "",
    var model: String = "",
    var description: String = "",
    var image: String? = "",
    var userId: String? = "",
    @Exclude var authorName: String? = "",
    @Exclude var authorImage: String? = ""
)