package btu.finalexam.georgegigauri.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comment(
    var id: String = "",
    var comment: String? = "",
    var authorId: String = "",
    @Exclude var author: String? = "",
    @Exclude var authorImage: String? = "",
    var timestamp: Long = System.currentTimeMillis()
)