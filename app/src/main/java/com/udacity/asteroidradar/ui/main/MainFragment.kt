package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.ui.main.recycler.AsteroidAdapter
import com.udacity.asteroidradar.ui.main.recycler.AsteroidListener

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var asteroidAdapter: AsteroidAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        asteroidAdapter = AsteroidAdapter(AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        binding.asteroidRecycler.adapter = asteroidAdapter

        viewModel.asteroids.observe(viewLifecycleOwner, { asteroids ->
            asteroids?.apply {
                asteroidAdapter.submitList(asteroids)
            }
        })

        viewModel.errorOnFetchingNetworkData.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(
                    activity,
                    R.string.network_error,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.displayNetworkErrorCompleted()
            }
        })

        viewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner, { asteroid ->
            if (asteroid != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_today_asteroids_menu -> viewModel.updateAsteroidFilter(AsteroidFilter.TODAY)
            R.id.show_week_asteroids_menu -> viewModel.updateAsteroidFilter(AsteroidFilter.WEEK)
            R.id.show_saved_asteroids_menu -> viewModel.updateAsteroidFilter(AsteroidFilter.SAVED)
        }

        return true
    }
}
