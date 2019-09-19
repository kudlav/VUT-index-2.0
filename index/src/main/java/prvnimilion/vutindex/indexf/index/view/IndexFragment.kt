package prvnimilion.vutindex.indexf.index.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.index_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import prvnimilion.vutindex.indexf.R
import prvnimilion.vutindex.indexf.adapters.IndexAdapter
import prvnimilion.vutindex.indexf.index.viewmodel.IndexViewModel
import prvnimilion.vutindex.indexf.models.IndexFeedModel
import prvnimilion.vutindex.indexf.models.IndexHeaderModel
import prvnimilion.vutindex.indexf.models.IndexSubjectModel

class IndexFragment : Fragment() {

    private val indexViewModel: IndexViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.index_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        index_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
        }

        indexViewModel.getIndex()

        observeViewModels()
    }

    private fun observeViewModels() {

        indexViewModel.userIndex.observe(this, Observer {
            val dataSet = mutableListOf<IndexFeedModel>()

            it.semesters.forEach { semester ->
                dataSet.add(
                    IndexHeaderModel(
                        semester.id,
                        semester.header
                    )
                )
                semester.subjects.forEach { sub ->
                    dataSet.add(
                        IndexSubjectModel(
                            sub.id,
                            sub.fullName,
                            sub.shortName,
                            sub.type,
                            sub.credits,
                            sub.completion,
                            sub.creditGiven,
                            sub.points,
                            sub.grade,
                            sub.termTime,
                            sub.passed,
                            sub.vsp
                        )
                    )
                }
            }
            index_recycler_view.adapter = IndexAdapter(context!!, dataSet)
        })
    }

}