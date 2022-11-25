package vighnesh.css_engine

class StylesScope internal constructor(
  // This is a list because it will help with finding "Specificity"
  private val currentSelector: CssSelector = emptyList(),

  // Holds the final styles
  private val outputStyles: MutableMap<Set<ElementState>, Styles> = mutableMapOf(),
) {
  var backgroundColor: String? = null
    get() = field ?: outputStyles[currentSelector.toSet()]?.backgroundColor?.value
    set(value) = getDerivedSelectors().forEach { selector ->
      field = value
      val stylesInStore = outputStyles[selector]
      val previousUpdateSelector = stylesInStore?.backgroundColor?.cssSelector

      // Current selector is less specific than the selector that updated this style, previously.
      // So, ignore this update
      if (currentSelector.hasLowerSpecificityThan(previousUpdateSelector)) {
        return@forEach
      }

      // Override the existing styles in the store, if they exist, else, just set it to newStyles
      overrideStyles(
        selector = selector,
        newStyles = Styles(
          backgroundColor = StyleWithSelector(value, cssSelector = currentSelector),
        ),
      );
    }

  var contentColor: String? = null
    get() = field ?: outputStyles[currentSelector.toSet()]?.contentColor?.value
    set(value) = getDerivedSelectors().forEach { selector ->
      field = value
      val stylesInStore = outputStyles[selector]
      val previousUpdateSelector = stylesInStore?.contentColor?.cssSelector

      // Current selector is less specific than the selector that updated this style, previously.
      // So, ignore this update
      if (currentSelector.hasLowerSpecificityThan(previousUpdateSelector)) {
        return@forEach
      }

      // Override the existing styles in the store, if they exist, else, just set it to newStyles
      overrideStyles(
        selector = selector,
        newStyles = Styles(
          contentColor = StyleWithSelector(value, cssSelector = currentSelector),
        ),
      );
    }

  /**
   * Applied when the element is focused
   */
  fun onFocused(styles: StylesScope.() -> Unit) = newScope(ElementState.Focused, styles)

  /**
   * Applied when the element is selected
   */
  fun onSelected(styles: StylesScope.() -> Unit) = newScope(ElementState.Selected, styles)

  /**
   * Applied when the element is pressed
   */
  fun onPressed(styles: StylesScope.() -> Unit) = newScope(ElementState.Pressed, styles)

  /**
   * Applied when the element is disabled
   */
  fun onDisabled(styles: StylesScope.() -> Unit) = newScope(ElementState.Disabled, styles)

  /**
   * Derives newSelector from currentSelector and creates a new scope for the child styles
   */
  private fun newScope(elementState: ElementState, styles: StylesScope.() -> Unit) {
    val newSelector = currentSelector.cloneAnd { add(elementState) }
    val scope = StylesScope(currentSelector = newSelector, outputStyles = outputStyles)
    scope.apply(styles)
  }

  /**
   * Returns selectors which inherit the currentSelector
   */
  private fun getDerivedSelectors(): List<Set<ElementState>> =
    ElementState.possibleCombinations.filter {
      it.containsAll(currentSelector.toSet())
    }

  /**
   * Overrides the styles for the given selector in output styles
   */
  private fun overrideStyles(selector: Set<ElementState>, newStyles: Styles) {
    val existingStyles = outputStyles[selector]
    if (existingStyles != null) {
      outputStyles[selector] = existingStyles.overriddenWith(newStyles)
    } else {
      outputStyles[selector] = newStyles
    }
  }
}
