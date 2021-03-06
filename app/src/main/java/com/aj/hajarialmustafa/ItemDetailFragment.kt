package com.aj.hajarialmustafa

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.aj.hajarialmustafa.databinding.FragmentItemDetailBinding
import com.aj.hajarialmustafa.model.Post
import com.aj.hajarialmustafa.placeholder.MainItems.Companion.getOfflineMakhtotItemAsAList
import com.aj.hajarialmustafa.preferences.PrefManagerSync
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.Gson

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
    private var item: Post? = null

    private var imageView: ImageView? = null
    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null
    lateinit var authorName: TextView
    lateinit var tableDetail: LinearLayout
    lateinit var writerName: TextView
    lateinit var pageCount: TextView
    lateinit var sourceName: TextView
    lateinit var fontType: TextView
    lateinit var catName: TextView
    lateinit var writingDate: TextView
    lateinit var lineCount: TextView
    lateinit var firstLine: TextView
    lateinit var endLine:TextView
    lateinit var notes: TextView
    private var toolbarLayout: CollapsingToolbarLayout? = null


    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
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
                for (makhtotItem in getOfflineMakhtotItemAsAList(requireContext())!!) {
                    if (makhtotItem.post_name.equals(it.getString(ARG_ITEM_ID))) {
                        this.item = makhtotItem
                    }
                }
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root
        tableDetail = binding.includedLayout.tableDetail
        toolbarLayout = binding.toolbarLayout
        imageView = binding.shadowLogo
        imageView1 = binding.includedLayout.image1
        imageView2 = binding.includedLayout.image2
        authorName = binding.includedLayout.author
        writerName = binding.includedLayout.writer
        writingDate = binding.includedLayout.writingDate
        fontType = binding.includedLayout.fontType
        lineCount = binding.includedLayout.lineCount
        pageCount = binding.includedLayout.pageCount
        sourceName = binding.includedLayout.sourceName
        catName = binding.includedLayout.cat
        firstLine = binding.includedLayout.firstLine
        endLine = binding.includedLayout.endLine
        notes = binding.includedLayout.notes
        updateContent()
        rootView.setOnDragListener(dragListener)

        return rootView
    }

    private fun updateContent() {
        toolbarLayout?.title = item?.post_name
        toolbarLayout?.setCollapsedTitleTextAppearance(R.style.ToolbarTextAppearance)
        toolbarLayout?.setExpandedTitleTextAppearance(R.style.ToolbarTextAppearance)
        tableDetail.visibility = View.GONE
        imageView?.visibility = View.VISIBLE
        // Show the placeholder content as text in a TextView.
        item?.let {
            tableDetail.visibility = View.VISIBLE
            authorName.text = it.details.author_name
            writerName.text = it.details.writer_name
            writingDate.text = it.details.writing_date
            fontType.text = it.details.font_type
            pageCount.text = it.details.page_count
            sourceName.text = it.details.source
            catName.text = it.details.category
            lineCount.text = it.details.note6
            firstLine.text = it.details.note1
            firstLine.text = it.details.note2
            notes.text = "${it.details.note3} \n ${it.details.note4} \n ${it.details.note5}"
            imageView?.visibility = View.GONE
            loadImageFromUrl(imageView1, it.images[0].thumbnail_url)
            loadImageFromUrl(imageView2, it.images[1].thumbnail_url)

            imageView1?.setOnClickListener { _ ->
                val intent = Intent(requireContext(), ImageActivity::class.java)
                intent.putExtra("IMAGE", it.images[0].image_url)
                startActivity(intent)
            }

            imageView2?.setOnClickListener { _ ->
                val intent = Intent(requireContext(), ImageActivity::class.java)
                intent.putExtra("IMAGE", it.images[1].image_url)
                startActivity(intent)
            }
        }
    }

    private fun loadImageFromUrl(imageView: ImageView?, thumbnailUrl: String) {
        Glide.with(this)
            .load(thumbnailUrl) // image url
//                .placeholder(R.drawable.placeholder) // any placeholder to load at start
//                .error(R.drawable.imagenotfound)  // any image in case of error
            .transform(CenterCrop(),RoundedCorners(25))
            .override(200, 200) // resizing
            .into(imageView!!);  // imageview object
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