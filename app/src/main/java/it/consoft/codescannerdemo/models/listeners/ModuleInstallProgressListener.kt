package it.consoft.codescannerdemo.models.listeners

import android.util.Log
import com.google.android.gms.common.moduleinstall.InstallStatusListener
import com.google.android.gms.common.moduleinstall.ModuleInstallClient
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_CANCELED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_FAILED


internal class ModuleInstallProgressListener(moduleInstallClient: ModuleInstallClient) : InstallStatusListener {

    var moduleInstallClient: ModuleInstallClient

    init {
        this.moduleInstallClient = moduleInstallClient
    }
    override fun onInstallStatusUpdated(update: ModuleInstallStatusUpdate) {
        val progressInfo = update.progressInfo
        // Progress info is only set when modules are in the progress of downloading.
        if (progressInfo != null) {
            val progress =
                (progressInfo.bytesDownloaded * 100 / progressInfo.totalBytesToDownload).toInt()
            // Set the progress for the progress bar.
            //progressBar.setProgress(progress)
            Log.d("progress module install", "onInstallStatusUpdated: $progress")
        }
        // Handle failure status maybe…

        // Unregister listener when there are no more install status updates.
        if (isTerminateState(update.installState)) {
            moduleInstallClient.unregisterListener(this)
        }
    }

    fun isTerminateState(@InstallState state: Int): Boolean {
        return state == STATE_CANCELED || state == STATE_COMPLETED || state == STATE_FAILED
    }
}

