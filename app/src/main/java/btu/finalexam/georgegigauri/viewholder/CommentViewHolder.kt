package btu.finalexam.georgegigauri.viewholder

import androidx.recyclerview.widget.RecyclerView
import btu.finalexam.georgegigauri.data.model.Comment
import btu.finalexam.georgegigauri.databinding.ItemCommentBinding
import btu.finalexam.georgegigauri.extension.setImage
import java.text.SimpleDateFormat
import java.util.*

class CommentViewHolder(
    private val binding: ItemCommentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment?) {
        comment?.let {
            binding.tvFullName.text = it.author
            binding.tvDate.text =
                SimpleDateFormat("dd MMM. yyyy | HH:mm").format(Date(it.timestamp))
            binding.tvComment.text = it.comment
            binding.ivAvatar.setImage(it.authorImage)
        }
    }
}