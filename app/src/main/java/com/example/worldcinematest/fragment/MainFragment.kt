package com.example.worldcinematest.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.worldcinematest.common.SocialEventsApi
import com.example.worldcinematest.databinding.FragmentMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding // Assuming you have a corresponding binding class for the fragment layout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using the binding
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isNetworkAvailable()) {
            fetchDataFromApi()
        } else {
            showNoInternetToast()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun fetchDataFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://virtserver.swaggerhub.com/focus.lvlup2021/LEVEL_UP_MOBILE/1.0.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val socialEventsApi = retrofit.create(SocialEventsApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val event = socialEventsApi.getEventById()

                withContext(Dispatchers.Main) {
                    binding.apply {
                        for (e in event.events) {
                            title.text = e.name
                            data.text = e.eventDate
                            description.text = e.description
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle API call failure, show an error message or log the exception
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch data. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showNoInternetToast() {
        Toast.makeText(
            requireContext(),
            "No internet connection. Please check your network settings.",
            Toast.LENGTH_SHORT
        ).show()
    }
}
