package com.cricket.driver_assistant

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import java.net.URI
import java.util.*

class MainActivity : AppCompatActivity() {
    private val mWebView: WebView by lazy {
        findViewById(R.id.activityContext)
    }

    //Problem: if create JsInterface in Managers classes then will be a problem: js code cant find Android functions
    private val mJsInterface: JsInterface by lazy{
        JsInterface(this, mWebView)
    }

    private var mEmergencyListManager: EmergencyListManager? = null
    private var mActionListManager: ActionListManager? = null

    //--------------------------------------------------------------------------

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mWebView.settings.javaScriptEnabled = true
        mWebView.addJavascriptInterface(mJsInterface, "Android")

        mWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {}

            override fun onPageFinished(view: WebView?, url: String?) {
                if(url == null)
                    return

                if(url.endsWith("index.html"))
                    mEmergencyListManager = EmergencyListManager(this@MainActivity, mJsInterface)
                else if("action_list" in url) {
                    val lArgs = parseUrlArgs(url)

                    mActionListManager = ActionListManager(this@MainActivity,
                        mJsInterface,
                        lArgs["id"]!!.toInt(),
                        lArgs["text"]!!)

                    ActionListInterface.mStaticListManager = mActionListManager
                }
            }
        }

        mWebView.webChromeClient = WebChromeClient()
        mWebView.loadUrl("file:///android_asset/index.html")
    }

    fun parseUrlArgs(url: String) : Map<String, String>{
        val lUri = URI(url)
        val query = lUri.query

        val lArgs = query.split("&")
            .map { it.split("=") }
            .map { it[0] to it[1] }
            .toMap()

        return lArgs
    }
}