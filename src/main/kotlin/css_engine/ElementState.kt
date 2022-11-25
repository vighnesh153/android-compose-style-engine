package vighnesh.css_engine

enum class ElementState {
  Focused,
  Pressed,
  Selected,
  Disabled;

  companion object {
    private val allStates = values().toList()
    internal val possibleCombinations = possibleCombinations()

    private fun possibleCombinations(
      currentIndex: Int = allStates.size - 1,
    ): List<Set<ElementState>> {
      if (currentIndex < 0) return listOf(emptySet())

      val previousCombinations = possibleCombinations(currentIndex - 1)
      val combinationsWithCurrent = previousCombinations.map {
        val copy = it.toMutableSet()
        copy.add(allStates[currentIndex])
        return@map copy.toSet()
      }
      val combinationsWithoutCurrent = previousCombinations.toList()

      val allCombinations = mutableListOf<Set<ElementState>>()
      allCombinations.addAll(combinationsWithCurrent)
      allCombinations.addAll(combinationsWithoutCurrent)
      return allCombinations
    }
  }
}
