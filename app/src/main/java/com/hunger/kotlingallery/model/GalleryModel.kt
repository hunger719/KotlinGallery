package com.hunger.kotlingallery.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.hunger.kotlingallery.entity.PixabayDataSourceFactory

class GalleryModel(application: Application) : AndroidViewModel(application) {
    private val factory = PixabayDataSourceFactory(application)
    val pagedListLiveData = PixabayDataSourceFactory(application).toLiveData(1)
    val netWorkStatus = Transformations.switchMap(factory.pixabayDataSource) {it.netWorkStatus}
    fun refreshData() {
        pagedListLiveData.value?.dataSource?.invalidate()
    }

    fun retry(){
        factory.pixabayDataSource.value?.retry?.invoke()
    }
}