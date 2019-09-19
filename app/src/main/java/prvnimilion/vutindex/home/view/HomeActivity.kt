package prvnimilion.vutindex.home.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import kotlinx.android.synthetic.main.home_screen.*
import org.koin.android.viewmodel.ext.android.viewModel
import prvnimilion.vutindex.BaseActivity
import prvnimilion.vutindex.R
import prvnimilion.vutindex.home.viewmodel.HomeNavViewModel
import prvnimilion.vutindex.indexf.index.view.IndexFragment
import timber.log.Timber

class HomeActivity : BaseActivity() {

    private val homeNavViewModel : HomeNavViewModel by viewModel()

    private var shortAnimationDuration: Int = 0
    private var longAnimationDuration: Int = 0
    private var viewDisplacementDistance: Float = -50f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        longAnimationDuration = shortAnimationDuration * 2
    }

    override fun onStart() {
        super.onStart()

        when (homeNavViewModel.currentFragment) {
            HomeNavViewModel.INDEX_FRAGMENT -> {
                startFragment(
                    index_idle,
                    index_selected,
                    IndexFragment(),
                    homeNavViewModel.currentFragment
                )
            }
            HomeNavViewModel.MENU_FRAGMENT -> {
                startFragment(
                    menu_idle,
                    menu_selected,
                    MenuFragment(),
                    homeNavViewModel.currentFragment
                )
            }
            HomeNavViewModel.SYSTEM_FRAGMENT -> {
                startFragment(
                    system_idle,
                    system_selected,
                    SystemFragment(),
                    homeNavViewModel.currentFragment
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        changeInteractions(isClickable = true)

        index_idle.setOnClickListener {
            cancelAllSelected()

            hideCurrentFragment(homeNavViewModel.currentFragment)
            crossfade(index_idle, index_selected) {
                if (supportFragmentManager.findFragmentByTag(HomeNavViewModel.INDEX_FRAGMENT)?.isAdded == true) {
                    Timber.tag("__").d("show INDEX")
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentByTag(HomeNavViewModel.INDEX_FRAGMENT)!!)
                        .commitAllowingStateLoss()
                } else {
                    Timber.tag("__").d("add INDEX")
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.fragment_container,
                            IndexFragment(),
                            HomeNavViewModel.INDEX_FRAGMENT
                        )
                        .commitAllowingStateLoss()
                }
            }
            homeNavViewModel.updateCurrentFragment(HomeNavViewModel.INDEX_FRAGMENT)
        }

        menu_idle.setOnClickListener {
            cancelAllSelected()

            hideCurrentFragment(homeNavViewModel.currentFragment)
            crossfade(menu_idle, menu_selected) {
                if (supportFragmentManager.findFragmentByTag(HomeNavViewModel.MENU_FRAGMENT)?.isAdded == true) {
                    Timber.tag("__").d("show MENU")
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentByTag(HomeNavViewModel.MENU_FRAGMENT)!!)
                        .commitAllowingStateLoss()
                } else {
                    Timber.tag("__").d("add MENU")
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.fragment_container,
                            MenuFragment(),
                            HomeNavViewModel.MENU_FRAGMENT
                        )
                        .commitAllowingStateLoss()
                }
            }
            homeNavViewModel.updateCurrentFragment(HomeNavViewModel.MENU_FRAGMENT)
        }

        system_idle.setOnClickListener {
            cancelAllSelected()

            hideCurrentFragment(homeNavViewModel.currentFragment)
            crossfade(system_idle, system_selected) {
                if (supportFragmentManager.findFragmentByTag(HomeNavViewModel.SYSTEM_FRAGMENT)?.isAdded == true) {
                    Timber.tag("__").d("show STORE")
                    supportFragmentManager.beginTransaction()
                        .show(supportFragmentManager.findFragmentByTag(HomeNavViewModel.SYSTEM_FRAGMENT)!!)
                        .commitAllowingStateLoss()
                } else {
                    Timber.tag("__").d("add STORE")
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.fragment_container,
                            SystemFragment(),
                            HomeNavViewModel.SYSTEM_FRAGMENT
                        )
                        .commitAllowingStateLoss()
                }
            }
            homeNavViewModel.updateCurrentFragment(HomeNavViewModel.SYSTEM_FRAGMENT)
        }
    }

    private fun startFragment(idle: View, selected: View, fragment: Fragment, tag: String) {
        cancelAllSelected()
        idle.visibility = View.INVISIBLE
        selected.visibility = View.VISIBLE

        Timber.tag("__").d("add $tag")
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container,
            fragment,
            tag
        ).commitAllowingStateLoss()
    }

    private fun changeInteractions(isClickable: Boolean) {
        index_idle.isClickable = isClickable
        menu_idle.isClickable = isClickable
        system_idle.isClickable = isClickable
    }

    private fun cancelAllSelected() {
        menu_selected.visibility = View.INVISIBLE
        index_selected.visibility = View.INVISIBLE
        system_selected.visibility = View.INVISIBLE

        menu_idle.apply {
            alpha = 1f
            visibility = View.VISIBLE
        }

        index_idle.apply {
            alpha = 1f
            visibility = View.VISIBLE
        }

        system_idle.apply {
            alpha = 1f
            visibility = View.VISIBLE
        }
    }

    private fun crossfade(current: View, next: View, startFragment: () -> Unit) {

        changeInteractions(isClickable = false)

        ObjectAnimator.ofFloat(current, "translationX", viewDisplacementDistance).apply {
            interpolator = LinearOutSlowInInterpolator()
            duration = shortAnimationDuration.toLong()
            start()
        }

        current.animate()
            .alpha(0f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    current.translationX -= viewDisplacementDistance
                    current.visibility = View.INVISIBLE
                }
            })


        next.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(longAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        startFragment()

                        changeInteractions(isClickable = true)
                    }
                })
        }
    }

    private fun hideCurrentFragment(tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag)?.isAdded == true) {
            Timber.tag("__").d("hide $tag")
            supportFragmentManager.beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(tag)!!).commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        hideCurrentFragment(homeNavViewModel.currentFragment)
    }
}