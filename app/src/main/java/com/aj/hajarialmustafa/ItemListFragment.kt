package com.aj.hajarialmustafa

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aj.hajarialmustafa.adapter.MyAdapter
import com.aj.hajarialmustafa.databinding.FragmentItemListBinding
import com.aj.hajarialmustafa.model.Post
import com.aj.hajarialmustafa.placeholder.DataListiner
import com.aj.hajarialmustafa.placeholder.MainItems.Companion.chechForUpdate
import com.aj.hajarialmustafa.placeholder.MainItems.Companion.getOfflineMakhtotItemAsAList
import java.util.*


/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

class ItemListFragment : Fragment(), DataListiner {
    var items = ArrayList<Post>()
    var adapter: MyAdapter? = null
    /**
     * Method to intercept global key events in the
     * item list fragment to trigger keyboard shortcuts
     * Currently provides a toast when Ctrl + Z and Ctrl + F
     * are triggered
     */
    private val unhandledKeyEventListenerCompat =
        ViewCompat.OnUnhandledKeyEventListenerCompat { v, event ->
            if (event.keyCode == KeyEvent.KEYCODE_Z && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Undo (Ctrl + Z) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            } else if (event.keyCode == KeyEvent.KEYCODE_F && event.isCtrlPressed) {
                Toast.makeText(
                    v.context,
                    "Find (Ctrl + F) shortcut triggered",
                    Toast.LENGTH_LONG
                ).show()
                true
            }
            false
        }

    private var _binding: FragmentItemListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.addOnUnhandledKeyEventListener(view, unhandledKeyEventListenerCompat)

        val recyclerView: RecyclerView = binding.itemList
        val searchView: SearchView = binding.searchView
        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)
        val context = requireContext()
        chechForUpdate(context, this)

        items = getOfflineMakhtotItemAsAList(context) as ArrayList<Post>

        adapter =  MyAdapter(
            items, itemDetailFragmentContainer
        )
        setupRecyclerView(recyclerView, adapter!!)
        setupSearchView(searchView, adapter!!)
    }



    private fun setupSearchView(searchView: SearchView, adapter:MyAdapter) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filterWord = newText.lowercase(Locale.getDefault())
                val newList: ArrayList<Post> = ArrayList()
                for (item in items) {
                    if (item.post_name.contains(filterWord) || item.details.author_name.contains(filterWord) || item.details.source.contains(filterWord)) {
                        newList.add(item)
                    }
                }
                adapter.setFilter(newList)
                return true
            }
        })

    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        adapter: MyAdapter
    ) {

        recyclerView.adapter = adapter
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetNewList() {
        items = getOfflineMakhtotItemAsAList(requireContext()) as ArrayList<Post>
        adapter!!.updateAdapter(items)
    }
}