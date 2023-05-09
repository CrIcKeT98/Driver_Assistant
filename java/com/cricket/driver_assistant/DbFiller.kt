package com.cricket.driver_assistant

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbFiller(context: Context?, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DbManager.DATABASE_NAME, factory, DbManager.DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase) {

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

    }

    fun fillEmergencyListDB(){
        val db = this.writableDatabase
        val values = ContentValues()

        val testData = listOf<String>(
            "Действия машиниста при вынужденной остановке",
            "Порядок покидания кабины управления",
            "Порядок снятия высокого напряжения",
            "Порядок проверки отсутствия сопротивлению",
            "Порядок перехода на комплекты АРС",
            "Порядок перехода на УОС",
            "Порядок отпуска пневмотормоза на вагоне",
            "Порядок перехода на КРУ",
            "Порядок перехода на кран машиниста",
            "Порядок приведения поезда в движение на подъеме"
        )

        var lCounter = 1
        for(i in testData){
            values.put(DbManager.TEXT_COL, i.toString())
            values.put(DbManager.ROOT_BUTTON_ID_COL, lCounter)
            values.put(DbManager.TAGS_COL, "")
            values.put(DbManager.DEPOT_COL, "Северное")
            values.put(DbManager.TRAIN_TYPE, "81-740")
            values.put(DbManager.RELEASE_DATE, "")

            db.insert(DbManager.EMERGENCY_LIST, null, values)

            values.clear()
            lCounter++
        }

        db.close()
    }

    fun fillActionButtonListDB(){
        val db = this.writableDatabase
        val values = ContentValues()
    }
}