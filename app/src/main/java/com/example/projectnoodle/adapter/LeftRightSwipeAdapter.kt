package com.example.projectnoodle.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectnoodle.fragment.GoodsListFragment
import com.example.projectnoodle.fragment.VideoFragment

class LeftRightSwipeAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> VideoFragment()
            else -> GoodsListFragment()
        }
    }
}