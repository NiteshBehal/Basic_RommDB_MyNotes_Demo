package com.rommdb.mynotes.ui


import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rommdb.mynotes.R
import com.rommdb.mynotes.db.Note
import com.rommdb.mynotes.db.NoteDatabase
import com.rommdb.mynotes.utils.toast
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class AddNoteFragment : BaseFragment() {

    private var mNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            mNote = AddNoteFragmentArgs.fromBundle(it).note
            et_title.setText(mNote?.title)
            et_note.setText(mNote?.note)
        }
        button_save.setOnClickListener { view ->
            val noteTitle = et_title.text.toString().trim()
            val noteBody = et_note.text.toString().trim()

            if (!TextUtils.isEmpty(noteTitle) && !TextUtils.isEmpty(noteBody)) {
                launch {
                    context?.let {
                        val note = Note(noteTitle, noteBody)
                        if (mNote == null) {
                            NoteDatabase(it).getNoteDao().addNote(note)
                            it.toast("Saved")
                        } else {
                            note.id = mNote!!.id
                            NoteDatabase(it).getNoteDao().updateNote(note)
                            it.toast("Updated")
                        }


                        val action = AddNoteFragmentDirections.actionSaveNote()
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_note -> {
                deleteItem()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteItem() {
        if (mNote != null) {
            AlertDialog.Builder(context).apply {
                setTitle("Are you sure?")
                setMessage("You cannot undo this operation")
                setPositiveButton("Yes") { _, _ ->
                    launch {
                        context?.let {
                           NoteDatabase(it).getNoteDao().deleteNote(mNote!!)
                            it.toast("Deleted")
                            val action = AddNoteFragmentDirections.actionSaveNote()
                            Navigation.findNavController(view!!).navigate(action)
                        }
                    }
                }
                setNegativeButton("No") { _, _ ->

                }
            }.create().show()
        } else {
            context?.toast("Cannot Delete")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_menu, menu)
    }
}
