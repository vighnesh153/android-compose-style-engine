package style_engine

open class StylesScope internal constructor(
    // This is a list because it will help with calculating "Specificity"
    private val currentSelector: StyleSelector = emptyList(),

    // Holds the final styles
    private val outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector> = mutableMapOf(),
) {
    var backgroundColor: String?
        get() = getPresetStyle { backgroundColor }
        set(value) = updateStyles(
            StylesWithSelector(backgroundColor = withCurrentSelector(value))
        ) { backgroundColor }

    var contentColor: String?
        get() = getPresetStyle { contentColor }
        set(value) = updateStyles(StylesWithSelector(contentColor = withCurrentSelector(value))) {
            contentColor
        }

    var outline: String?
        get() = getPresetStyle { outline }
        set(value) = updateStyles(StylesWithSelector(contentColor = withCurrentSelector(value))) {
            outline
        }

    /**
     * Updates the styles in all derived selectors, if currentSelector is more "specific"
     */
    private fun <T> updateStyles(
        newStylesWithSelector: StylesWithSelector,
        previousStyleWithSelector: StylesWithSelector.() -> StyleWithSelector<T>?,
    ) {
        ElementState
            .getDerivedSelectors(currentSelector.toSet())
            .filter {
                canCurrentSelectorOverride(
                    previousSelectorAsSet = it,
                    previousStyleWithSelector = previousStyleWithSelector
                )
            }
            .forEach { derivedSelector ->
                overrideStylesFor(
                    selector = derivedSelector,
                    newStylesWithSelector = newStylesWithSelector,
                )
            }
    }

    /**
     * Checks if currentSelector can override the oldSelector for a Style
     */
    private fun <T> canCurrentSelectorOverride(
        previousSelectorAsSet: StyleSelectorAsSet,
        previousStyleWithSelector: (StylesWithSelector).() -> StyleWithSelector<T>?,
    ): Boolean {
        val previousStyle = outputStylesWithSelector[previousSelectorAsSet]
            ?.previousStyleWithSelector()

        return currentSelector.hasHigherOrEqualSpecificityThan(previousStyle?.styleSelector)
    }

    /**
     * Overrides the styles for the given selector in output styles
     */
    private fun overrideStylesFor(
        selector: StyleSelectorAsSet,
        newStylesWithSelector: StylesWithSelector
    ) {
        val existingStyles = outputStylesWithSelector[selector]
        if (existingStyles == null) {
            outputStylesWithSelector[selector] = newStylesWithSelector
        } else {
            outputStylesWithSelector[selector] =
                existingStyles.overriddenWith(newStylesWithSelector)
        }
    }

    /**
     * Returns the Style's value for the styleWithSelector, from the store
     */
    private fun <T> getPresetStyle(
        styleWithSelector: (StylesWithSelector).() -> StyleWithSelector<T>?,
    ): T? {
        val storeStyles = outputStylesWithSelector[currentSelector.toSet()]
        return storeStyles?.styleWithSelector()?.value
    }

    /**
     * Wraps the value with currentSelector
     */
    private fun <T> withCurrentSelector(value: T): StyleWithSelector<T> =
        StyleWithSelector(value = value, styleSelector = currentSelector)
}

abstract class StylesStates<T>(
    private val currentSelector: StyleSelector = emptyList(),
    private val outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector>,
) {
    fun onFocused(styles: T.() -> Unit) = newScope(ElementState.Focused, styles)
    fun onSelected(styles: T.() -> Unit) = newScope(ElementState.Selected, styles)
    fun onPressed(styles: T.() -> Unit) = newScope(ElementState.Pressed, styles)
    fun onDisabled(styles: T.() -> Unit) = newScope(ElementState.Disabled, styles)

    internal abstract fun createScopeInstance(
        newSelector: StyleSelector,
        outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector>,
    ): T

    /**
     * Derives newSelector from currentSelector and creates a new scope for the child styles
     */
    private fun newScope(newElementState: ElementState, styles: T.() -> Unit) {
        val newSelector = currentSelector.cloneAnd { add(newElementState) }
        createScopeInstance(
            newSelector = newSelector,
            outputStylesWithSelector = outputStylesWithSelector
        ).styles()
    }
}
