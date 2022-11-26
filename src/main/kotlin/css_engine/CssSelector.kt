package vighnesh.css_engine

/**
 * Css-like selector which holds the different states in the path to root
 */
internal typealias CssSelector = List<ElementState>

/**
 * Clones the selector and provides modifications
 */
internal fun CssSelector.cloneAnd(action: MutableList<ElementState>.() -> Unit): CssSelector {
  val copy = this.toMutableList()
  copy.apply { action.invoke(this) }
  return copy
}

/**
 * Learn more about specificity here: https://developer.mozilla.org/en-US/docs/Web/CSS/Specificity
 */
internal fun CssSelector.hasHigherOrEqualSpecificityThan(otherSelector: CssSelector?): Boolean {
  val otherSelectorList = (otherSelector ?: emptyList())

  val thisUniqueStateCount = this.toSet().size
  val otherUniqueStateCount = otherSelectorList.toSet().size

  if (thisUniqueStateCount == otherUniqueStateCount) {
    // If the UniqueStateSet count is equal, then the selector with higher number of duplicate
    // states, is more specific (has more weightage)
    return this.size >= otherSelectorList.size
  }

  // If the UniqueStateSet count is not equal, then the selector with more unique states has higher
  // specificity (has more weightage)
  return thisUniqueStateCount >= otherUniqueStateCount
}
