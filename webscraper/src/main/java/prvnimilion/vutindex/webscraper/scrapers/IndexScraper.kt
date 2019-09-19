package prvnimilion.vutindex.webscraper.scrapers

import android.annotation.SuppressLint
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import prvnimilion.vutindex.ui_common.models.Index
import prvnimilion.vutindex.ui_common.models.Semester
import prvnimilion.vutindex.ui_common.models.Subject
import prvnimilion.vutindex.webscraper.util.INDEX_URL
import prvnimilion.vutindex.webscraper.util.VutCookieStore
import timber.log.Timber

class IndexScraper(private val vutCookieStore: VutCookieStore) {

    @SuppressLint("BinaryOperationInTimber")
    fun getIndex(): Index {
        Timber.tag("VUTdebug").d("Loading index ...")
        val cookies = vutCookieStore.loadCookies()
        val indexRequest = Jsoup.connect(INDEX_URL)
            .followRedirects(true)
            .method(Connection.Method.GET)
            .timeout(10 * 1000)
            .cookies(cookies)
            .execute()

        cookies.putAll(indexRequest.cookies())
        vutCookieStore.saveCookies(cookies)

        val indexDoc = indexRequest.parse()

        val tableHeaders =
            indexDoc.getElementsByClass("main-content").first().getElementsByTag("h3").eachText()

        val semesterTables = Elements()
        indexDoc.getElementsByClass("table table-bordered table-middle").map {
            semesterTables.add(it.getElementsByTag("tbody").first())
        }

        var id = 0
        var tableIndex = 0
        val semesters = mutableListOf<Semester>()
        semesterTables.forEach { semesterTable ->
            val tableRows = semesterTable.allElements

            val subjects = mutableListOf<Subject>()
            tableRows.forEach { subject ->
                if (subject.className().contains("pov")) {
                    val parsedData = subject.getElementsByClass("center").eachText()

                    val fullName = subject.select("[title=Detail zapsaného předmětu]").text();
                    val shortName = parsedData[0]
                    val type = parsedData[2]
                    val credits = parsedData[3]

                    val vsp = parsedData[4]
                    val completion = parsedData[5]

                    var creditGiven = false
                    if (parsedData[6].contains("ano"))
                        creditGiven = true

                    val points = parsedData[7]

                    var grade = ""
                    if (parsedData[8].isNotEmpty())
                        grade = parsedData[8].substring(0, 1)

                    var termTime = "1"
                    if (parsedData[9].isNotEmpty())
                        termTime = parsedData[9]

                    val passed = parsedData[10].contains("ano")

                    Timber.tag("VUTdebug")
                        .d(
                            "zkratka: $shortName," +
                                    " typ: $type," +
                                    " kredity: $credits," +
                                    " vsp: $vsp," +
                                    " dokončení: $completion," +
                                    " zápočet: $creditGiven," +
                                    " body: $points," +
                                    " známka: $grade," +
                                    " termín: $termTime," +
                                    " dokončeno: $passed"
                        )
                    subjects.add(
                        Subject(
                            id++,
                            fullName,
                            shortName,
                            type,
                            credits,
                            completion,
                            creditGiven,
                            points,
                            grade,
                            termTime,
                            passed,
                            vsp
                        )
                    )
                }
            }
            semesters.add(Semester(id++, tableHeaders[tableIndex++], subjects))
        }
        return Index(semesters)
    }

}