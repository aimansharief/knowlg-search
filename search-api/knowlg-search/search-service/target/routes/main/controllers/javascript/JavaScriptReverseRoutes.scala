// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/shouryasolanki/work1/sunbird/knowledge/knowlg-search/knowlg-search/search-service/conf/routes
// @DATE:Fri Aug 11 12:25:03 IST 2023

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset

// @LINE:4
package controllers.javascript {

  // @LINE:4
  class ReverseHealthController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:4
    def health: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HealthController.health",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "health"})
        }
      """
    )
  
    // @LINE:5
    def serviceHealth: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HealthController.serviceHealth",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "service/health"})
        }
      """
    )
  
  }

  // @LINE:8
  class ReverseSearchController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def search: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SearchController.search",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "v3/search"})
        }
      """
    )
  
    // @LINE:9
    def privateSearch: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SearchController.privateSearch",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "v3/private/search"})
        }
      """
    )
  
    // @LINE:10
    def count: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.SearchController.count",
      """
        function() {
        
          if (true) {
            return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "v2/search/count"})
          }
        
        }
      """
    )
  
  }


}
