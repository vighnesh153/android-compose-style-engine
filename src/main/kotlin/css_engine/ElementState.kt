package vighnesh.css_engine

enum class ElementState {
  Focused,
  Pressed,
  Selected,
  Disabled;

  companion object {
    private val allStates = values().toList()
    internal val possibleCombinations = possibleCombinations()
    private val allDerivedSelectors: Map<CssSelectorAsSet, List<CssSelectorAsSet>> =
      generateDerivedSelectors()

    /**
     * Returns all the derived selectors from the provided selector. Using "!!" because we
     * generate all the selectors initially.
     */
    internal fun getDerivedSelectors(selector: CssSelectorAsSet): List<CssSelectorAsSet> =
      allDerivedSelectors[selector]!!

    /**
     * Generates all possible combinations of selectors
     * "F", "FP", "FS", "FD", "PS", "PD", "SD", "FPS", "FPD", "FSD", "PSD", "FPSD"
     */
    private fun possibleCombinations(
      currentIndex: Int = allStates.size - 1,
    ): List<CssSelectorAsSet> {
      if (currentIndex < 0) return listOf(emptySet())

      val previousCombinations = possibleCombinations(currentIndex - 1)
      val combinationsWithCurrent = previousCombinations.map {
        val copy = it.toMutableSet()
        copy.add(allStates[currentIndex])
        return@map copy.toSet()
      }
      val combinationsWithoutCurrent = previousCombinations.toList()

      val allCombinations = mutableListOf<CssSelectorAsSet>()
      allCombinations.addAll(combinationsWithCurrent)
      allCombinations.addAll(combinationsWithoutCurrent)
      return allCombinations
    }

    /**
     * Generates derived selectors for all selectors
     *
     * Examples:
     * - F -> FP, FS, FD, FPD, FPS, FSD, FPSD
     * - SD -> SD, FSD, PSD, FPSD
     */
    private fun generateDerivedSelectors(): Map<CssSelectorAsSet, List<CssSelectorAsSet>> {
      val allDerivedSelectors = mutableMapOf<CssSelectorAsSet, List<CssSelectorAsSet>>()
      possibleCombinations.forEach { selector ->
        val derivedSelectors = possibleCombinations.filter { it.containsAll(selector) }
        allDerivedSelectors[selector] = derivedSelectors
      }
      return allDerivedSelectors
    }
  }
}
