package com.cricket.driver_assistant

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.json.JSONObject

class DbManager(context: Context?, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        var query = ("CREATE TABLE IF NOT EXISTS " + EMERGENCY_LIST + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                TEXT_COL + " TEXT," +
                ROOT_BUTTON_ID_COL + " INTEGER," +
                TAGS_COL + " TEXT," +
                DEPOT_COL + " TEXT," +
                TRAIN_TYPE + " TEXT," +
                RELEASE_DATE + " TEXT)")

        db.execSQL(query)

        query = ("CREATE TABLE IF NOT EXISTS " + ACTION_BUTTON_LIST + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                TEXT_COL + " TEXT," +
                DESCRIPTION_COL + " TEXT," +
                PARENT_ID_COL + " INTEGER, " +
                IS_BINARY_COL + " INTEGER)")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

    }

    //---------------------------------------------------------------
    //Getters
    //---------------------------------------------------------------

    @SuppressLint("Range")
    fun getEmergencyList(): ArrayList<JSONObject> {
        val db = this.readableDatabase
        val final = ArrayList<JSONObject>()
        val cursor = db.rawQuery("SELECT * FROM $EMERGENCY_LIST", null)

        if(cursor.moveToFirst()) {
            do {
                val lObj = JSONObject()
                lObj.put(TEXT_COL, cursor.getString(cursor.getColumnIndex(TEXT_COL)))
                lObj.put("button_id", cursor.getInt(cursor.getColumnIndex(ROOT_BUTTON_ID_COL)))
                final.add(lObj)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return final
    }

    @SuppressLint("Range")
    fun getButtonById(id: Int): JSONObject {
        val db = this.readableDatabase
        val final = JSONObject()
        val cursor = db.rawQuery("SELECT * FROM $ACTION_BUTTON_LIST WHERE $ID_COL=?", arrayOf(id.toString()))

        if(cursor.moveToFirst()) {
            final.put(ID_COL, cursor.getInt(cursor.getColumnIndex(ID_COL)))
            final.put(TEXT_COL, cursor.getString(cursor.getColumnIndex(TEXT_COL)))
            final.put(DESCRIPTION_COL, cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL)))
            final.put(PARENT_ID_COL, cursor.getInt(cursor.getColumnIndex(PARENT_ID_COL)))
            final.put(IS_BINARY_COL, cursor.getInt(cursor.getColumnIndex(IS_BINARY_COL)))
            final.put("current_state", 0)
        }

        cursor.close()

        return final
    }

    @SuppressLint("Range")
    fun getButtonsByParentId(id: Int): ArrayList<JSONObject> {
        val db = this.readableDatabase
        val final = ArrayList<JSONObject>()
        val cursor = db.rawQuery("SELECT * FROM $ACTION_BUTTON_LIST WHERE $PARENT_ID_COL=?", arrayOf(id.toString()))

        if(cursor.moveToFirst()) {
            do {
                val lObj = JSONObject()
                lObj.put(ID_COL, cursor.getInt(cursor.getColumnIndex(ID_COL)))
                lObj.put(TEXT_COL, cursor.getString(cursor.getColumnIndex(TEXT_COL)))
                lObj.put(DESCRIPTION_COL, cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL)))
                lObj.put(PARENT_ID_COL, cursor.getInt(cursor.getColumnIndex(PARENT_ID_COL)))
                lObj.put(IS_BINARY_COL, cursor.getInt(cursor.getColumnIndex(IS_BINARY_COL)))
                lObj.put("current_state", 0)
                final.add(lObj)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return final
    }

    companion object {
        const val DATABASE_NAME = "DRIVER_ASSISTANT"
        const val DATABASE_VERSION = 1

        //Tables name
        const val EMERGENCY_LIST = "emergency_list"
        const val ACTION_BUTTON_LIST = "action_button_list"

        //Emergency list cols
        const val ID_COL = "id"
        const val TEXT_COL = "text"
        const val ROOT_BUTTON_ID_COL = "root_button_id"
        const val TAGS_COL = "tags"
        const val DEPOT_COL = "depot"
        const val TRAIN_TYPE = "train_type"
        const val RELEASE_DATE = "release_date"

        //Action button cols
        //const val ID_COL = "id"
        //const val TEXT_COL = "text"
        const val DESCRIPTION_COL = "description"
        const val PARENT_ID_COL = "parent_id"
        const val IS_BINARY_COL = "is_binary"

    }
}