package sample2

// class Style(
//   val backgroundColor: String? = null,
//   val contentColor: String? = null,
// ) {
//   internal fun overriddenWith(style: Style) = run {
//     return@run Style(
//       backgroundColor = style.backgroundColor ?: this.backgroundColor,
//       contentColor = style.contentColor ?: this.contentColor
//     )
//   }
//
//   override fun toString(): String {
//     return "{ bg: $backgroundColor, content: $contentColor }"
//   }
// }
//
// enum class StyleState {
//   Focused,
//   Pressed,
//   Selected,
//   Disabled;
//
//   companion object {
//     val possibleCombinations = possibleCombinations()
//
//     private fun possibleCombinations(
//       options: Set<StyleState> = StyleState.values().toSet()
//     ): Set<Set<StyleState>> {
//       if (options.isEmpty()) return setOf(emptySet())
//
//       val prefix = options.toList()[0]
//       val tailOptions = buildSet {
//         addAll(options)
//         remove(prefix)
//       }
//
//       val setWithoutPrefix = possibleCombinations(tailOptions)
//       return buildSet {
//         addAll(setWithoutPrefix)
//         if (setWithoutPrefix.isEmpty()) {
//           add(setOf(prefix))
//         } else {
//           setWithoutPrefix.forEach {
//             add(buildSet {
//               addAll(it)
//               add(prefix)
//             })
//           }
//         }
//       }
//     }
//   }
// }
//
// data class InheritedStyle(
//   val parentKey: Set<StyleState>,
//   val style: Style
// )
//
// interface FlattenableStyle {
//   fun flattenToMap(
//     currentKey: Set<StyleState>,
//     parentStyle: Style = Style(),
//     outputStyles: MutableMap<Set<StyleState>, InheritedStyle>
//   )
// }
//
// object NoOverrides: FlattenableStyle {
//   // no calculation of style. Only propagating the current style down the current subtree.
//   // TODO: If a value is already present in the map, should this override it?
//   override fun flattenToMap(
//     currentKey: Set<StyleState>,
//     parentStyle: Style,
//     outputStyles: MutableMap<Set<StyleState>, InheritedStyle>
//   ) {
//     StyleState.possibleCombinations
//       .filter { it.containsAll(currentKey) && it.size >= currentKey.size } // only child keys
//       .filterNot { (outputStyles[it]?.parentKey?.size ?:0) > currentKey.size }   // Styles which inherit from more specific keys should not be overridden
//       .forEach { outputStyles[it] = InheritedStyle(currentKey, parentStyle) }
//   }
// }
//
// class Styles(
//   backgroundColor: String? = null,
//   contentColor: String? = null,
//
//   private val focused: FlattenableStyle = NoOverrides,
//   private val pressed: FlattenableStyle = NoOverrides,
//   private val selected: FlattenableStyle = NoOverrides,
//   private val disabled: FlattenableStyle = NoOverrides,
// ) : FlattenableStyle {
//   private val style = Style(backgroundColor, contentColor)
//
//   override fun flattenToMap(
//     currentKey: Set<StyleState>,
//     parentStyle: Style,
//     outputStyles: MutableMap<Set<StyleState>, InheritedStyle>
//   ) {
//     val currentStyle = parentStyle.overriddenWith(style)
//     val previouslyStoredStyle = outputStyles[currentKey]?.style ?: Style()
//     val updatedCurrentStyle = previouslyStoredStyle.overriddenWith(currentStyle)
//     outputStyles[currentKey] = InheritedStyle(currentKey, updatedCurrentStyle)
//
//     focused.flattenToMap(
//       currentKey.updateWith { add(StyleState.Focused) },
//       outputStyles[currentKey]?.style!!,
//       outputStyles
//     )
//
//     pressed.flattenToMap(
//       currentKey.updateWith { add(StyleState.Pressed) },
//       outputStyles[currentKey]?.style!!,
//       outputStyles
//     )
//
//     selected.flattenToMap(
//       currentKey.updateWith { add(StyleState.Selected) },
//       outputStyles[currentKey]?.style!!,
//       outputStyles
//     )
//
//     disabled.flattenToMap(
//       currentKey.updateWith { add(StyleState.Disabled) },
//       outputStyles[currentKey]?.style!!,
//       outputStyles
//     )
//   }
//
//   private fun <E> Set<E>.updateWith(builderAction: MutableSet<E>.() -> Unit): Set<E> =
//     buildSet {
//       addAll(this@updateWith)
//       builderAction.invoke(this)
//     }
// }
//
// fun main() {
//   val styles = Styles(
//     backgroundColor = "Default",
//
//     focused = Styles(
//       pressed = Styles(
//         disabled = Styles(
//           backgroundColor = "F.P.D"
//         ),
//       )
//     ),
//
//     pressed = Styles(
//       focused = Styles(
//         backgroundColor = "P.F"
//       )
//     )
//   )
//
//   val outputStyles: MutableMap<Set<StyleState>, InheritedStyle> = mutableMapOf()
//   styles.flattenToMap(setOf(), outputStyles = outputStyles)
//
//   StyleState.possibleCombinations
//     .sortedWith { x, y -> x.size.compareTo(y.size) }
//     .forEach { key ->
//       println("$key => ${outputStyles[key]?.style}")
//     }
// }