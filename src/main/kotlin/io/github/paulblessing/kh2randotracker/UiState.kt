package io.github.paulblessing.kh2randotracker

import com.squareup.moshi.JsonClass

const val DEFAULT_TRACKER_WINDOW_WIDTH = 640
const val DEFAULT_TRACKER_WINDOW_HEIGHT = 800
const val DEFAULT_BROADCAST_WINDOW_WIDTH = 320
const val DEFAULT_BROADCAST_WINDOW_HEIGHT = 540

@JsonClass(generateAdapter = true)
class WindowSizeAndPosition(
  val x: Int,
  val y: Int,
  val width: Int,
  val height: Int
)

@JsonClass(generateAdapter = true)
class UiState(
  val trackerWindowSizeClass: SizeClass = SizeClass.Normal,
  val trackerWindowMetrics: WindowSizeAndPosition = WindowSizeAndPosition(
    x = 100,
    y = 50,
    width = DEFAULT_TRACKER_WINDOW_WIDTH,
    height = DEFAULT_TRACKER_WINDOW_HEIGHT
  ),
  val broadcastWindowSizeClass: SizeClass = SizeClass.Normal,
  val broadcastWindowMetrics: WindowSizeAndPosition = WindowSizeAndPosition(
    x = 900,
    y = 200,
    width = DEFAULT_BROADCAST_WINDOW_WIDTH,
    height = DEFAULT_BROADCAST_WINDOW_HEIGHT
  )
)
