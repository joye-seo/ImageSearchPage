package com.example.imagesearchpage.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearchpage.data.Data
import com.example.imagesearchpage.databinding.FragmentFavoriteBinding
import com.example.imagesearchpage.ui.search.SearchAdapter

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchAdapter(requireContext(), Data.favoriteList)
        binding.rvFavorite.adapter = searchAdapter
        binding.rvFavorite.layoutManager = GridLayoutManager(context,2)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}