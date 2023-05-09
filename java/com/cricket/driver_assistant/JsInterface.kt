package com.cricket.driver_assistant

import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import org.json.JSONObject

class JsInterface(private val mContext: Context,
                  private val mWebView: WebView) {
    //JS Interface

    //JS return id as string (not int)
    @JavascriptInterface
    fun emergencyButtonListListener(id: String, text: String){
        //"All WebView methods must be called on the same thread"
        mWebView.post(Runnable {
            mWebView.loadUrl("file:///android_asset/action_list.html?id=$id&text=$text")
        })
    }

    @JavascriptInterface
    fun actionButtonListListener(id: String) {
        if(ActionListInterface.mStaticListManager != null)
            ActionListInterface.mStaticListManager!!.actionButtonListHandler(id.toInt())
    }

    @JavascriptInterface
    fun showToast(): String {
        Toast.makeText(mContext, "Turbodimooooooooooon", Toast.LENGTH_SHORT).show()
        return "abcd"
    }

//---------------------------------------------------------------
//Calling Js functions
//---------------------------------------------------------------

    fun createEmergencyListButton(obj: JSONObject){
        mWebView.evaluateJavascript("createEmergencyListButton($obj);") {}
    }

    fun createActionButton(obj: JSONObject, isLast: Boolean){
        mWebView.evaluateJavascript("createActionButton($obj, $isLast);") {}
    }

    fun setActionListTitle(text: String){
        mWebView.evaluateJavascript("setActionListTitle(\"$text\");") {}
    }

    fun setActionButtonState(newState: Int){
        mWebView.post {
            mWebView.evaluateJavascript("setActionButtonState($newState);") {}
        }
    }
}