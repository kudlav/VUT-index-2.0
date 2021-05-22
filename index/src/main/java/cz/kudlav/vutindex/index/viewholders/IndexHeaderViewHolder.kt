package cz.kudlav.vutindex.index.viewholders

import android.view.View
import android.widget.TextView
import cz.kudlav.vutindex.index.R
import cz.kudlav.vutindex.index.models.IndexHeaderModel
import cz.kudlav.vutindex.ui_common.viewholder.BaseViewHolder

class IndexHeaderViewHolder(itemView: View) : BaseViewHolder<IndexHeaderModel>(itemView) {

    override fun bindView(item: IndexHeaderModel) {
        itemView.findViewById<TextView>(R.id.header).text = item.header
    }
}