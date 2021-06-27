package btu.finalexam.georgegigauri.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.databinding.ItemCarBinding
import btu.finalexam.georgegigauri.extension.getDiffUtil
import btu.finalexam.georgegigauri.viewholder.CarViewHolder

class CarAdapter(
    private val listener: OnCarItemClickListener
) : ListAdapter<Car, CarViewHolder>(getDiffUtil<Car>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder =
        CarViewHolder(ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) =
        holder.bind(getItem(position), listener)

    interface OnCarItemClickListener {
        fun onCarClick(car: Car)
    }
}