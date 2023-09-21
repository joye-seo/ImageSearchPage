package com.example.imagesearchpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchpage.RetrofitClient.AUTH_HEADER
import com.example.imagesearchpage.data.Data
import com.example.imagesearchpage.data.Search
import com.example.imagesearchpage.data.image.ImageResponse
import com.example.imagesearchpage.databinding.FragmentSearchBinding
import com.example.imagesearchpage.ui.search.SearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapter: SearchAdapter
    private var isLoading = false
    private var currentPage = 1
    private val ITEMS_PER_PAGE = 10
    private val TITLE = "페이커"


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

        searchAdapter = SearchAdapter(requireContext(), Data.searchList)
        binding.rvSearch.adapter = searchAdapter
        binding.rvSearch.layoutManager = GridLayoutManager(context, 2)

        getImageData(AUTH_HEADER, TITLE, currentPage)

        binding.rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem + 2) {
                    loadNextPageData()
                }
            }
        })


    }

    private fun loadNextPageData() {
        if (!isLoading) {
            isLoading = true
            // 다음 페이지 데이터 로드
            currentPage++
            getImageData(AUTH_HEADER, TITLE, currentPage + 1)
        }
    }

    private fun getImageData(authorization: String, search: String, page: Int) {

        val imageInterface = RetrofitClient.initRetrofit().create(SearchInterface::class.java)

        imageInterface.getImage(authorization, search, page).enqueue(object :
            Callback<ImageResponse> {

            override fun onResponse(
                call: Call<ImageResponse>,
                response: Response<ImageResponse>,
            ) {
                if (response.isSuccessful) {

                    val result = response.body() as ImageResponse
                    val startIndex = (page - 1) * ITEMS_PER_PAGE

                    for (i in startIndex until startIndex + ITEMS_PER_PAGE) {
                        if (i >= result.documents.size) {
                            break
                        }
                        Data.searchList.add(
                            Search(
                                "[image]",
                                result.documents[i].thumbnail_url,
                                result.documents[i].display_sitename,
                                result.documents[i].getFormatterTime(),
                                false
                            )
                        )
                        Data.searchList.sortByDescending { it.date }

                    }
                    searchAdapter.notifyDataSetChanged()

                    // 현재 페이지 업데이트
                    currentPage = page
                    isLoading = false
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