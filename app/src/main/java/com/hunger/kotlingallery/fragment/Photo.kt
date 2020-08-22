package com.hunger.kotlingallery.fragment

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hunger.kotlingallery.R
import com.hunger.kotlingallery.adapter.PhotoPagerAdapter
import com.hunger.kotlingallery.adapter.PhotoPagerViewHolder
import com.hunger.kotlingallery.entity.PhotoItem
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.pager_photo_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Photo : Fragment() {
    private val WRITE_EXTERNAL_STORAGE = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val photoList = arguments?.getParcelableArrayList<PhotoItem>("PHOTO_LIST")
        PhotoPagerAdapter().apply {
            viewpager2.adapter = this
            submitList(photoList)
        }

        viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                textView2.text = getString(R.string.photo_index, position + 1, photoList?.size)
            }
        })

        viewpager2.setCurrentItem(arguments?.getInt("PHOTO_INDEX") ?: 0, false)

        photo_download_img.setOnClickListener {
            if (Build.VERSION.SDK_INT > 23 && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE)
            } else {
                viewLifecycleOwner.lifecycleScope.launch { savePhoto() }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewLifecycleOwner.lifecycleScope.launch { savePhoto() }
                } else {
                    Toast.makeText(requireContext(), "请开启存储权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun savePhoto() {
        withContext(Dispatchers.IO) {
            val holder =
                (viewpager2[0] as RecyclerView).findViewHolderForAdapterPosition(viewpager2.currentItem) as PhotoPagerViewHolder

            val bitmap = holder.itemView.pager_photo.drawable.toBitmap()
            val savaUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , ContentValues()
            ) ?: kotlin.run {
                MainScope().launch { Toast.makeText(requireContext(), "保存失败", Toast.LENGTH_SHORT).show() }
                return@withContext
            }
            requireContext().contentResolver.openOutputStream(savaUri).use {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)) {
                    MainScope().launch { Toast.makeText(requireContext(), "保存成功", Toast.LENGTH_SHORT).show() }
                } else {
                    MainScope().launch { Toast.makeText(requireContext(), "保存失败", Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

}