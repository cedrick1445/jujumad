package ced.cedie.cedrick.jujump.presentation.helper

import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import ced.cedie.cedrick.jujump.presentation.utils.Constants

class PermissionHelper(context: Context) : ContextWrapper(context) {

    private var activityResult: ActivityResultLauncher<Array<String>>? = null

    fun isLackPermissions(): Boolean {
        return Constants.PERMISSIONS.any {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(activityResult: ActivityResultLauncher<Array<String>>) {
        this.activityResult = activityResult
        activityResult.launch(Constants.PERMISSIONS.toTypedArray())
    }

    fun hasAllPermissionsGranted(): Boolean {
        return !isLackPermissions()
    }

    fun showDialog() {
        val permissionDialog = AlertDialog.Builder(this)
        permissionDialog.setTitle("Permission Needed")
            .setMessage("Some permissions are needed to proceed. Please allow the required permissions.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                activityResult?.launch(Constants.PERMISSIONS.toTypedArray())
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.create()

        permissionDialog.show()
    }
}