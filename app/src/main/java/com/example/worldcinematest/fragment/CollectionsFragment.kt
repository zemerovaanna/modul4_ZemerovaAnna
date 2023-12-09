package com.example.worldcinematest.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worldcinematest.common.AppDatabase
import com.example.worldcinematest.common.MyCollection
import com.example.worldcinematest.common.MyCollectionAdapter
import com.example.worldcinematest.R
import com.example.worldcinematest.activity.EditorActivity
import java.util.ArrayList

class CollectionsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bAdd: ImageButton
    private var list = ArrayList<MyCollection>()
    private lateinit var adapter: MyCollectionAdapter
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collections, container, false)

        recyclerView = view.findViewById(R.id.rvCollections)
        bAdd = view.findViewById(R.id.buttonAdd)

        database = AppDatabase.getInstance(requireContext())
        adapter = MyCollectionAdapter(list)
        adapter.setDialog(object : MyCollectionAdapter.Dialog {

            override fun onClick(position: Int) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle(list[position].cName)
                dialog.setItems(
                    R.array.itemsoption,
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            0 -> {
                                val intent =
                                    Intent(requireContext(), EditorActivity::class.java)
                                intent.putExtra("id", list[position].cId)
                                intent.putExtra("title", "Изменить Коллецию")
                                startActivity(intent)
                            }

                            1 -> {
                                database.myCollectionDao().delete(list[position])
                                getData()
                            }

                            else -> {
                                dialog.dismiss()
                            }
                        }
                    })
                val dialogView = dialog.create()
                dialogView.show()
            }

        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )

        bAdd.setOnClickListener {
            startActivity(Intent(requireContext(), EditorActivity::class.java))
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val myCollection = adapter.getItemAtPosition(position)
                database.myCollectionDao().delete(myCollection)
                adapter.removeItem(position)
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        list.clear()
        list.addAll(database.myCollectionDao().getAll())
        adapter.notifyDataSetChanged()
    }
}