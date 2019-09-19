package prvnimilion.vutindex.indexf.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import prvnimilion.vutindex.indexf.R
import prvnimilion.vutindex.indexf.enums.IndexModelType
import prvnimilion.vutindex.indexf.models.IndexFeedModel
import prvnimilion.vutindex.indexf.models.IndexHeaderModel
import prvnimilion.vutindex.indexf.models.IndexSubjectModel
import prvnimilion.vutindex.indexf.viewholders.IndexHeaderViewHolder
import prvnimilion.vutindex.indexf.viewholders.IndexSubjectViewHolder

class IndexAdapter(private val context: Context, private val dataSet: List<IndexFeedModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position]) {
            is IndexSubjectModel -> IndexModelType.SUBJECT.position
            is IndexHeaderModel -> IndexModelType.HEADER.position
            else -> IndexModelType.SUBJECT.position
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            IndexModelType.SUBJECT.position -> IndexSubjectViewHolder(
                inflater.inflate(
                    R.layout.index_subject_card,
                    parent,
                    false
                )
            )
            IndexModelType.HEADER.position -> IndexHeaderViewHolder(
                inflater.inflate(
                    R.layout.index_header_card,
                    parent,
                    false
                )
            )

            else -> IndexSubjectViewHolder(
                inflater.inflate(
                    R.layout.index_subject_card,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            IndexModelType.SUBJECT.position -> (holder as IndexSubjectViewHolder).bindView(dataSet[position] as IndexSubjectModel)
            IndexModelType.HEADER.position -> (holder as IndexHeaderViewHolder).bindView(dataSet[position] as IndexHeaderModel)

            else -> return
        }
    }

}