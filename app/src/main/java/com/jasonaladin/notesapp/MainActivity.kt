/*
Ginawa ni jasonaladin
Halos lahat ng code ay galing sa tutorial na aking inaaral
*/

package com.jasonaladin.notesapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
    //data container
    var listNotesArray = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*
        //add dummy data

        listNotesArray.add(Note(1,"name1","Aliquam mi nisi, lobortis vel semper id, finibus non ligula. Donec consequat ex id feugiat."))
        listNotesArray.add(Note(2,"name2","Mauris porttitor, mauris in rutrum lacinia, ex erat condimentum nunc, at lacinia urna quam ac."))
        listNotesArray.add(Note(3,"name3","Quisque malesuada metus laoreet rutrum auctor. Suspendisse non fermentum turpis. Cras tempor dignissim lectus at."))
        */

        loadQuery("%")

    }

    override fun onResume() {
        loadQuery("%")
        super.onResume()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)


        //initialize SearchView and SearchManager needed to do search
        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val systemService = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(systemService.getSearchableInfo(componentName))
        //code to listen for string that will be search
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query, Toast.LENGTH_SHORT).show()
                loadQuery("%"+query+"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }


        })//end of listening

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                loadQuery("%")
                return false
            }
        })



        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!=null){
            when(item.itemId){
                R.id.addNote ->{
                    var intent = Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }


    //custom adapter for our listview
    inner class MyCustomListAdapter: BaseAdapter {
        var listNotesAdapter = ArrayList<Note>()
        var context:Context? = null
        constructor(listNotesAdapter:ArrayList<Note>, context: Context):super(){
            this.listNotesAdapter = listNotesAdapter
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var layoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
            var myView = layoutInflater.inflate(R.layout.ticket,null)
            var myNote = listNotesAdapter[position]
            myView.tvDes.text = myNote.nodeNote
            myView.tvTitle.text = myNote.nodeTitle
            myView.ivDel.setOnClickListener(View.OnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeID.toString())
                dbManager.delete("ID=?",selectionArgs)
                loadQuery("%")
            })
            myView.llTicket.setOnClickListener(View.OnClickListener {
                goToUpdate(myNote)
            })

            return myView
        }



        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]

        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

    }

    fun loadQuery(title:String){
        listNotesArray.clear()
        val dbManager = DbManager(this)
        val projection = arrayOf("ID","Title","Note")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.read(projection, "Title like ?", selectionArgs, "Title")

        if(cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndex("ID"))
                val titleCol = cursor.getString(cursor.getColumnIndex("Title"))
                val note = cursor.getString(cursor.getColumnIndex("Note"))
                listNotesArray.add(Note(id,titleCol,note))
            }while(cursor.moveToNext())
        }

        val myCustomListAdapter = MyCustomListAdapter(listNotesArray, this)
        lvNotes.adapter = myCustomListAdapter
    }

    fun goToUpdate(note:Note){
        var intent = Intent(this,AddNotes::class.java)
        intent.putExtra("ID",note.nodeID)
        intent.putExtra("Title",note.nodeTitle)
        intent.putExtra("Note",note.nodeNote)
        startActivity(intent)
    }
}
