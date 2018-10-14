package com.mikkipastel.exoplayer18

import android.app.Notification
import android.support.annotation.Nullable
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.ui.DownloadNotificationUtil
import com.google.android.exoplayer2.offline.DownloadManager.TaskState
import com.google.android.exoplayer2.scheduler.Scheduler
import com.mikkipastel.exoplayer18.C.DOWNLOAD_CHANNEL_ID
import com.mikkipastel.exoplayer18.C.DOWNLOAD_NOTIFICATION_ID


class AudioDownloadService : DownloadService(
        DOWNLOAD_NOTIFICATION_ID,
        DownloadService.DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
        DOWNLOAD_CHANNEL_ID,
        R.string.download_channel_name) {

    override fun getDownloadManager(): DownloadManager {
        return DownloadUtil.getDownloadManager(this)
    }

    @Nullable
    override fun getScheduler(): Scheduler? {
        return null
    }

    override fun getForegroundNotification(taskStates: Array<TaskState>): Notification {
        return DownloadNotificationUtil.buildProgressNotification(
                this,
                R.drawable.exo_icon_play,
                DOWNLOAD_CHANNEL_ID, null, null,
                taskStates)
    }

}