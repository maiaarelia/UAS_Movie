package com.example.uas_movie


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//kelas adapter untuk mengelola fragmen dalam ViewPager2.
class FragmentLoginRegisterAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    // Returns the total number of fragments
    override fun getItemCount(): Int {
        return 2
    }

    // Creates and returns a fragment based on its position
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            // First position (index 0) corresponds to the login fragment
            0 -> FragmentLogin()
            // Second position (index 1) corresponds to the register fragment
            1 -> FragmentRegister()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}




