package com.example.imagesearchpage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.imagesearchpage.databinding.ActivityMainBinding
import com.example.imagesearchpage.ui.favorite.FavoriteFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNav()
    }

    private fun initBottomNav() = binding.apply {
        val searchFragment = SearchFragment()
        val favoriteFragment = FavoriteFragment()

        bottomNav.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.btn_search -> changeFragment(searchFragment)
                    R.id.btn_favorite -> changeFragment(favoriteFragment)
                }
                true
            }
            selectedItemId = R.id.btn_search
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentLayout.id, fragment)
            .commit()
    }


}