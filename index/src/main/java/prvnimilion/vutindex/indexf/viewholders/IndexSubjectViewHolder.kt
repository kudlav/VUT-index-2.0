package prvnimilion.vutindex.indexf.viewholders

import android.view.View
import android.widget.*
import prvnimilion.vutindex.indexf.R
import prvnimilion.vutindex.indexf.models.IndexSubjectModel
import prvnimilion.vutindex.ui_common.viewholder.BaseViewHolder

class IndexSubjectViewHolder(itemView: View) : BaseViewHolder<IndexSubjectModel>(itemView) {
    var mode = MODE_LESS

    override fun bindView(item: IndexSubjectModel) {
        itemView.findViewById<TextView>(R.id.full_name).text = item.fullName
        itemView.findViewById<TextView>(R.id.short_name).text = item.shortName

        itemView.findViewById<TextView>(R.id.type).text = "Typ: ${item.type}"
        itemView.findViewById<TextView>(R.id.completion).text = "Ukončení: ${item.completion}"
        itemView.findViewById<TextView>(R.id.credits).text = "Kredity: ${item.credits}"
        itemView.findViewById<TextView>(R.id.term_time).text = "Termín: ${item.termTime}"

        val points = item.points.toIntOrNull() ?: if (item.passed) 100 else 0

        itemView.findViewById<ProgressBar>(R.id.progress_bar).progress = points
        itemView.findViewById<TextView>(R.id.points).text = "$points/100"
        itemView.findViewById<CheckBox>(R.id.passed).isChecked = item.passed

        itemView.findViewById<CheckBox>(R.id.credit_given).isChecked = item.creditGiven

        val showMoreButton = itemView.findViewById<ImageView>(R.id.show_more)
        showMoreButton.setOnClickListener {
            mode = if (mode == MODE_LESS) {
                showMoreButton.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_expand_less_black_24dp))
                itemView.findViewById<TextView>(R.id.full_name).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.type).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.completion).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.credits).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.term_time).visibility = View.VISIBLE
                itemView.findViewById<RelativeLayout>(R.id.credit_given_wrapper).visibility =
                    View.VISIBLE
                itemView.findViewById<TextView>(R.id.credit_given_text).visibility = View.VISIBLE

                MODE_MORE
            } else {
                showMoreButton.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_expand_more_black_24dp))
                itemView.findViewById<TextView>(R.id.full_name).visibility = View.GONE
                itemView.findViewById<TextView>(R.id.type).visibility = View.GONE
                itemView.findViewById<TextView>(R.id.completion).visibility = View.GONE
                itemView.findViewById<TextView>(R.id.credits).visibility = View.GONE
                itemView.findViewById<TextView>(R.id.term_time).visibility = View.GONE
                itemView.findViewById<RelativeLayout>(R.id.credit_given_wrapper).visibility =
                    View.GONE
                itemView.findViewById<TextView>(R.id.credit_given_text).visibility = View.GONE
                MODE_LESS
            }
        }
    }

    companion object {
        const val MODE_LESS = "less"
        const val MODE_MORE = "more"
    }
}