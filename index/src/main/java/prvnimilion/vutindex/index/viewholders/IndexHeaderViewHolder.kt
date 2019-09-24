package prvnimilion.vutindex.index.viewholders

import android.view.View
import android.widget.TextView
import prvnimilion.vutindex.index.R
import prvnimilion.vutindex.index.models.IndexHeaderModel
import prvnimilion.vutindex.ui_common.viewholder.BaseViewHolder

class IndexHeaderViewHolder(itemView: View) : BaseViewHolder<IndexHeaderModel>(itemView) {

    override fun bindView(item: IndexHeaderModel) {
        itemView.findViewById<TextView>(R.id.header).text = item.header
    }
}