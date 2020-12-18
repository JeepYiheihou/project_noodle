package com.example.projectnoodle.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectnoodle.fragment.GoodsListFragment
import com.example.projectnoodle.fragment.VideoFragment

class LeftRightSwipeAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
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