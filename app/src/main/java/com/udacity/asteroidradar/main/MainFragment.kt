package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.recycler.AsteroidAdapter
import com.udacity.asteroidradar.main.recycler.AsteroidListener

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val asteroidAdapter = AsteroidAdapter(AsteroidListener { asteroidId ->
            Toast.makeText(context, "$asteroidId", Toast.LENGTH_LONG).show()
        })
        binding.asteroidRecycler.adapter = asteroidAdapter
        val list = listOf(
            Asteroid(12345678, "2018 BF5", "2020-02-08", 24.3, 0.622358, 15.515735, 0.445338, true),
            Asteroid(123456789, "2018 BF5", "2020-02-08", 24.3, 0.622358, 15.515735, 0.445338, false)
        )
        asteroidAdapter.submitList(list)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
