package com.cricket.driver_assistant

import android.content.Context
import android.util.Log
import org.json.JSONObject

class EmergencyListManager(
    private val mContext: Context,
    private val mJsInterface: JsInterface) {

    init {
        val lDb = DbManager(mContext, null);

//        val dDb = DbFiller(mContext, null)
//        dDb.fillEmergencyListDB()

        val lList = lDb.getEmergencyList()
        lDb.close()

        for (i in lList)
            mJsInterface.createEmergencyListButton(i)
    }
}