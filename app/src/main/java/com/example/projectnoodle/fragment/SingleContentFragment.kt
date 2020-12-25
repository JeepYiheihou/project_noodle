package com.example.projectnoodle.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectnoodle.R
import com.example.projectnoodle.adapter.LeftRightSwipeAdapter
import com.example.projectnoodle.databinding.FragmentSingleContentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SingleContentFragment : Fragment() {
    private lateinit var binding: FragmentSingleContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleContentBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val leftRightSwipeAdapter = LeftRightSwipeAdapter(this)
        with(binding) {

            leftRightSwipeViewPager.apply {
                adapter = leftRightSwipeAdapter
                setCurrentItem(1, false)
            }

            TabLayoutMediator(
                leftRightSwipeTab,
                leftRightSwipeViewPager
            ) { tab: TabLayout.Tab, i: Int ->
                tab.text = when (i) {
                    1 -> "video"
                    else -> "product"
                }
            }.attach()
        }
    }
}