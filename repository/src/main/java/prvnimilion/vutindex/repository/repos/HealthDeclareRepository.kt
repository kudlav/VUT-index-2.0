package prvnimilion.vutindex.repository.repos

import prvnimilion.vutindex.webscraper.scrapers.HealthDeclareScraper

class HealthDeclareRepository(private val healthDeclareScraper: HealthDeclareScraper) {

    fun getDeclarationState(): String? {
        return healthDeclareScraper.getDeclarationState()
    }

    fun signDeclaration(): String? {
        return healthDeclareScraper.signDeclaration()
    }

}
