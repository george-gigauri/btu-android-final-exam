package btu.finalexam.georgegigauri.data.model

data class Comment(
    var id: String = "",
    var comment: String? = "",
    var author: String? = "",
    var authorImage: String? = "",
    var timestamp: Long = System.currentTimeMillis()
)