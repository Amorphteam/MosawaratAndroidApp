package com.aj.hajarialmustafa.adapter

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aj.hajarialmustafa.ItemDetailFragment
import com.aj.hajarialmustafa.R
import com.aj.hajarialmustafa.databinding.ItemListContentBinding
import com.aj.hajarialmustafa.placeholder.PlaceholderContent
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(
    private var values: ArrayList<PlaceholderContent.PlaceholderItem>,
    private val itemDetailFragmentContainer: View?
) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    fun setFilter(newList: ArrayList<PlaceholderContent.PlaceholderItem>?) {
        values = ArrayList()
        values = newList!!
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = "اسم المخطوطة من مخطوطات المصطفى " + item.id
        holder.contentView.text = item.content

        with(holder.itemView) {
            tag = item
            setOnClickListener { itemView ->
                val item = itemView.tag as PlaceholderContent.PlaceholderItem
                val bundle = Bundle()
                bundle.putString(
                    ItemDetailFragment.ARG_ITEM_ID,
                    item.id
                )
                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.fragment_item_detail, bundle)
                } else {
                    itemView.findNavController().navigate(R.id.show_item_detail, bundle)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /**
                 * Context click listener to handle Right click events
                 * from mice and trackpad input to provide a more native
                 * experience on larger screen devices
                 */
                setOnContextClickListener { v ->
                    val item = v.tag as PlaceholderContent.PlaceholderItem
                    Toast.makeText(
                        v.context,
                        "Context click of item " + item.id,
                        Toast.LENGTH_LONG
                    ).show()
                    true
                }
            }

            setOnLongClickListener { v ->
                // Setting the item id as the clip data so that the drop target is able to
                // identify the id of the content
                val clipItem = ClipData.Item(item.id)
                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    clipItem
                )

                if (Build.VERSION.SDK_INT >= 24) {
                    v.startDragAndDrop(
                        dragData,
                        View.DragShadowBuilder(v),
                        null,
                        0
                    )
                } else {
                    v.startDrag(
                        dragData,
                        View.DragShadowBuilder(v),
                        null,
                        0
                    )
                }
            }
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.idText
        val contentView: TextView = binding.content
    }

}