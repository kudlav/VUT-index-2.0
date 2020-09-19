package prvnimilion.vutindex.repository.repos

import prvnimilion.vutindex.webscraper.scrapers.HealthDeclareScraper

class HealthDeclareRepository(private val healthDeclareScraper: HealthDeclareScraper) {

    fun getDeclarationState(): String {
        return healthDeclareScraper.getDeclarationState() ?: "error" // TODO unable to translate
    }

    fun signDeclaration(): String {
        return healthDeclareScraper.signDeclaration() ?: "error" // TODO unable to translate
    }

}
