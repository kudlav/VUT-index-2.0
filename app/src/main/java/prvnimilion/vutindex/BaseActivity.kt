package prvnimilion.vutindex

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

const val TAG = "VUTDebug"

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        Timber.tag(TAG).d("Activity: $this onCreate")
    }

    override fun onResume() {
        super.onResume()

        Timber.tag(TAG).d("Activity: $this onResume")
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.tag(TAG).d("Activity: $this onDestroy")
    }

    override fun onStop() {
        super.onStop()

        Timber.tag(TAG).d("Activity: $this onStop")
    }
}