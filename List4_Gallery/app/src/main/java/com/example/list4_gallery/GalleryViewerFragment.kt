package com.example.list4_gallery


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_gallery_viewer.*

class GalleryViewerFragment : Fragment() {
    val photos: ArrayList<Photo> = ArrayList()
    private var spanCount: Int = 3
    private var PREFS_NAME = "photos"
    lateinit var sharedPref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_gallery_viewer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")
        setPhotosArray()
        spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            3
        } else {
            4
        }
        galleryRecyclerView.layoutManager = GridLayoutManager(this.context, spanCount)
        galleryRecyclerView.adapter = GalleryItemAdapter(photos, this)

    }

    fun showImage(index: Int) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val showImageIntent = Intent(activity, ImageActivity::class.java)
            showImageIntent.putExtra("path", photos[index].path)
            showImageIntent.putExtra("thumbPath", photos[index].thumbPath)
            showImageIntent.putExtra("description", photos[index].description)
            showImageIntent.putExtra("rating", photos[index].rating)
            startActivity(showImageIntent)
        } else {
            (fragmentManager!!.findFragmentById(R.id.imageViewerFragment) as ImageViewerFragment).showImage(photos[index])

        }
    }

    private fun setPhotosArray() {
        for (i in 172 downTo 1){
            photos.add(Photo("https://amalinowska.pl/album/photo_$i.jpg",
                "https://amalinowska.pl/album/thumbs/thumb_photo_$i.jpg",
                "photo $i"))
        }

    }

    fun setRatings() {
        sharedPref = this.activity!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        for (photo in photos) {
            photo.rating = sharedPref.getFloat(photo.path, 0.0F)
        }
        photos.sortByDescending { photo -> photo.rating }
        galleryRecyclerView.adapter!!.notifyDataSetChanged()
    }


    private var TAG = "lifecycle [gallery fragment]"
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        setRatings()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
    }
}
