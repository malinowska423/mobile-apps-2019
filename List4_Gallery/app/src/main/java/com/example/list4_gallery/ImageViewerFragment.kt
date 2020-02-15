package com.example.list4_gallery


import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image_viewer.*

class ImageViewerFragment : Fragment() {

    private var photo : Photo = Photo("https://amalinowska.pl/album/photo_1.jpg",
        "https://amalinowska.pl/album/thumbs/thumb_photo_1.jpg",
        "photo 1")
    private var PREFS_NAME = "photos"
    lateinit var sharedPref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_image_viewer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")
        if (activity?.intent != null && activity!!.intent.getStringExtra("path") != null) {
            photo = Photo(activity!!.intent.getStringExtra("path"),
                activity!!.intent.getStringExtra("thumbPath"),
                activity!!.intent.getStringExtra("description"))
            photo.rating = activity!!.intent.getFloatExtra("rating", 0.0F)
        }
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ratingBar.setOnRatingBarChangeListener { _, _, _ ->
                run {
                    saveRating()
                    (fragmentManager!!.findFragmentById(R.id.galleryViewerFragment) as GalleryViewerFragment).setRatings()
                }
            }
        }
        showImage(photo)
    }

    fun showImage(photo: Photo) {
        this.photo = photo
        Picasso.get().load(photo.path).into(galleryImageView)
        ratingBar.rating = photo.rating
        imageDesc.text = photo.description
    }

    private fun saveRating() {
        sharedPref = activity!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putFloat(photo.path, ratingBar.rating)
        editor.apply()
    }

    private var TAG = "lifecycle [image fragment]"
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
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        saveRating()
        //save rating to shared pref
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
