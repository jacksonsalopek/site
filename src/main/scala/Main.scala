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
import io.activej.launchers.http.MultithreadedHttpServerLauncher
import io.activej.worker.annotation.{Worker, WorkerId}
import io.activej.reactor.Reactor

import java.util.concurrent.Executor

object Main extends MultithreadedHttpServerLauncher:
  final val RESOURCE_DIR = "public"

  @Provides
  def staticLoader(reactor: Reactor, executor: Executor): IStaticLoader =
    IStaticLoader.ofClassPath(reactor, executor, RESOURCE_DIR)

  @Provides
  @Worker
  def servlet(
      @WorkerId workerId: Int,
      reactor: Reactor,
      staticLoader: IStaticLoader
  ): AsyncServlet =
    RoutingServlet
      .builder(reactor)
      .`with`(
        GET,
        "/",
        { request =>
          HttpResponse.ok200
            .withHtml(html.index("spotify").toString)
            .toPromise
        }
      )
      .`with`(
        GET,
        "/blog",
        { request =>
          HttpResponse.ok200
            .withHtml(html.blog(readManifest()).toString)
            .toPromise
        }
      )
      .`with`(
        GET,
        "/post/:post_id",
        { request =>
          val postId = request.getPathParameter("post_id")
          val postContent = readFileContent(s"posts/${postId}.md")
          val postHTML = renderPost(postContent)
          HttpResponse.ok200
            .withHtml(html.post(postId, postHTML).toString)
            .toPromise
        }
      )
      .`with`(
        GET,
        "/client/menu",
        { request =>
          HttpResponse.ok200
            .withHtml(html.menu_open("menu").toString)
            .toPromise
        }
      )
      .`with`(
        GET,
        "/client/menu/close",
        { request =>
          HttpResponse.ok200
            .withHtml(html.menu_button("menu").toString)
            .toPromise
        }
      )
      .`with`(
        "/public/*",
        StaticServlet.builder(reactor, staticLoader).build
      )
      .build

  def main(args: Array[String]): Unit = {
    val launcher: Launcher = Main
    launcher.launch(args)
  }
