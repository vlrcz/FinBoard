package com.vlad.finboard

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vlad.finboard.databinding.ActivityMainBinding
import com.vlad.finboard.main.MainFragment
import com.vlad.finboard.navigation.CustomAction
import com.vlad.finboard.navigation.HasCustomAction
import com.vlad.finboard.navigation.HasCustomTitle
import com.vlad.finboard.navigation.Navigator

class MainActivity : AppCompatActivity(R.layout.activity_main), Navigator {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi(f)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.topAppBar)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, MainFragment())
                .commit()
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun goToMain() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateUi(fragment: Fragment) {

        if (fragment is HasCustomTitle) {
            supportActionBar?.title = getString(fragment.getTitleRes())
        } else {
            supportActionBar?.title = getString(R.string.main_title)
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            setDisplayHome(true)
        } else {
            setDisplayHome(false)
        }

        if (fragment is HasCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        }
    }

    private fun setDisplayHome(set: Boolean) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(set)
            setDisplayShowHomeEnabled(set)
        }
    }

    private fun createCustomToolbarAction(action: CustomAction) {
        binding.topAppBar.menu.clear()
        val iconDrawable = ContextCompat.getDrawable(this, action.iconRes)
            ?.let {
                it.setTint(Color.WHITE)
                DrawableCompat.wrap(it)
            }

        binding.topAppBar.menu.add(action.textRes).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            icon = iconDrawable
            setOnMenuItemClickListener {
                action.onCustomAction.run()
                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .addToBackStack(null)
            .replace(R.id.container, fragment)
            .commit()
    }
}