package com.example.kustaurant.presentation.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.kustaurant.R
import com.example.kustaurant.databinding.ActivityOnboardingBinding

class OnboardingFragment1: Fragment(R.layout.fragment_onboarding1)
class OnboardingFragment2: Fragment(R.layout.fragment_onboarding2)
class OnboardingFragment3: Fragment(R.layout.fragment_onboarding3)
class OnboardingFragment4: Fragment(R.layout.fragment_onboarding4)
class OnboardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding
    private var selectedColor: Int = 0
    private var defaultColor: Int = 0
    private var selectedIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedColor = ContextCompat.getColor(this, R.color.cement_4)
        defaultColor = ContextCompat.getColor(this, R.color.cement_3)
        setupButtons()

        val adapter = OnboardingPagerAdapter(this)
        binding.onboardingVp.adapter = adapter

        binding.onboardingVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonState(position)
            }
        })

        binding.onboardingBtn1.setOnClickListener {
            binding.onboardingVp.currentItem = 0
        }

        binding.onboardingBtn2.setOnClickListener {
            binding.onboardingVp.currentItem = 1
        }

        binding.onboardingBtn3.setOnClickListener {
            binding.onboardingVp.currentItem = 2
        }

        binding.onboardingBtn4.setOnClickListener {
            binding.onboardingVp.currentItem = 3
        }
    }

    private fun setupButtons() {
        val buttons = listOf(
            binding.onboardingBtn1,
            binding.onboardingBtn2,
            binding.onboardingBtn3,
            binding.onboardingBtn4
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                binding.onboardingVp.currentItem = index
                updateButtonState(index)
            }
        }

        updateButtonState(selectedIndex)
    }

    private fun updateButtonState(selectedIndex: Int) {
        val buttons = listOf(
            binding.onboardingBtn1,
            binding.onboardingBtn2,
            binding.onboardingBtn3,
            binding.onboardingBtn4
        )

        buttons.forEachIndexed { index, button ->
            if (index == selectedIndex) {
                button.layoutParams.width = dpToPx(16)
                button.layoutParams.height = dpToPx(16)
                button.isSelected = true
                button.requestLayout()
            } else {
                button.layoutParams.width = dpToPx(12)
                button.layoutParams.height = dpToPx(12)
                button.isSelected = false
                button.requestLayout()
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}