package com.jme.common.network;

import android.net.Uri;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.NetworkPolicy;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by XuJun on 2018/11/7.
 */

public class ImageDownLoader implements Downloader {

    private OkHttpClient client;

    public ImageDownLoader(OkHttpClient okHttpClient) {
        this.client = okHttpClient;
    }

    @Override
    public Response load(Uri uri, int networkPolicy) throws IOException {
        CacheControl cacheControl = null;

        if (networkPolicy != 0) {
            if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
                cacheControl = CacheControl.FORCE_CACHE;
            } else {
                CacheControl.Builder builder = new CacheControl.Builder();

                if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy))
                    builder.noCache();

                if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy))
                    builder.noStore();

                cacheControl = builder.build();
            }
        }

        Request.Builder builder = new Request.Builder().url(uri.toString());

        if (cacheControl != null)
            builder.cacheControl(cacheControl);

        okhttp3.Response response = client.newCall(builder.build()).execute();

        int responseCode = response.code();

        if (responseCode >= 300) {
            response.body().close();

            throw new ResponseException(responseCode + " " + response.message(), networkPolicy,
                    responseCode);
        }

        boolean fromCache = response.cacheResponse() != null;

        ResponseBody responseBody = response.body();

        return new Response(responseBody.byteStream(), fromCache, responseBody.contentLength());
    }


    @Override
    public void shutdown() {
        Cache cache = client.cache();

        if (cache != null) {
            try {
                cache.close();
            } catch (IOException ignored) {
            }
        }
    }
}

