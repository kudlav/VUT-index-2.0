package prvnimilion.vutindex.repository.util

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101

object PermissionsUtil {

    fun hasStorageWritePermissions(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun requestStorageWritePermission(activity: Activity) {
        activity.requestPermissions(
            arrayOf(WRITE_EXTERNAL_STORAGE),
            PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
        )
    }

    fun isStorageWritePermissionGranted(grantPermissions: Array<out String>, grantResults: IntArray): Boolean {
        for (i in grantPermissions.indices) {
            if (WRITE_EXTERNAL_STORAGE == grantPermissions[i]) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }
}