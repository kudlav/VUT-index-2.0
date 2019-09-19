package prvnimilion.vutindex.indexf.viewholders

import android.view.View
import android.widget.TextView
import prvnimilion.vutindex.indexf.R
import prvnimilion.vutindex.indexf.models.IndexHeaderModel
import prvnimilion.vutindex.ui_common.viewholder.BaseViewHolder

class IndexHeaderViewHolder(itemView: View) : BaseViewHolder<IndexHeaderModel>(itemView) {

    override fun bindView(item: IndexHeaderModel) {
        itemView.findViewById<TextView>(R.id.header).text = item.header
    }
}