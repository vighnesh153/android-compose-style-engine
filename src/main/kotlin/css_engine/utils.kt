package vighnesh.css_engine

/**
 * Internal utility to build stylesOutput map
 */
fun buildOutputStyles(): MutableMap<Set<ElementState>, Styles> {
  val outputStyles = mutableMapOf<Set<ElementState>, Styles>()
  ElementState.possibleCombinations.forEach {
    outputStyles[it] = Styles()
  }
  return outputStyles
}
