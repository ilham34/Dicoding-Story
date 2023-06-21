package com.ilham.submissionstory.view.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.submissionstory.R
import com.ilham.submissionstory.adapter.LoadingStateAdapter
import com.ilham.submissionstory.adapter.StoriesAdapter
import com.ilham.submissionstory.databinding.ActivityMainBinding
import com.ilham.submissionstory.model.UserPreference
import com.ilham.submissionstory.view.ViewModelFactory
import com.ilham.submissionstory.view.addstory.AddStoryActivity
import com.ilham.submissionstory.view.maps.MapsActivity
import com.ilham.submissionstory.view.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_list_stories)

        binding.listStories.layoutManager = LinearLayoutManager(this)
        setStory()
        addStory()
        swipeRefresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                mainViewModel.logout()
                true
            }
            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> true
        }
    }

    private fun addStory() {
        binding.fabButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            setStory()
            binding.swipeRefresh.isRefreshing = false
        }
    }


    private fun setStory() {
        val layoutManager = LinearLayoutManager(this)
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.listStories.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.listStories.layoutManager = layoutManager
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.getUser().collect {
                if (it.isLogin) {
                    val adapter = StoriesAdapter()

                    binding.listStories.adapter = adapter.withLoadStateFooter(
                        footer = LoadingStateAdapter {
                            adapter.retry()
                        }
                    )

                    mainViewModel.getStories(it.token).observe(this@MainActivity) { result ->
                        adapter.submitData(lifecycle, result)
                    }
                } else {
                    moveIntent()
                }
            }
        }
    }

    private fun moveIntent() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}