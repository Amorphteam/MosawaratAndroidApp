package com.aj.hajarialmustafa

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.aj.hajarialmustafa.placeholder.PlaceholderContent
import com.aj.hajarialmustafa.databinding.FragmentItemDetailBinding

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The placeholder content this fragment is presenting.
     */
    private var item: PlaceholderContent.PlaceholderItem? = null

    lateinit var itemDetailTextView: TextView
    private var imageView: ImageView? = null
    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null

    private var toolbarLayout: CollapsingToolbarLayout? = null


    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            item = PlaceholderContent.ITEM_MAP[dragData]
            updateContent()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = PlaceholderContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        toolbarLayout = binding.toolbarLayout
        itemDetailTextView = binding.includedLayout.itemDetail
        imageView = binding.shadowLogo
        imageView1 = binding.includedLayout.image1
        imageView2 = binding.includedLayout.image2

        updateContent()
        rootView.setOnDragListener(dragListener)

        return rootView
    }

    private fun updateContent() {
        toolbarLayout?.title = item?.content
        toolbarLayout?.setCollapsedTitleTextAppearance(R.style.ToolbarTextAppearance)
        toolbarLayout?.setExpandedTitleTextAppearance(R.style.ToolbarTextAppearance)

        // Show the placeholder content as text in a TextView.
        item?.let {
            itemDetailTextView.text = it.details
            imageView?.visibility = View.GONE
            imageView1?.setOnClickListener {
                startActivity(Intent(requireContext(), ImageActivity::class.java))
            }

            imageView2?.setOnClickListener {
                startActivity(Intent(requireContext(), ImageActivity::class.java))
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}