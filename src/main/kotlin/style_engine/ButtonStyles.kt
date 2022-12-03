package style_engine

import vighnesh.Composable

class ContainedButtonStylesScope(
    private val currentSelector: StyleSelector = emptyList(),
    private val outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector>,
) : StylesStates<ContainedButtonStylesScope>(
    currentSelector, outputStylesWithSelector
) {
    private val stylesScope = StylesScope(currentSelector, outputStylesWithSelector)

    var backgroundColor
        get() = stylesScope.backgroundColor
        set(value) { stylesScope.backgroundColor = value }

    var contentColor
        get() = stylesScope.contentColor
        set(value) { stylesScope.contentColor = value }

    override fun createScopeInstance(
        newSelector: StyleSelector,
        outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector>
    ) = ContainedButtonStylesScope(
        currentSelector = newSelector,
        outputStylesWithSelector = outputStylesWithSelector
    )
}

class ContainedButtonStyles(
    mergeFrom: ContainedButtonStyles? = null,
    stylesDefinition: @Composable (ContainedButtonStylesScope.() -> Unit),
): Styles<ContainedButtonStyles, ContainedButtonStylesScope>(
    mergeFrom = mergeFrom,
    stylesDefinition = stylesDefinition,
) {
    override fun createStylesInstance(
        outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector>
    ) = ContainedButtonStylesScope(outputStylesWithSelector = outputStylesWithSelector)
}

class OutlinedButtonStylesScope(
    currentSelector: StyleSelector = emptyList(),
    outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector>,
) : StylesStates<OutlinedButtonStylesScope>(
    currentSelector, outputStylesWithSelector
) {
    private val stylesScope = StylesScope(currentSelector, outputStylesWithSelector)

    var outline
        get() = stylesScope.outline
        set(value) { stylesScope.outline = value }

    var contentColor
        get() = stylesScope.contentColor
        set(value) { stylesScope.contentColor = value }

    override fun createScopeInstance(
        newSelector: StyleSelector,
        outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector>
    ) = OutlinedButtonStylesScope(
        currentSelector = newSelector,
        outputStylesWithSelector = outputStylesWithSelector
    )
}

class OutlinedButtonStyles(
    mergeFrom: OutlinedButtonStyles? = null,
    stylesDefinition: @Composable (OutlinedButtonStylesScope.() -> Unit),
): Styles<OutlinedButtonStyles, OutlinedButtonStylesScope>(
    mergeFrom = mergeFrom,
    stylesDefinition = stylesDefinition,
) {
    override fun createStylesInstance(
        outputStylesWithSelector: MutableMap<StyleSelectorAsSet, StylesWithSelector>
    ) = OutlinedButtonStylesScope(outputStylesWithSelector = outputStylesWithSelector)
}

