package com.example.imagesearchpage.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchpage.RetrofitClient
import com.example.imagesearchpage.RetrofitClient.AUTH_HEADER
import com.example.imagesearchpage.SearchInterface
import com.example.imagesearchpage.data.Data
import com.example.imagesearchpage.data.Search
import com.example.imagesearchpage.data.image.ImageResponse
import com.example.imagesearchpage.data.video.VideoResponse
import com.example.imagesearchpage.databinding.FragmentSearchBinding
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


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    binding.rvSearch.visibility = View.VISIBLE
                    binding.rootLytSearchOn.visibility = View.GONE
                    getImageData(AUTH_HEADER, newText!!, currentPage)
                } else {
                    binding.rvSearch.visibility = View.GONE
                    binding.rootLytSearchOn.visibility = View.VISIBLE
                }
                return true
            }
        })
    }

    private fun loadNextPageData() {
        if (!isLoading) {
            isLoading = true
            // 다음 페이지 데이터 로드
            currentPage++
        }
        searchAdapter.notifyDataSetChanged()
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

        imageInterface.getVideo(authorization, search, page).enqueue(object :
            Callback<VideoResponse> {

            override fun onResponse(
                call: Call<VideoResponse>,
                response: Response<VideoResponse>,
            ) {
                if (response.isSuccessful) {

                    val result = response.body() as VideoResponse
                    val startIndex = (page - 1) * ITEMS_PER_PAGE

                    for (i in startIndex until startIndex + ITEMS_PER_PAGE) {
                        if (i >= result.documents.size) {
                            break
                        }
                        Data.searchList.add(
                            Search(
                                "[video]",
                                result.documents[i].thumbnail,
                                result.documents[i].title,
                                result.documents[i].datetime,
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

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                Log.d("teddddddst", t.message ?: "통신오류")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}