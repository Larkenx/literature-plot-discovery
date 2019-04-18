package larkenx.com

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


fun main() {
    val articlesAndPronouns = listOf("he", "she", "it", "they", "them", "the", "a", "an", "i", "thou", "thee", "thy")
    val prepositions = listOf("of", "with", "at", "from", "into", "during", "including", "until", "against", "among", "throughout", "despite", "towards", "upon", "concerning", "to", "in", "for", "on", "by", "about", "like", "through", "over", "before", "between", "after", "since", "without", "under", "within", "along", "following", "across", "behind", "beyond", "plus", "except", "but", "up", "out", "around", "down", "off", "above", "near")
    // Load our thesaurus into memory
    val typeToken = object : TypeToken<HashMap<String, List<String>>>() {}.type
    val thesaurus: HashMap<String, List<String>> =
        Gson().fromJson(File(System.getProperty("user.dir") + "/thesaurus.json").readText(), typeToken)

    fun getSynonyms(word: String): List<String> {
        return thesaurus.getOrDefault(word, emptyList())
    }

    fun getWords(text: String): List<String> {
        return text.split(" ").filter { word -> !articlesAndPronouns.contains(word) && !prepositions.contains(word) }
    }

    class SynonymComparator : Comparator<Pair<String, Int>> {
        override fun compare(o1: Pair<String, Int>, o2: Pair<String, Int>): Int {
            return o1.second.compareTo(o2.second)
        }
    }

    fun sortSynonymsByHighestVolume(map: HashMap<String, Int>): List<Pair<String, Int>> {
        var result = arrayListOf<Pair<String, Int>>()
        val sorter = SynonymComparator()
        for ((word, count) in map) {
            result.add(Pair(word, count))
        }
        return result.sortedWith(sorter)
    }

    fun diagramSentence(text: String) {
        val articles = listOf("a", "an", "thy", "the").joinToString("|")
        val noun = Regex("""(?:$articles).*([A-Z].*)\s""")
        println("Found the following nouns in \"$text\"")
        println("""[${noun.findAll(text).map { result -> result.groupValues.first() }.joinToString(",")}]""")
    }

    val genesis = File(System.getProperty("user.dir") + "/kvj-genesis.txt").readLines()//.map { line -> line.toLowerCase() }
    val genesisOne = genesis.subList(1, 31)
//    val allOfGenesis = bible.subList(0, 4678) // Genesis
    val chapterAndVersePattern = Regex("""\d+:\d+""")
    val synonyms = HashMap<String, Int>()
    for (line in genesisOne) {
        diagramSentence(line)
        val chapterAndVerse = chapterAndVersePattern.find(line)
        for (word in getWords(line)) {
            val matchedSynonyms = getSynonyms(word)
            for (synonym in matchedSynonyms) {
                synonyms[synonym] = synonyms.getOrDefault(synonym, 0) + 1
            }
//            if (matchedSynonyms.isNotEmpty()) {
//                println("$word -> [${matchedSynonyms.joinToString(",")}]")
//            }
        }
    }

//    println(sortSynonymsByHighestVolume(synonyms).takeLast(20))


}
