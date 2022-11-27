package vighnesh.css_engine

class StylesScope internal constructor(
  // This is a list because it will help with calculating "Specificity"
  private val currentSelector: CssSelector = emptyList(),

  // Holds the final styles
  private val outputStyles: MutableMap<CssSelectorAsSet, Styles> = mutableMapOf(),
) {
  var backgroundColor: String? = null
    get() = field ?: getPresetStyle { backgroundColor }
    set(value) {
      field = value
      updateStyles(Styles(backgroundColor = withCurrentSelector(value))) { backgroundColor }
    }

  var contentColor: String? = null
    get() = field ?: getPresetStyle { contentColor }
    set(value) {
      field = value
      updateStyles(Styles(contentColor = withCurrentSelector(value))) { contentColor }
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
          canCurrentSelectorOverride(
            previousSelector = childSelector,
            previousStyleWithSelector = previousStyleWithSelector
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
   * Checks if currentSelector can override the oldSelector for a Style
   */
  private fun <T> canCurrentSelectorOverride(
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
   * Returns the Style's value for the styleWithSelector, from the store
   */
  private fun <T>getPresetStyle(styleWithSelector: (Styles).() -> StyleWithSelector<T>?): T? {
    val storeStyles = outputStyles[currentSelector.toSet()]
    return storeStyles?.let(styleWithSelector)?.value
  }

  /**
   * Wraps the value with currentSelector
   */
  private fun <T> withCurrentSelector(value: T): StyleWithSelector<T> =
    StyleWithSelector(value = value, cssSelector = currentSelector)
}
