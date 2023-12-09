package com.example.worldcinematest.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldcinematest.common.AppDatabase
import com.example.worldcinematest.common.MenuItem
import com.example.worldcinematest.common.MenuItemAdapter
import com.example.worldcinematest.R
import com.example.worldcinematest.activity.ChatListActivity
import com.example.worldcinematest.activity.SignUpActivity
import com.example.worldcinematest.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), MenuItemAdapter.Listener {

    private lateinit var profile: FragmentProfileBinding
    private var adapter = MenuItemAdapter(this)
    private lateinit var shPref: SharedPreferences

    private val menuList = listOf(
        MenuItem(0, R.drawable.ic_discussion, "Обсуждения"),
        MenuItem(1, R.drawable.ic_history, "История"),
        MenuItem(2, R.drawable.ic_settings, "Настройки")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profile = FragmentProfileBinding.inflate(inflater, container, false)
        return profile.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shPref = requireActivity().getSharedPreferences(
            "userWorldCinema",
            AppCompatActivity.MODE_PRIVATE
        )

        profile.apply {
            ProfileName.text = shPref.getString("name", "").toString()
            ProfileLastname.text = shPref.getString("lastname", "").toString()
            ProfileEmail.text = shPref.getString("email", "").toString()
            Menu.layoutManager = LinearLayoutManager(requireContext())
            Menu.adapter = adapter
            adapter.addDiscussion(menuList)

            buttonLogOut.setOnClickListener {
                shPref.edit().putBoolean("logout", true).apply()
                startActivity(
                    Intent(
                        this@ProfileFragment.requireContext(),
                        SignUpActivity::class.java
                    )
                )
                activity?.finish()
            }

            changeIcon?.setOnClickListener{
                pickImageFromGallery()
            }
        }
    }

    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            profile.avatar?.setImageURI(data?.data)
        }
    }

    override fun onClick(itemId: Int) {
        when (itemId) {
            0 -> {
                startActivity(
                    Intent(
                        this@ProfileFragment.requireContext(),
                        ChatListActivity::class.java
                    )
                )
            }
        }
    }
}