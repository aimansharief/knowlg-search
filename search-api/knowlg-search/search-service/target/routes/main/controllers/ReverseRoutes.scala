// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/shouryasolanki/work1/sunbird/knowledge/knowlg-search/knowlg-search/search-service/conf/routes
// @DATE:Fri Aug 11 12:25:03 IST 2023

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:4
package controllers {

  // @LINE:4
  class ReverseHealthController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:4
    def health(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "health")
    }
  
    // @LINE:5
    def serviceHealth(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "service/health")
    }
  
  }

  // @LINE:8
  class ReverseSearchController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def search(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "v3/search")
    }
  
    // @LINE:9
    def privateSearch(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "v3/private/search")
    }
  
    // @LINE:10
    def count(): Call = {
    
      () match {
      
        // @LINE:10
        case ()  =>
          
          Call("POST", _prefix + { _defaultPrefix } + "v2/search/count")
      
      }
    
    }
  
  }


}
