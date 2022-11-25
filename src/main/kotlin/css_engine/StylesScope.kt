package vighnesh.css_engine

class StylesScope internal constructor(
  // This is a list because it will help with finding "Specificity"
  private val currentSelector: CssSelector = emptyList(),

  // Holds the final styles
  private val outputStyles: MutableMap<Set<ElementState>, Styles> = mutableMapOf(),
) {
  var backgroundColor: String? = null
    get() = field ?: getStylesFromStore().backgroundColor?.value
    set(value) = childrenStatesSet().forEach { statesSet ->
      field = value
      if (currentSelector.hasLowerSpecificityThan(
          outputStyles[statesSet]?.backgroundColor?.cssSelector)
      ) return@forEach
      outputStyles[statesSet] = getStylesFromStore(statesSet).overriddenWith(
        Styles(backgroundColor = StyleWithSelector(value, cssSelector = currentSelector))
      )
    }

  var contentColor: String? = null
    get() = field ?: getStylesFromStore().contentColor?.value
    set(value) = childrenStatesSet().forEach { statesSet ->
      field = value
      if (currentSelector.hasLowerSpecificityThan(outputStyles[statesSet]?.contentColor?.cssSelector)) return@forEach
      outputStyles[statesSet] = getStylesFromStore(statesSet).overriddenWith(
        Styles(contentColor = StyleWithSelector(value, cssSelector = currentSelector))
      )
    }

  fun onFocused(styles: StylesScope.() -> Unit) = scopedStyles(ElementState.Focused, styles)
  fun onSelected(styles: StylesScope.() -> Unit) = scopedStyles(ElementState.Selected, styles)
  fun onPressed(styles: StylesScope.() -> Unit) = scopedStyles(ElementState.Pressed, styles)
  fun onDisabled(styles: StylesScope.() -> Unit) = scopedStyles(ElementState.Disabled, styles)

  private fun scopedStyles(elementState: ElementState, styles: StylesScope.() -> Unit) {
    val newSelector = currentSelector.cloneAnd { add(elementState) }
    val scope = StylesScope(currentSelector = newSelector, outputStyles = outputStyles)
    scope.apply(styles)
  }

  private fun childrenStatesSet(): List<Set<ElementState>> =
    ElementState.possibleCombinations.filter {
      it.containsAll(currentSelector.toSet())
    }

  private fun getStylesFromStore(statesSet: Set<ElementState> = currentSelector.toSet()): Styles =
    outputStyles[statesSet] ?: Styles()
}
