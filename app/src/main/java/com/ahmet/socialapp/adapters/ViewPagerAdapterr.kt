package com.ahmet.socialapp.adapters

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ahmet.socialapp.Fragments.ChatsFragment
import com.ahmet.socialapp.Fragments.HomeFragment
import com.ahmet.socialapp.Fragments.SearchFragment
import com.ahmet.socialapp.Fragments.SettingsFragment

class ViewPagerAdapterr(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment()
            }
            1 -> {
                ChatsFragment()
            }
            2 -> {
                SearchFragment()
            }
            3-> {
                SettingsFragment()
            }
            else -> {
                throw Resources.NotFoundException("Pozisyon Yok")
            }
        }
    }
}