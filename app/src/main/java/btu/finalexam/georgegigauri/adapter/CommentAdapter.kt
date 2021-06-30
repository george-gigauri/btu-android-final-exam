package btu.finalexam.georgegigauri.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import btu.finalexam.georgegigauri.data.model.Comment
import btu.finalexam.georgegigauri.databinding.ItemCommentBinding
import btu.finalexam.georgegigauri.extension.getDiffUtil
import btu.finalexam.georgegigauri.viewholder.CommentViewHolder

class CommentAdapter : ListAdapter<Comment, CommentViewHolder>(getDiffUtil<Comment>()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder =
        CommentViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}