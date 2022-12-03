package vighnesh

import style_engine.ContainedButtonStyles
import style_engine.ElementState

const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"

data class TestCase(
  val identifier: String,
  val styles: ContainedButtonStyles,
  val output: Map<String, String>,
)

@Composable
fun TestRunner() {
  testcases.forEachIndexed { index, testcase ->
    // println("Vighnesh Validating ${testCase.identifier}")
    var mismatchedOutputs = 0
    ElementState.possibleCombinations
      .sortedWith { x, y -> x.size.compareTo(y.size) }
      .forEach { key ->
        val expectedOutput = testcase.output[key.toString()]
        val actualOutput = testcase.styles.getStyle(key).toString()
        if (expectedOutput != actualOutput) {
          println("$ANSI_YELLOW Vighnesh \tFor key -> $key$ANSI_RESET")
          println("$ANSI_GREEN Vighnesh \t\tExpected: \n\t\t\t+ $expectedOutput$ANSI_RESET")
          println("$ANSI_RED Vighnesh \t\tReceived: \n\t\t\t- $actualOutput$ANSI_RESET")
          mismatchedOutputs++
        }
      }

    if (mismatchedOutputs > 0) {
      println()
      println("$ANSI_RED Vighnesh ⤫ Test failed: \"${testcase.identifier}\"$ANSI_RESET\n")

      if (index != testcases.lastIndex) {
        println("-".repeat(50))
      }
    } else {
      println("$ANSI_GREEN Vighnesh ✓ Test passed: \"${testcase.identifier}\"$ANSI_RESET")
    }
  }
}

