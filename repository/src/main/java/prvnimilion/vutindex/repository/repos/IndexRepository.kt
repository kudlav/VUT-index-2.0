package prvnimilion.vutindex.repository.repos

import prvnimilion.vutindex.ui_common.models.Index
import prvnimilion.vutindex.webscraper.scrapers.IndexScraper

class IndexRepository(private val indexScraper: IndexScraper) {

    fun getIndex(): Index {
        return indexScraper.getIndex()
    }

    fun compareIndexes() {

    }

    private fun saveIndexToDb() {

    }


}

