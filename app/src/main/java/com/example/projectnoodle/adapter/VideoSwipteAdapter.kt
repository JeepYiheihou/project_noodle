package com.example.projectnoodle.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectnoodle.NoodleViewModel
import com.example.projectnoodle.fragment.PlayerFragment

class VideoSwipeAdapter(private val noodleViewModel: NoodleViewModel,
                         fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return noodleViewModel.contentListLive.value!!.size
    }

    override fun createFragment(position: Int): Fragment {
        val shortUrl = noodleViewModel.contentListLive.value!![position]!!.realUrl
        val fullUrl = noodleViewModel.generateFullVideoUrl(shortUrl)
        return PlayerFragment(fullUrl)
    }
}