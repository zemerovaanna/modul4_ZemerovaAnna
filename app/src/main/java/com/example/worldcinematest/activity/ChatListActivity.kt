package com.example.worldcinematest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.worldcinematest.databinding.ActivityChatListBinding

class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
}