val testcases = listOf(
  TestCase(
    identifier = "testcase1",
    styles = ContainedButtonStyles {
      backgroundColor = "default"

      onFocused {
        onPressed {
          onSelected {
            backgroundColor = "F.P.S"
          }
          onDisabled {
            backgroundColor = "F.P.D"
          }
        }
      }

      onPressed {
        onFocused {
          backgroundColor = "P.F"
        }
      }
    },
    output = mapOf(
      "[]" to "bgColor: default, contentColor: null",
      "[Disabled]" to "bgColor: default, contentColor: null",
      "[Selected]" to "bgColor: default, contentColor: null",
      "[Pressed]" to "bgColor: default, contentColor: null",
      "[Focused]" to "bgColor: default, contentColor: null",
      "[Selected, Disabled]" to "bgColor: default, contentColor: null",
      "[Pressed, Disabled]" to "bgColor: default, contentColor: null",
      "[Focused, Disabled]" to "bgColor: default, contentColor: null",
      "[Pressed, Selected]" to "bgColor: default, contentColor: null",
      "[Focused, Selected]" to "bgColor: default, contentColor: null",
      "[Focused, Pressed]" to "bgColor: P.F, contentColor: null",
      "[Pressed, Selected, Disabled]" to "bgColor: default, contentColor: null",
      "[Focused, Selected, Disabled]" to "bgColor: default, contentColor: null",
      "[Focused, Pressed, Disabled]" to "bgColor: F.P.D, contentColor: null",
      "[Focused, Pressed, Selected]" to "bgColor: F.P.S, contentColor: null",
      "[Focused, Pressed, Selected, Disabled]" to "bgColor: F.P.D, contentColor: null",
    )
  ),
  TestCase(
    identifier = "testcase2",
    styles = ContainedButtonStyles {
      backgroundColor = "default"

      onFocused {
        onPressed {
          onFocused {
            backgroundColor = "F.P.F"
          }
        }
      }

      onPressed {
        onFocused {
          backgroundColor = "P.F"
        }
      }
    },
    output = mapOf(
      "[]" to "bgColor: default, contentColor: null",
      "[Disabled]" to "bgColor: default, contentColor: null",
      "[Selected]" to "bgColor: default, contentColor: null",
      "[Pressed]" to "bgColor: default, contentColor: null",
      "[Focused]" to "bgColor: default, contentColor: null",
      "[Selected, Disabled]" to "bgColor: default, contentColor: null",
      "[Pressed, Disabled]" to "bgColor: default, contentColor: null",
      "[Focused, Disabled]" to "bgColor: default, contentColor: null",
      "[Pressed, Selected]" to "bgColor: default, contentColor: null",
      "[Focused, Selected]" to "bgColor: default, contentColor: null",
      "[Focused, Pressed]" to "bgColor: F.P.F, contentColor: null",
      "[Pressed, Selected, Disabled]" to "bgColor: default, contentColor: null",
      "[Focused, Selected, Disabled]" to "bgColor: default, contentColor: null",
      "[Focused, Pressed, Disabled]" to "bgColor: F.P.F, contentColor: null",
      "[Focused, Pressed, Selected]" to "bgColor: F.P.F, contentColor: null",
      "[Focused, Pressed, Selected, Disabled]" to "bgColor: F.P.F, contentColor: null",
    )
  ),
  TestCase(
    identifier = "testcase3",
    styles = ContainedButtonStyles {
      onFocused {
        onPressed {
          backgroundColor = "F.P"
        }
      }
    },
    output = mapOf(
      "[]" to "bgColor: null, contentColor: null",
      "[Disabled]" to "bgColor: null, contentColor: null",
      "[Selected]" to "bgColor: null, contentColor: null",
      "[Pressed]" to "bgColor: null, contentColor: null",
      "[Focused]" to "bgColor: null, contentColor: null",
      "[Selected, Disabled]" to "bgColor: null, contentColor: null",
      "[Pressed, Disabled]" to "bgColor: null, contentColor: null",
      "[Focused, Disabled]" to "bgColor: null, contentColor: null",
      "[Pressed, Selected]" to "bgColor: null, contentColor: null",
      "[Focused, Selected]" to "bgColor: null, contentColor: null",
      "[Focused, Pressed]" to "bgColor: F.P, contentColor: null",
      "[Pressed, Selected, Disabled]" to "bgColor: null, contentColor: null",
      "[Focused, Selected, Disabled]" to "bgColor: null, contentColor: null",
      "[Focused, Pressed, Disabled]" to "bgColor: F.P, contentColor: null",
      "[Focused, Pressed, Selected]" to "bgColor: F.P, contentColor: null",
      "[Focused, Pressed, Selected, Disabled]" to "bgColor: F.P, contentColor: null",
    )
  ),
  TestCase(
    identifier = "testcase4",
    styles = ContainedButtonStyles {
      onFocused {
        onPressed {
          backgroundColor = "F.P"
        }
      }

      onPressed {
        backgroundColor = "P"
        contentColor = "P"
      }
    },
    output = mapOf(
      "[]" to "bgColor: null, contentColor: null",
      "[Disabled]" to "bgColor: null, contentColor: null",
      "[Selected]" to "bgColor: null, contentColor: null",
      "[Pressed]" to "bgColor: P, contentColor: P",
      "[Focused]" to "bgColor: null, contentColor: null",
      "[Selected, Disabled]" to "bgColor: null, contentColor: null",
      "[Pressed, Disabled]" to "bgColor: P, contentColor: P",
      "[Focused, Disabled]" to "bgColor: null, contentColor: null",
      "[Pressed, Selected]" to "bgColor: P, contentColor: P",
      "[Focused, Selected]" to "bgColor: null, contentColor: null",
      "[Focused, Pressed]" to "bgColor: F.P, contentColor: P",
      "[Pressed, Selected, Disabled]" to "bgColor: P, contentColor: P",
      "[Focused, Selected, Disabled]" to "bgColor: null, contentColor: null",
      "[Focused, Pressed, Disabled]" to "bgColor: F.P, contentColor: P",
      "[Focused, Pressed, Selected]" to "bgColor: F.P, contentColor: P",
      "[Focused, Pressed, Selected, Disabled]" to "bgColor: F.P, contentColor: P",
    )
  ),
  TestCase(
    identifier = "testcase5",
    styles = ContainedButtonStyles {
      onFocused {
        onPressed {
          backgroundColor = "F.P"
        }
      }

      onPressed {
        backgroundColor = "P"
        contentColor = "P"
      }

      onFocused {
        onPressed {
          onSelected {
            backgroundColor = "F.P.S"
          }
        }
      }
    },
    output = mapOf(
      "[]" to "bgColor: null, contentColor: null",
      "[Disabled]" to "bgColor: null, contentColor: null",
      "[Selected]" to "bgColor: null, contentColor: null",
      "[Pressed]" to "bgColor: P, contentColor: P",
      "[Focused]" to "bgColor: null, contentColor: null",
      "[Selected, Disabled]" to "bgColor: null, contentColor: null",
      "[Pressed, Disabled]" to "bgColor: P, contentColor: P",
      "[Focused, Disabled]" to "bgColor: null, contentColor: null",
      "[Pressed, Selected]" to "bgColor: P, contentColor: P",
      "[Focused, Selected]" to "bgColor: null, contentColor: null",
      "[Focused, Pressed]" to "bgColor: F.P, contentColor: P",
      "[Pressed, Selected, Disabled]" to "bgColor: P, contentColor: P",
      "[Focused, Selected, Disabled]" to "bgColor: null, contentColor: null",
      "[Focused, Pressed, Disabled]" to "bgColor: F.P, contentColor: P",
      "[Focused, Pressed, Selected]" to "bgColor: F.P.S, contentColor: P",
      "[Focused, Pressed, Selected, Disabled]" to "bgColor: F.P.S, contentColor: P",
    )
  ),
)
