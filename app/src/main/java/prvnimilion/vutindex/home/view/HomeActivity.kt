package prvnimilion.vutindex.home.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_screen.*
import org.koin.android.viewmodel.ext.android.viewModel
import prvnimilion.vutindex.BaseActivity
import prvnimilion.vutindex.R
import prvnimilion.vutindex.auth.view.LoginActivity
import prvnimilion.vutindex.home.HomePagerAdapter
import prvnimilion.vutindex.home.viewmodel.HomeViewModel
import prvnimilion.vutindex.index.adapters.IndexAdapter
import prvnimilion.vutindex.index.viewmodel.IndexViewModel
import prvnimilion.vutindex.menu.viewmodel.MenuViewModel
import prvnimilion.vutindex.repository.util.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
import prvnimilion.vutindex.repository.util.PermissionsUtil.hasStorageWritePermissions
import prvnimilion.vutindex.repository.util.PermissionsUtil.isStorageWritePermissionGranted
import prvnimilion.vutindex.repository.util.PermissionsUtil.requestStorageWritePermission
import prvnimilion.vutindex.system.viewmodel.SystemViewModel

class HomeActivity : BaseActivity() {

    private val indexViewModel: IndexViewModel by viewModel()
    private val menuViewModel: MenuViewModel by viewModel()
    private val systemViewModel: SystemViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()

    private var shortAnimationDuration: Int = 0
    private var longAnimationDuration: Int = 0
    private var viewDisplacementDistance: Float = -50f

    private lateinit var indexView: View
    private lateinit var menuView: View
    private lateinit var systemView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        longAnimationDuration = shortAnimationDuration * 2

        setupTabLayout()
        setupTabClickListeners()

        showIndexTab()
        showMenuTab()
        showSystemTab()

        setupWorkers()

        home_tab_layout.selectTab(home_tab_layout.getTabAt(0))
        //disables swipe
        home_view_pager.swipeLocked = true
    }

    private fun showIndexTab() {
        val recyclerView = indexView.findViewById<RecyclerView>(R.id.index_recycler_view)
        val progressBar = indexView.findViewById<View>(R.id.index_feed_progress_bar)

        progressBar.bringToFront()
        progressBar.visibility = View.VISIBLE

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
        indexViewModel.getIndex()
        indexViewModel.dataSet.observe(this, Observer {
            if (it == null) return@Observer

            if (recyclerView.adapter == null) {
                recyclerView.adapter = IndexAdapter(this, it)
                progressBar.visibility = View.GONE

            } else {
                (recyclerView.adapter as IndexAdapter).updateDataSet(it)
                recyclerView.adapter?.notifyDataSetChanged()
                indexView.findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh).isRefreshing =
                    false
            }

        })
        indexView.findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh).setOnRefreshListener {
            indexViewModel.getIndex(true)
        }
    }

    private fun showMenuTab() {
        menuViewModel.getIsicCredit()

        menuView.findViewById<TextView>(R.id.logout_button).setOnClickListener {
            menuViewModel.logoutUser()
        }

        menuViewModel.userLoggedOut.observe(this, Observer {
            if (it) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        menuViewModel.userCredit.observe(this, Observer {
            menuView.findViewById<TextView>(R.id.user_credit).text = it
        })
    }

    private fun showSystemTab() {
        val webView = systemView.findViewById<WebView>(R.id.system_webview)
        val loadingSpinner = systemView.findViewById<View>(R.id.loadingPanel)

        loadingSpinner.bringToFront()
        loadingSpinner.visibility = View.VISIBLE

        systemViewModel.setupWebViewClient(webView) { firstLoad ->
            if (firstLoad) {
                loadingSpinner.visibility = View.GONE
            }
            systemView.findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh).isRefreshing =
                false
        }
        systemViewModel.setupChromeWebClient(webView)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        systemViewModel.setupDownloadListener(webView, downloadManager) {
            if (hasStorageWritePermissions(this)) {
                true
            } else {
                requestStorageWritePermission(this)
                false
            }
        }
        systemViewModel.getCookies(webView)
        systemViewModel.getLoginCredentials()
        systemViewModel.loginCredentials.observe(this, Observer {
            systemViewModel.loginIntoSystem(webView)
        })

        systemView.findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh).setOnRefreshListener {
            webView.reload()
        }
    }


    @SuppressLint("InflateParams")
    private fun setupTabLayout() {
        indexView = LayoutInflater.from(this).inflate(R.layout.index_fragment, null)
        menuView = LayoutInflater.from(this).inflate(R.layout.menu_fragment, null)
        systemView = LayoutInflater.from(this).inflate(R.layout.system_fragment, null)

        val views: MutableList<View> = mutableListOf(indexView, menuView, systemView)
        val names: MutableList<String> = mutableListOf(
            getString(R.string.index_tabbar_title),
            getString(R.string.menu_tabbar_title),
            getString(R.string.system_tabbar_title)
        )
        val icons: MutableList<Int> = mutableListOf(
            R.drawable.ic_school_white_24dp,
            R.drawable.ic_menu_white_24dp,
            R.drawable.ic_cloud_white_24dp
        )
        val colors: MutableList<Int> = mutableListOf(
            R.color.tabbarIndexColor,
            R.color.tabbarMenuColor,
            R.color.tabbarSystemColor
        )

        val homePagerAdapter = HomePagerAdapter(this, views, names, icons, colors)
        home_view_pager.adapter = homePagerAdapter
        home_view_pager.offscreenPageLimit = 3
        home_tab_layout.setupWithViewPager(home_view_pager)

        for (i in 0 until home_tab_layout.tabCount) {
            val tab = home_tab_layout.getTabAt(i)
            tab?.customView = homePagerAdapter.getTabView(i)
        }
    }

    private fun setupTabClickListeners() {
        home_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (!homeViewModel.tabLayoutInitialized) {
                    cancelAllSelected()
                    val idle = tab?.customView?.findViewById<View>(R.id.tab_button_idle)!!
                    val active = tab.customView?.findViewById<View>(R.id.tab_button_active)!!
                    crossfade(idle, active)
                    homeViewModel.tabLayoutInitialized = true
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                cancelAllSelected()
                val idle = tab?.customView?.findViewById<View>(R.id.tab_button_idle)!!
                val active = tab.customView?.findViewById<View>(R.id.tab_button_active)!!
                crossfade(idle, active)
            }

        })
    }

    private fun crossfade(current: View, next: View) {
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

            animate().alpha(1f).duration = longAnimationDuration.toLong()

        }
    }

    private fun cancelAllSelected() {
        for (i in 0..home_tab_layout.tabCount) {
            val tab = home_tab_layout.getTabAt(i)
            tab?.customView?.findViewById<MaterialButton>(R.id.tab_button_active)?.visibility =
                View.INVISIBLE
            tab?.customView?.findViewById<ImageView>(R.id.tab_button_idle)?.apply {
                alpha = 1f
                visibility = View.VISIBLE
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(POSITION, home_tab_layout.selectedTabPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        home_view_pager.currentItem = savedInstanceState.getInt(POSITION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (isStorageWritePermissionGranted(permissions, grantResults)) {
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                systemViewModel.downloadFile(downloadManager)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setupWorkers() {
        homeViewModel.setupWorkers()
    }

    companion object {
        const val POSITION = "position"
    }
}