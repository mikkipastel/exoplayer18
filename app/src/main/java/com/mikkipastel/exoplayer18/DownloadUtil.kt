package com.mikkipastel.exoplayer18

import android.content.Context
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import java.io.File


object DownloadUtil {
    private var cache: Cache? = null
    private var downloadManager: DownloadManager? = null

    @Synchronized
    fun getCache(context: Context): Cache {
        if (cache == null) {
            val cacheDirectory = File(context.getExternalFilesDir(null), "downloads")
            cache = SimpleCache(cacheDirectory, NoOpCacheEvictor())
        }
        return cache as Cache
    }

    @Synchronized
    fun getDownloadManager(context: Context): DownloadManager {
        if (downloadManager == null) {
            val actionFile = File(context.externalCacheDir, "actions")
            downloadManager = DownloadManager(
                    getCache(context),
                    DefaultDataSourceFactory(
                            context,
                            Util.getUserAgent(context, context.getString(R.string.app_name))),
                    actionFile,
                    ProgressiveDownloadAction.DESERIALIZER)
        }
        return downloadManager as DownloadManager
    }
}