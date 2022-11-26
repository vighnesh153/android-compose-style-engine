package vighnesh.css_engine

class StylesScope internal constructor(
  // This is a list because it will help with finding "Specificity"
  private val currentSelector: CssSelector = emptyList(),

  // Holds the final styles
  private val outputStyles: MutableMap<CssSelectorAsSet, Styles> = mutableMapOf(),
) {
  // For the getter, should we do this?
  // field ?: outputStyles[currentSelector.toSet()]?.backgroundColor?.value
  var backgroundColor: String? = null
    set(value) {
      field = value
      updateStyles(Styles(backgroundColor = withThisSelector(value))) { backgroundColor }
    }

  var contentColor: String? = null
    set(value) {
      field = value
      updateStyles(Styles(contentColor = withThisSelector(value))) { contentColor }
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
  private fun newScope(newElementState: ElementState, styles: StylesScope.() -> Unit) {
    val newSelector = currentSelector.cloneAnd { add(newElementState) }
    StylesScope(currentSelector = newSelector, outputStyles = outputStyles)
      .apply(styles)
  }

  /**
   * Updates the styles in all derived selectors, if currentSelector is more "specific"
   */
  private fun <T> updateStyles(
    newStyles: Styles,
    previousStyleWithSelector: Styles.() -> StyleWithSelector<T>?,
  ) {
    ElementState
      .getDerivedSelectors(currentSelector.toSet())
      .forEach { childSelector ->
      if (
        canOverrideStyle(
          previousSelector = childSelector,
          previousStyleWithSelector = previousStyleWithSelector,
        )
      ) {
        overrideStylesFor(
          selector = childSelector,
          newStyles = newStyles,
        );
      }
    }
  }

  /**
   * Checks if new selector can override the old selector for a Style
   */
  private fun <T> canOverrideStyle(
    previousSelector: CssSelectorAsSet,
    previousStyleWithSelector: (Styles).() -> StyleWithSelector<T>?,
  ): Boolean {
    val stylesInStore = outputStyles[previousSelector]
    val previousCssSelector = (stylesInStore ?: Styles())
      .let(previousStyleWithSelector)?.cssSelector

    return currentSelector.hasHigherOrEqualSpecificityThan(previousCssSelector)
  }

  /**
   * Overrides the styles for the given selector in output styles
   */
  private fun overrideStylesFor(selector: CssSelectorAsSet, newStyles: Styles) {
    val existingStyles = outputStyles[selector]
    if (existingStyles == null) {
      outputStyles[selector] = newStyles
    } else {
      outputStyles[selector] = existingStyles.overriddenWith(newStyles)
    }
  }

  /**
   * Wraps the value with currentSelector
   */
  private fun <T> withThisSelector(value: T): StyleWithSelector<T> =
    StyleWithSelector(value = value, cssSelector = currentSelector)
}
