package vighnesh.experimental

interface BackgroundColorStyle {
  val styles: Styles

  var backgroundColor: String?
    get() = styles.backgroundColor
    set(value) { styles.backgroundColor = value }
}

interface ContentColorStyle {
  val styles: Styles

  var contentColor: String?
    get() = styles.contentColor
    set(value) { styles.contentColor = value }
}

interface OutlineStyle {
  val styles: Styles

  var outline: String?
    get() = styles.outline
    set(value) { styles.outline = value }
}

open class ElementStates {
  fun onFocus() {}
  fun onPressed() {}
}

class Styles internal constructor(): ElementStates() {
  var backgroundColor: String? = null
    set(value) {
      println(value)
      field = value
    }
  var contentColor: String? = null
    set(value) {
      println(value)
      field = value
    }
  var outline: String? = null
    set(value) {
      println(value)
      field = value
    }
}

class OutlineButtonStyle: ElementStates(), ContentColorStyle, OutlineStyle {
  override val styles: Styles = Styles()
}

class ContainedButtonStyle: ElementStates(), ContentColorStyle, BackgroundColorStyle {
  override val styles: Styles = Styles()
}

fun main() {
  val containedButtonStyle: ContainedButtonStyle.() -> Unit = {
    contentColor = "red"
    backgroundColor = "blue"
  }

  val outlineButtonStyle: OutlineButtonStyle.() -> Unit = {
    outline = "blue"
    contentColor = "red"
  }

  ContainedButtonStyle().containedButtonStyle()
  OutlineButtonStyle().outlineButtonStyle()
}
