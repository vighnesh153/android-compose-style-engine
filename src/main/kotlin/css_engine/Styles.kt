package vighnesh.css_engine

/**
 * Wrapper for style with selector that updated the style. The selector will
 * be used to determine specificity which will be used to check if the value can be overridden.
 */
class StyleWithSelector<T>(
  val value: T,
  val cssSelector: CssSelector = emptyList(),
)

/**
 * This will be used by the Surface and other components
 */
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
class Styles(
  internal var backgroundColor: StyleWithSelector<String?>? = null,
  internal var contentColor: StyleWithSelector<String?>? = null,
) {
  fun overriddenWith(overrideStyles: Styles): Styles = Styles(
    backgroundColor = overrideStyles.backgroundColor ?: backgroundColor,
    contentColor = overrideStyles.contentColor ?: contentColor,
  )

  fun toPureStyle(): Style = Style(
    backgroundColor = backgroundColor?.value,
    contentColor = contentColor?.value,
  )
}
