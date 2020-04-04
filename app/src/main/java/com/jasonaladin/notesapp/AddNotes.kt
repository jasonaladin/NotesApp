package com.jasonaladin.notesapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    val dbTable = "Notes"
    var ID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        try{
            var bundle: Bundle? = intent.extras
            ID = bundle!!.getInt("ID")
            if(ID != 0){
                etTitleAdd.setText(bundle.getString("Title"))
                etNotesAdd.setText(bundle.getString("Note"))
            }
        }catch (ex:Exception){ }

    }

    fun addNote(view: View) {
        val dbManager = DbManager(this)
        var values = ContentValues()
        values.put("Title",etTitleAdd.text.toString())
        values.put("Note",etNotesAdd.text.toString())
        if(ID == 0) {
            val id = dbManager.create(values)
            if (id > 0) {
                Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add in database", Toast.LENGTH_SHORT).show()
            }
        }else{
            val selectionArgs = arrayOf(ID.toString())
            val id = dbManager.update(values,"ID=? ",selectionArgs)
            if (id > 0) {
                Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add in database", Toast.LENGTH_SHORT).show()
            }

        }
    }


}
