package com.example.imagesearchpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearchpage.data.image.ImageResponse
import com.example.imagesearchpage.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: Adapter


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

        adapter = Adapter(requireContext(),Data.searchList)
        binding.rvSearch.adapter = adapter
        binding.rvSearch.layoutManager = GridLayoutManager(context,2)

        getMovieData("KakaoAK d0c3dd174157f12b0e98736f7eff048a","faker")

    }

    private fun getMovieData(authorization: String, search: String,) {

        val movieInterface = RetrofitClient.initRetrofit().create(SearchInterface::class.java)

        movieInterface.getNowPlaying(authorization, search).enqueue(object :
            Callback<ImageResponse> {

            override fun onResponse(
                call: Call<ImageResponse>,
                response: Response<ImageResponse>,
            ) {
                if (response.isSuccessful) {

                    val result = response.body() as ImageResponse
                    for (i in 0..30) {
                        Data.searchList.add(
                            Search(
                                result.documents[i].display_sitename,
                                result.documents[i].thumbnail_url,
                                result.documents[i].datetime,
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
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