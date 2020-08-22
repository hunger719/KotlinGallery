package com.hunger.kotlingallery.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hunger.kotlingallery.R
import com.hunger.kotlingallery.adapter.GalleryAdapter
import com.hunger.kotlingallery.model.GalleryModel
import kotlinx.android.synthetic.main.fragment_gallery.*

@Suppress("DEPRECATION")
class Gallery : Fragment() {
    private val galleryViewModel by viewModels<GalleryModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.gallery_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.gallery_menu_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                galleryViewModel.refreshData()
            }
            R.id.gallery_menu_retry -> {
                galleryViewModel.retry()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        val galleryAdapter = GalleryAdapter()
        recyclerview.apply {
            adapter = galleryAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        galleryViewModel.pagedListLiveData.observe(viewLifecycleOwner, Observer {
            galleryAdapter.submitList(it)
            swipeRefreshLayout.isRefreshing = false
        })

        swipeRefreshLayout.setOnRefreshListener {
            galleryViewModel.refreshData()
        }

        galleryViewModel.netWorkStatus.observe(viewLifecycleOwner, Observer { })
    }
}