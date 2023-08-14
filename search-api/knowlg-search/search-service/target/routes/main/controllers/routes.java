// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/shouryasolanki/work1/sunbird/knowledge/knowlg-search/knowlg-search/search-service/conf/routes
// @DATE:Fri Aug 11 12:25:03 IST 2023

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseHealthController HealthController = new controllers.ReverseHealthController(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseSearchController SearchController = new controllers.ReverseSearchController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseHealthController HealthController = new controllers.javascript.ReverseHealthController(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseSearchController SearchController = new controllers.javascript.ReverseSearchController(RoutesPrefix.byNamePrefix());
  }

}
