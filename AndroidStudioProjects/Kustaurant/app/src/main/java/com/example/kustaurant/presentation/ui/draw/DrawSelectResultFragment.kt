package com.example.kustaurant.presentation.ui.draw

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.kustaurant.R
import com.example.kustaurant.data.model.DrawRestaurantData
import com.example.kustaurant.databinding.FragmentDrawSelectResultBinding
import kotlin.math.abs

class DrawSelectResultFragment : Fragment() {
    private var _binding: FragmentDrawSelectResultBinding? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: DrawSelectResultAdapter
    private var restaurantList = mutableListOf<DrawRestaurantData>()
    private val viewModel: DrawViewModel by activityViewModels()
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDrawSelectResultBinding.inflate(inflater, container, false)
        setupViewPager()
        loadRestaurants()
        setupButton()
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.selectedIndex.observe(viewLifecycleOwner) { index ->
            if (index != null && index != RecyclerView.NO_POSITION) {
                viewPager.setCurrentItem(index, true)
                adapter.highlightItem(index)
            }
        }

        viewModel.selectedRestaurant.observe(viewLifecycleOwner) { selected ->
            displaySelectedRestaurantInfo(selected)
        }
    }

    private fun setupViewPager() {
        viewPager = binding.drawViewPager
        adapter = DrawSelectResultAdapter(restaurantList)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false // 스크롤 비활성화
        viewPager.offscreenPageLimit = 1
        val pageTransformer = ViewPager2.PageTransformer { page, position ->
            val absPosition = abs(position)
            page.apply {
                rotationY = -30 * position
                scaleX = 0.8f + (1 - absPosition) * 0.2f
                scaleY = scaleX
            }
        }
        viewPager.setPageTransformer(pageTransformer)
    }

    @SuppressLint("SetTextI18n")
    private fun displaySelectedRestaurantInfo(restaurant: DrawRestaurantData) {
        binding.drawTvRestaurantName.text = restaurant.restaurantName
        binding.drawTvRestaurantMenu.text = restaurant.restaurantMenu
        binding.drawTvRestaurantScore.text = restaurant.restaurantScoreSum.toString()
        binding.drawTvRestaurantPartnershipInfo.text = "이과대학 학생증 제시 시 99퍼센트 할인적용"

        updateStarRating(restaurant.restaurantScoreSum)
    }

    private fun updateStarRating(score: Double) {
        val fullStars = score.toInt()
        val hasHalfStar = score % 1 >= 0.5

        val starIds = listOf(R.id.draw_iv_star_1, R.id.draw_iv_star_2, R.id.draw_iv_star_3, R.id.draw_iv_star_4, R.id.draw_iv_star_5)
        starIds.forEachIndexed { index, starId ->
            val star = binding.root.findViewById<ImageView>(starId)
            when {
                index < fullStars -> star.setImageResource(R.drawable.ic_star_full)
                index == fullStars && hasHalfStar -> star.setImageResource(R.drawable.ic_star_half)
                else -> star.setImageResource(R.drawable.ic_star_empty)
            }
        }
    }

    private fun setupButton() {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setStroke(1, ContextCompat.getColor(requireContext(), R.color.signature_1))
            cornerRadius = 100f
            setColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        binding.drawBtnCategoryReset.background = drawable

        binding.drawBtnRetry.setOnClickListener {
            loadRestaurants()
        }

        binding.drawBtnCategoryReset.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.draw_fragment_container, DrawSelectCategoryFragment())
                .addToBackStack(null)
                .commit()
        }
    }
    private fun loadRestaurants() {
        viewModel.drawRestaurants()

        viewModel.drawList.observe(viewLifecycleOwner) { list ->
            restaurantList.clear()
            restaurantList.addAll(list)
            adapter.notifyDataSetChanged()

            // 애니메이션 시작
            startAnimation()
        }
    }

    // Assuming this method is called when the ViewPager stops at a page
    private fun highlightCenterImage() {
        val centerPosition = viewPager.currentItem
        adapter.highlightItem(centerPosition)
    }

    private fun startAnimation() {
        disableButtons() // 애니메이션 시작 시 버튼 비활성화

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            var currentPage = 0
            override fun run() {
                if (currentPage < adapter.itemCount) {
                    viewPager.setCurrentItem(currentPage++, true)
                    handler.postDelayed(this, 2000L / adapter.itemCount)
                    displaySelectedRestaurantInfo(restaurantList[currentPage-1])
                } else {
                    // 애니메이션 종료 후, 중앙에 선택된 음식점 배치
                    viewModel.selectedRestaurant.value?.let { selected ->
                        val selectedIndex = restaurantList.indexOf(selected)
                        viewPager.setCurrentItem(selectedIndex, true)
                        displaySelectedRestaurantInfo(selected)
                        highlightCenterImage()
                        enableButtons() // 애니메이션 종료 후 버튼 활성화
                    }
                }
            }
        }
        handler.post(runnable)
    }



    private fun disableButtons() {
        binding.drawBtnCategoryReset.isClickable = false
        binding.drawBtnRetry.isClickable = false
        binding.drawBtnCategoryReset.alpha = 0.5f // 버튼을 흐리게 표시
        binding.drawBtnRetry.alpha = 0.5f
    }

    private fun enableButtons() {
        binding.drawBtnCategoryReset.isClickable = true
        binding.drawBtnRetry.isClickable = true
        binding.drawBtnCategoryReset.alpha = 1.0f // 버튼을 명확하게 표시
        binding.drawBtnRetry.alpha = 1.0f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}