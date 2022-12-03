package style_engine

enum class ElementState {
    Focused,
    Pressed,
    Selected,
    Disabled;

    companion object {
        private val allStates = values().toList()
        internal val possibleCombinations = possibleCombinations()
        private val allDerivedSelectors: Map<StyleSelectorAsSet, List<StyleSelectorAsSet>> =
            generateDerivedSelectors()

        /**
         * Returns all the derived selectors from the provided selector. Using "!!" because we
         * generate all the selectors initially.
         */
        internal fun getDerivedSelectors(selector: StyleSelectorAsSet): List<StyleSelectorAsSet> =
            allDerivedSelectors[selector]!!

        /**
         * Generates all possible combinations of selectors
         * "F", "FP", "FS", "FD", "PS", "PD", "SD", "FPS", "FPD", "FSD", "PSD", "FPSD"
         */
        private fun possibleCombinations(
            currentIndex: Int = allStates.size - 1,
        ): List<StyleSelectorAsSet> {
            if (currentIndex < 0) return listOf(emptySet())

            val previousCombinations = possibleCombinations(currentIndex - 1)
            val combinationsWithCurrent = previousCombinations.map {
                buildSet {
                    addAll(it)
                    add(allStates[currentIndex])
                }
            }

            return buildList {
                addAll(combinationsWithCurrent)
                addAll(previousCombinations)  // without current item
            }
        }

        /**
         * Generates derived selectors for all selectors
         *
         * Examples:
         * - F -> FP, FS, FD, FPD, FPS, FSD, FPSD
         * - SD -> SD, FSD, PSD, FPSD
         */
        private fun generateDerivedSelectors(): Map<StyleSelectorAsSet, List<StyleSelectorAsSet>> =
            buildMap {
                possibleCombinations.forEach { selector ->
                    val derivedSelectors = possibleCombinations.filter { it.containsAll(selector) }
                    put(selector, derivedSelectors)
                }
            }
    }
}
