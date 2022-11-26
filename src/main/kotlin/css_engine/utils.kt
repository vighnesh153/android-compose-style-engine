package vighnesh.css_engine

/**
 * Internal utility to build stylesOutput map
 */
fun buildOutputStyles(): MutableMap<CssSelectorAsSet, Styles> {
  val outputStyles = mutableMapOf<CssSelectorAsSet, Styles>()
  ElementState.possibleCombinations.forEach {
    outputStyles[it] = Styles()
  }
  return outputStyles
}
