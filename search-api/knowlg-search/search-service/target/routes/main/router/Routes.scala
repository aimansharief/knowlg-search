// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/shouryasolanki/work1/sunbird/knowledge/knowlg-search/knowlg-search/search-service/conf/routes
// @DATE:Fri Aug 11 12:25:03 IST 2023

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:4
  HealthController_0: controllers.HealthController,
  // @LINE:8
  SearchController_1: controllers.SearchController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:4
    HealthController_0: controllers.HealthController,
    // @LINE:8
    SearchController_1: controllers.SearchController
  ) = this(errorHandler, HealthController_0, SearchController_1, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HealthController_0, SearchController_1, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """health""", """controllers.HealthController.health()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """service/health""", """controllers.HealthController.serviceHealth()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """v3/search""", """controllers.SearchController.search()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """v3/private/search""", """controllers.SearchController.privateSearch()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """v2/search/count""", """controllers.SearchController.count()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """v3/count""", """controllers.SearchController.count()"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:4
  private[this] lazy val controllers_HealthController_health0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("health")))
  )
  private[this] lazy val controllers_HealthController_health0_invoker = createInvoker(
    HealthController_0.health(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HealthController",
      "health",
      Nil,
      "GET",
      this.prefix + """health""",
      """ Routes
 This file defines all application routes (Higher priority routes first)
 ~~~~""",
      Seq()
    )
  )

  // @LINE:5
  private[this] lazy val controllers_HealthController_serviceHealth1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("service/health")))
  )
  private[this] lazy val controllers_HealthController_serviceHealth1_invoker = createInvoker(
    HealthController_0.serviceHealth(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HealthController",
      "serviceHealth",
      Nil,
      "GET",
      this.prefix + """service/health""",
      """""",
      Seq()
    )
  )

  // @LINE:8
  private[this] lazy val controllers_SearchController_search2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("v3/search")))
  )
  private[this] lazy val controllers_SearchController_search2_invoker = createInvoker(
    SearchController_1.search(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SearchController",
      "search",
      Nil,
      "POST",
      this.prefix + """v3/search""",
      """POST    /v2/search						controllers.SearchController.search()""",
      Seq()
    )
  )

  // @LINE:9
  private[this] lazy val controllers_SearchController_privateSearch3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("v3/private/search")))
  )
  private[this] lazy val controllers_SearchController_privateSearch3_invoker = createInvoker(
    SearchController_1.privateSearch(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SearchController",
      "privateSearch",
      Nil,
      "POST",
      this.prefix + """v3/private/search""",
      """""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_SearchController_count4_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("v2/search/count")))
  )
  private[this] lazy val controllers_SearchController_count4_invoker = createInvoker(
    SearchController_1.count(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SearchController",
      "count",
      Nil,
      "POST",
      this.prefix + """v2/search/count""",
      """""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val controllers_SearchController_count5_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("v3/count")))
  )
  private[this] lazy val controllers_SearchController_count5_invoker = createInvoker(
    SearchController_1.count(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.SearchController",
      "count",
      Nil,
      "POST",
      this.prefix + """v3/count""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:4
    case controllers_HealthController_health0_route(params@_) =>
      call { 
        controllers_HealthController_health0_invoker.call(HealthController_0.health())
      }
  
    // @LINE:5
    case controllers_HealthController_serviceHealth1_route(params@_) =>
      call { 
        controllers_HealthController_serviceHealth1_invoker.call(HealthController_0.serviceHealth())
      }
  
    // @LINE:8
    case controllers_SearchController_search2_route(params@_) =>
      call { 
        controllers_SearchController_search2_invoker.call(SearchController_1.search())
      }
  
    // @LINE:9
    case controllers_SearchController_privateSearch3_route(params@_) =>
      call { 
        controllers_SearchController_privateSearch3_invoker.call(SearchController_1.privateSearch())
      }
  
    // @LINE:10
    case controllers_SearchController_count4_route(params@_) =>
      call { 
        controllers_SearchController_count4_invoker.call(SearchController_1.count())
      }
  
    // @LINE:11
    case controllers_SearchController_count5_route(params@_) =>
      call { 
        controllers_SearchController_count5_invoker.call(SearchController_1.count())
      }
  }
}
