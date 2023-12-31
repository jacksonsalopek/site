package com.jacksonsalopek.site

import io.activej.http.{
  AsyncServlet,
  HttpResponse,
  RoutingServlet,
  StaticServlet
}
import io.activej.http.HttpMethod.GET
import io.activej.http.loader.IStaticLoader
import io.activej.inject.annotation.Provides
import io.activej.launcher.Launcher
import io.activej.launchers.http.HttpServerLauncher
import io.activej.worker.annotation.{Worker, WorkerId}
import io.activej.reactor.Reactor

import java.util.concurrent.Executor
import java.util.concurrent.Executors.newSingleThreadExecutor

object Main extends HttpServerLauncher:
  final val RESOURCE_DIR = "public"

  @Provides
  def executor(): Executor = {
    newSingleThreadExecutor()
  }

  @Provides
  def staticLoader(reactor: Reactor, executor: Executor): IStaticLoader =
    IStaticLoader.ofClassPath(reactor, executor, RESOURCE_DIR)

  def index(): AsyncServlet = { request =>
    HttpResponse.ok200
      .withHtml(html.index("spotify").toString)
      .toPromise
  }

  def works(): AsyncServlet = { request =>
    HttpResponse.ok200
      .withHtml(html.works(WORKS).toString)
      .toPromise
  }

  def blog(): AsyncServlet = { request =>
    HttpResponse.ok200
      .withHtml(html.blog(readManifest()).toString)
      .toPromise
  }

  def post(): AsyncServlet = { request =>
    val postId = request.getPathParameter("post_id")
    val postContent = readFileContent(s"posts/${postId}.md")
    val postHTML = renderPost(postContent)
    HttpResponse.ok200
      .withHtml(html.post(postId, postHTML).toString)
      .toPromise
  }

  def menuOpen(): AsyncServlet = { request =>
    HttpResponse.ok200
      .withHtml(html.menu_open("menu").toString)
      .toPromise
  }

  def menuClose(): AsyncServlet = { request =>
    HttpResponse.ok200
      .withHtml(html.menu_button("menu").toString)
      .toPromise
  }

  @Provides
  def servlet(
      reactor: Reactor,
      staticLoader: IStaticLoader
  ): AsyncServlet =
    RoutingServlet
      .builder(reactor)
      .`with`(GET, "/", index())
      .`with`(GET, "/works", works())
      .`with`(GET, "/blog", blog())
      .`with`(GET, "/post/:post_id", post())
      .`with`(GET, "/client/menu", menuOpen())
      .`with`(GET, "/client/menu/close", menuClose())
      .`with`(
        "/public/*",
        StaticServlet.builder(reactor, staticLoader).build
      )
      .build

  def main(args: Array[String]): Unit = {
    val launcher: Launcher = Main
    launcher.launch(args)
  }
