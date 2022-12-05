package style_engine

import vighnesh.Composable

/**
 * Wrapper for style with selector that updated the style. The selector will
 * be used to determine specificity which will be used to check if the value can be overridden.
 */
// TODO: make internal
class StyleWithSelector<T>(
    val value: T,
    val styleSelector: StyleSelector = emptyList(),
)

/**
 * This will be used by the Surface and other components
 */
// TODO: make internal, change to Color
class Style(
    internal val backgroundColor: String?,
    internal val contentColor: String?,
) {
    override fun toString(): String {
        return "bgColor: $backgroundColor, contentColor: $contentColor"
    }
}

/**
 * This will hold style values along with the selector that updated it
 */
// TODO: make internal
class StylesWithSelector(
    internal var backgroundColor: StyleWithSelector<String?>? = null,
    internal var contentColor: StyleWithSelector<String?>? = null,
    internal var outline: StyleWithSelector<String?>? = null,
) {
    internal fun overriddenWith(
        overrideStylesWithSelector: StylesWithSelector,
    ): StylesWithSelector = StylesWithSelector(
        backgroundColor =
        overrideStylesWithSelector.backgroundColor ?: backgroundColor,
        contentColor = overrideStylesWithSelector.contentColor ?: contentColor,
    )

    internal fun toStyle(): Style = Style(
        backgroundColor = backgroundColor?.value,
        contentColor = contentColor?.value,
    )
}

abstract class Styles<T, Scope>(
    mergeFrom: Styles<T, Scope>? = null,
    private val stylesDefinition: @Composable (Scope.() -> Unit),
) {
    private var isFlattened = false
    private val selectorToStylesMap: MutableMap<StyleSelectorAsSet, StylesWithSelector> =
        mergeFrom?.selectorToStylesMap?.toMutableMap() ?: mutableMapOf()

    internal abstract fun createStylesInstance(
        outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector>
    ): Scope

    @Composable
    private fun FlattenStyles() {
        if (isFlattened) return

        ElementState.possibleCombinations.forEach {
            selectorToStylesMap[it] = selectorToStylesMap[it] ?: StylesWithSelector()
        }

        createStylesInstance(outputStylesWithSelector = selectorToStylesMap)
            .stylesDefinition()
        isFlattened = true
    }

    @Composable
    // TODO: make internal
    fun getStyle(
        isFocused: Boolean,
        isPressed: Boolean,
        isSelected: Boolean,
        isDisabled: Boolean,
    ): Style {
        FlattenStyles()

        val selector = buildSet {
            if (isFocused) add(ElementState.Focused)
            if (isPressed) add(ElementState.Pressed)
            if (isSelected) add(ElementState.Selected)
            if (isDisabled) add(ElementState.Disabled)
        }
        return selectorToStylesMap[selector]?.toStyle()!!
    }

    @Composable
    // TODO: make internal
    fun getStyle(selector: StyleSelectorAsSet): Style {
        FlattenStyles()

        return selectorToStylesMap[selector]?.toStyle()!!
    }
}
