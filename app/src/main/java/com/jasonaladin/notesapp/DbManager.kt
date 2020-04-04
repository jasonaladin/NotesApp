package com.jasonaladin.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager{
    val dbName = "MyNotes"
    val dbTable = "Notes"
    val colId = "ID"
    val colTitle = "Title"
    val colNote = "Note"
    val dbVersion = 1
    //CREATE TABLE IF NOT EXIST dbTable (colId INTEGER PRIMARY KEY, colTitle TEXT, colNote TEXT);
    val sqlCreateTable =
        "CREATE TABLE IF NOT EXISTS $dbTable ($colId INTEGER PRIMARY KEY, $colTitle TEXT, $colNote TEXT);"
    var sqlDB:SQLiteDatabase? = null
    constructor(context: Context){
        val db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes:SQLiteOpenHelper{
        var context:Context? = null
        constructor(context: Context):super(context, dbName,null,dbVersion){
            this.context = context

        }
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"Database is created",Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS " + dbTable)
        }

    }

    fun create(values:ContentValues):Long{
        val id = sqlDB!!.insert(dbTable,"",values)
        return id
    }

    fun read(projection:Array<String>, selection:String, selectionArgs:Array<String>, sortOrder:String):Cursor{
        val qDb = SQLiteQueryBuilder()
        qDb.tables = dbTable
        val cursor = qDb.query(sqlDB,projection,selection,selectionArgs,null,null,sortOrder)
        return cursor
    }

    fun update(values:ContentValues, selection: String, selectionArgs: Array<String>): Int{
        val count = sqlDB!!.update(dbTable,values,selection,selectionArgs)
        return count
    }

    fun delete(selection:String,selectionArgs:Array<String>):Int{
        val count = sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }


}