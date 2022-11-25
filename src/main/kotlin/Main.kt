package vighnesh

import vighnesh.css_engine.ElementState
import vighnesh.css_engine.StylesScope
import vighnesh.css_engine.buildOutputStyles

fun main() {
  val styles: StylesScope.() -> Unit = {
    onFocused {
      onPressed {
        backgroundColor = "F.P"
      }
    }
    onPressed {
      backgroundColor = "P"
      contentColor = "P"
    }
    onDisabled {

    }
    onFocused {
      onPressed {
        onSelected {
          backgroundColor = "F.P.S"
        }
      }
    }
  }

  val outputStyles = buildOutputStyles()
  val scope = StylesScope(outputStyles = outputStyles)
  scope.apply(styles)

  ElementState.possibleCombinations
    .sortedWith { x, y -> x.size.compareTo(y.size) }
    .forEach { key ->
      println("$key => ${outputStyles[key]?.toPureStyle()}")
    }
}

