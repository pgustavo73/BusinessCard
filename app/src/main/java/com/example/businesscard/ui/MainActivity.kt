package com.example.businesscard.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.businesscard.App
import com.example.businesscard.R
import com.example.businesscard.data.BusinessCard
import com.example.businesscard.databinding.ActivityMainBinding
import com.example.businesscard.util.Image

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }
    private val adapter by lazy { BusinessCardAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.rvCards.adapter = adapter
        getAllBusinessCard()
        insertListerners()

    }

    private fun insertListerners() {
        binding.fab.setOnClickListener{
            val intent = Intent(this@MainActivity, AddBusinessCardActivity::class.java)
            startActivity(intent)
        }

        adapter.listenerShare = { card ->
            Image.share(this@MainActivity, card)
        }


        adapter.listernerDelete = {
            showRemoveDialog(it)
            getAllBusinessCard()
        }

    }


    private fun getAllBusinessCard() {
        mainViewModel.getAll().observe(this, { businessCards ->
            adapter.submitList(businessCards)
        })
    }

    private fun showRemoveDialog(businessCard: BusinessCard) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_delete))
            .setMessage(getString(R.string.dialog_delete_msg))
            .setNegativeButton(getString(R.string.label_no), null)
            .setPositiveButton(getString(R.string.label_yes)
            ) { _, _ ->
                mainViewModel.delete(businessCard)
            }
            .create()
            .show()
    }



}