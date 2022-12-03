package style_engine

/**
 * Css-like selector which holds the different states in the path to root
 */
internal typealias StyleSelector = List<ElementState>
internal typealias StyleSelectorAsSet = Set<ElementState>

/**
 * Clones the selector and provides modifications
 */
internal fun StyleSelector.cloneAnd(action: MutableList<ElementState>.() -> Unit): StyleSelector {
  val copy = this.toMutableList()
  copy.apply { action.invoke(this) }
  return copy
}

/**
 * Learn more about specificity here: https://developer.mozilla.org/en-US/docs/Web/CSS/Specificity
 */
internal fun StyleSelector.hasHigherOrEqualSpecificityThan(otherSelector: StyleSelector?): Boolean {
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
