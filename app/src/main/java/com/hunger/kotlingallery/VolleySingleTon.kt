package com.hunger.kotlingallery

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleTon private constructor(context: Context) {

    companion object {
        private var INSTANCE: VolleySingleTon? = null

        /*   fun getInstance(context: Context): VolleySingleTon {
               if (INSTANCE == null)
                   synchronized(VolleySingleTon) {
                       INSTANCE =  VolleySingleTon(context)
                   }
               return INSTANCE as VolleySingleTon
           }*/
//        fun getInstance(context: Context)={
//            INSTANCE?: synchronized(this){
//                VolleySingleTon(context).also { INSTANCE = it }
//            }
//        }
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                VolleySingleTon(context).also { INSTANCE = it }
            }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}