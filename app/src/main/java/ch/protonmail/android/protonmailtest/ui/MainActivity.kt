package ch.protonmail.android.protonmailtest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ch.protonmail.android.protonmailtest.R
import ch.protonmail.android.protonmailtest.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tabAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupViewPager(binding.viewpager)
    }

    private fun setupViewPager(viewPager: ViewPager2) {
        tabAdapter = TabAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = tabAdapter

        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.main_pager_upcoming)
                1 -> {
                    tab.text = getString(R.string.main_pager_hottest)
                    tab.icon = getDrawable(this, R.drawable.ic_baseline_whatshot_24)
                }
                else -> {
                    /* Nothing to do */
                }
            }
        }.attach()
    }

    internal class TabAdapter(supportFragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(supportFragmentManager, lifecycle) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> UpcomingFragment()
            else -> HottestFragment()
        }

    }

}
