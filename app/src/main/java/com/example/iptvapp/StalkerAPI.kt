package com.example.iptvapp

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class StalkerAPI(private val portalUrl: String, private val mac: String) {
    
    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request().newBuilder()
            .header("User-Agent", "Mozilla/5.0 (QtEmbedded; U; Linux; C) AppleWebKit/533.3 MAG200 stbapp ver: 2 rev: 250 Safari/533.3")
            .header("Cookie", "mac=${mac.uppercase()}; stb_lang=en")
            .build()
        chain.proceed(request)
    }.build()

    var token: String? = null
    private val cleanUrl = portalUrl.trim().removeSuffix("/")

    // دالة تجربة الاتصال (Handshake)
    fun handshake(): Boolean {
        val paths = listOf("/portal.php", "/c/portal.php")
        for (path in paths) {
            val url = "$cleanUrl$path?type=stb&action=handshake&mac=$mac&JsHttpRequest=1-xml"
            try {
                client.newCall(Request.Builder().url(url).build()).execute().use { response ->
                    if (response.isSuccessful) {
                        val js = JSONObject(response.body?.string() ?: "").optJSONObject("js")
                        if (js?.has("token") == true) {
                            this.token = js.getString("token")
                            return true
                        }
                    }
                }
            } catch (e: Exception) { continue }
        }
        return false
    }

    // دالة لجلب القنوات
    fun getChannels(): String {
        val url = "$cleanUrl/portal.php?type=itv&action=get_ordered_list&mac=$mac&token=$token&JsHttpRequest=1-xml"
        return try {
            client.newCall(Request.Builder().url(url).build()).execute().use { it.body?.string() ?: "" }
        } catch (e: Exception) { "" }
    }
}

