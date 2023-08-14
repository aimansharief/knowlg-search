// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/shouryasolanki/work1/sunbird/knowledge/knowlg-search/knowlg-search/search-service/conf/routes
// @DATE:Fri Aug 11 12:25:03 IST 2023


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
