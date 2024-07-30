package com.example.kustaurant.presentation.ui.tier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ItemWideRestaurantBinding
import com.example.kustaurant.presentation.ui.home.RestaurantModel

class WideTierRestaurantAdapter(var tierRestaurantList: ArrayList<RestaurantModel>): RecyclerView.Adapter<WideTierRestaurantAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemWideRestaurantBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(restaurant: RestaurantModel){
            binding.restaurantCuisine.text = restaurant.restaurantCuisine
            binding.restaurantPosition.text = restaurant.restaurantPosition
            binding.restaurantName.text = restaurant.restaurantName
            binding.restaurantPartnershipInfo.text = restaurant.partnershipInfo

            val tierDrawable = when(restaurant.mainTier){
                1-> R.drawable.ic_rank_1
                2-> R.drawable.ic_rank_2
                3-> R.drawable.ic_rank_3
                else -> R.drawable.ic_rank_1
            }

            binding.favoriteImg.visibility = if (restaurant.isFavorite) View.VISIBLE else View.GONE
            binding.evaluationImg.visibility = if (restaurant.isChecked) View.VISIBLE else View.GONE

            Glide.with(binding.root.context)
                .load(restaurant.restaurantImgUrl)
                .centerCrop()
                .into(binding.restaurantImage)

            binding.restaurantTier.setImageResource(tierDrawable)

            binding.tierRv.setOnClickListener{ // recyclerview를 클릭했을 때

            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemWideRestaurantBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tierRestaurantList[position])
    }

    override fun getItemCount(): Int {
        return tierRestaurantList.size
    }

}