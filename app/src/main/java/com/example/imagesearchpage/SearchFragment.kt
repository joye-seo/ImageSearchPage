package com.example.imagesearchpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearchpage.RetrofitClient.AUTH_HEADER
import com.example.imagesearchpage.data.image.ImageResponse
import com.example.imagesearchpage.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchAdapter: SearchAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Data.searchList.sortedByDescending { it.date }
        searchAdapter = SearchAdapter(requireContext(), Data.searchList)
        binding.rvSearch.adapter = searchAdapter
        binding.rvSearch.layoutManager = GridLayoutManager(context, 2)

        getImageData(AUTH_HEADER, "faker")
        initSearchView()

    }

    private fun initSearchView() = binding.apply {
//        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object : OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })


    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filterList = ArrayList<Search>()
            for (i in Data.searchList) {
                if (i.title.lowercase(Locale.ROOT).contains(query)) {
                    filterList.add(i)
                }
            }


        }
    }

    private fun getImageData(authorization: String, search: String) {

        val movieInterface = RetrofitClient.initRetrofit().create(SearchInterface::class.java)

        movieInterface.getImage(authorization, search).enqueue(object :
            Callback<ImageResponse> {

            override fun onResponse(
                call: Call<ImageResponse>,
                response: Response<ImageResponse>,
            ) {
                if (response.isSuccessful) {

                    val result = response.body() as ImageResponse
                    for (i in 0..10) {
                        Data.searchList.add(
                            Search(
                                "[image]",
                                result.documents[i].thumbnail_url,
                                result.documents[i].display_sitename,
                                result.documents[i].getFormatterTime(),
                                false
                                )
                        )
                    }
                    searchAdapter.notifyItemChanged(Data.searchList.size)
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Log.d("teddddddst", t.message ?: "통신오류")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}