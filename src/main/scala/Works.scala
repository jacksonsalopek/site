package com.jacksonsalopek.site

case class Work(
    title: String,
    desc: String,
    langs: List[String],
    deps: List[String],
    url: String
)
case class Works(works: List[Work])
object Works

val WORKS = Works(
  List[Work](
    Work(
      "aponia",
      "Filesystem-routed API framework built on Bun + Elysia",
      List[String]("ts"),
      List[String]("bun", "elysia"),
      "https://github.com/jacksonsalopek/aponia"
    ),
    Work(
      "skintracker",
      "Utilities for tracking CSGO skins across different markets",
      List[String]("ts", "tsx"),
      List[String]("bun", "htmx", "aponia", "tailwind"),
      "https://github.com/skintracker/skintracker"
    ),
    Work(
      "temporal",
      "Cross-platform time-based budgeting desktop app",
      List[String]("ts", "js"),
      List[String]("electron", "solidjs", "vite"),
      "https://github.com/jacksonsalopek/temporal"
    ),
    Work(
      "ssd",
      "Lightweight state management in SolidJS",
      List[String]("ts"),
      List[String]("solidjs"),
      "https://github.com/jacksonsalopek/temporal/tree/master/packages/shared/ssd"
    ),
    Work(
      "raycast/tradingview",
      "TradingView controls for Raycast",
      List[String]("ts"),
      List[String]("raycast"),
      "https://github.com/jacksonsalopek/raycast-extension-tradingview"
    )
  )
)
