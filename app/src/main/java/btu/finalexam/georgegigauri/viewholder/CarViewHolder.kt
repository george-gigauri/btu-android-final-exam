package btu.finalexam.georgegigauri.viewholder

import androidx.recyclerview.widget.RecyclerView
import btu.finalexam.georgegigauri.adapter.CarAdapter
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.databinding.ItemCarBinding
import btu.finalexam.georgegigauri.extension.setImage

class CarViewHolder(
    private val binding: ItemCarBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(car: Car, listener: CarAdapter.OnCarItemClickListener) {
        binding.apply {
            tvTitle.text = "${car.brand} ${car.model}"
            tvDescription.text = car.description
            tvAuthorName.text = car.authorName
            ivPicture.setImage(car.image)
            ivAuthorProfile.setImage(car.authorImage)

            root.setOnClickListener { listener.onCarClick(car) }
        }
    }
}