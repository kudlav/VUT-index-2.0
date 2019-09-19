package prvnimilion.vutindex.home.viewmodel

import androidx.lifecycle.ViewModel

class HomeNavViewModel : ViewModel() {


    var currentFragment: String = INDEX_FRAGMENT
        private set


    fun updateCurrentFragment(f: String) {
        currentFragment = f
    }

    companion object {
        const val INDEX_FRAGMENT = "INDEX"
        const val MENU_FRAGMENT = "MENU"
        const val SYSTEM_FRAGMENT = "SYSTEM"
    }
}