@REM search-service launcher script
@REM
@REM Environment:
@REM JAVA_HOME - location of a JDK home dir (optional if java on path)
@REM CFG_OPTS  - JVM options (optional)
@REM Configuration:
@REM SEARCH_SERVICE_config.txt found in the SEARCH_SERVICE_HOME.
@setlocal enabledelayedexpansion

@echo off


if "%SEARCH_SERVICE_HOME%"=="" (
  set "APP_HOME=%~dp0\\.."

  rem Also set the old env name for backwards compatibility
  set "SEARCH_SERVICE_HOME=%~dp0\\.."
) else (
  set "APP_HOME=%SEARCH_SERVICE_HOME%"
)

set "APP_LIB_DIR=%APP_HOME%\lib\"

rem Detect if we were double clicked, although theoretically A user could
rem manually run cmd /c
for %%x in (!cmdcmdline!) do if %%~x==/c set DOUBLECLICKED=1

rem FIRST we load the config file of extra options.
set "CFG_FILE=%APP_HOME%\SEARCH_SERVICE_config.txt"
set CFG_OPTS=
call :parse_config "%CFG_FILE%" CFG_OPTS

rem We use the value of the JAVACMD environment variable if defined
set _JAVACMD=%JAVACMD%

if "%_JAVACMD%"=="" (
  if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\java.exe" set "_JAVACMD=%JAVA_HOME%\bin\java.exe"
  )
)

if "%_JAVACMD%"=="" set _JAVACMD=java

rem Detect if this java is ok to use.
for /F %%j in ('"%_JAVACMD%" -version  2^>^&1') do (
  if %%~j==java set JAVAINSTALLED=1
  if %%~j==openjdk set JAVAINSTALLED=1
)

rem BAT has no logical or, so we do it OLD SCHOOL! Oppan Redmond Style
set JAVAOK=true
if not defined JAVAINSTALLED set JAVAOK=false

if "%JAVAOK%"=="false" (
  echo.
  echo A Java JDK is not installed or can't be found.
  if not "%JAVA_HOME%"=="" (
    echo JAVA_HOME = "%JAVA_HOME%"
  )
  echo.
  echo Please go to
  echo   http://www.oracle.com/technetwork/java/javase/downloads/index.html
  echo and download a valid Java JDK and install before running search-service.
  echo.
  echo If you think this message is in error, please check
  echo your environment variables to see if "java.exe" and "javac.exe" are
  echo available via JAVA_HOME or PATH.
  echo.
  if defined DOUBLECLICKED pause
  exit /B 1
)


rem We use the value of the JAVA_OPTS environment variable if defined, rather than the config.
set _JAVA_OPTS=%JAVA_OPTS%
if "!_JAVA_OPTS!"=="" set _JAVA_OPTS=!CFG_OPTS!

rem We keep in _JAVA_PARAMS all -J-prefixed and -D-prefixed arguments
rem "-J" is stripped, "-D" is left as is, and everything is appended to JAVA_OPTS
set _JAVA_PARAMS=
set _APP_ARGS=

set "APP_CLASSPATH=%APP_LIB_DIR%\..\conf\;%APP_LIB_DIR%\org.sunbird.search-service-1.0-SNAPSHOT-sans-externalized.jar;%APP_LIB_DIR%\org.scala-lang.scala-library-2.12.11.jar;%APP_LIB_DIR%\com.typesafe.play.twirl-api_2.12-1.4.2.jar;%APP_LIB_DIR%\org.scoverage.scalac-scoverage-runtime_2.12-1.4.7.jar;%APP_LIB_DIR%\com.typesafe.play.play-server_2.12-2.7.9.jar;%APP_LIB_DIR%\com.typesafe.play.play-netty-server_2.12-2.7.9.jar;%APP_LIB_DIR%\com.typesafe.play.play-logback_2.12-2.7.9.jar;%APP_LIB_DIR%\com.typesafe.play.filters-helpers_2.12-2.7.9.jar;%APP_LIB_DIR%\com.typesafe.play.play-guice_2.12-2.7.9.jar;%APP_LIB_DIR%\org.sunbird.actor-core-1.0-SNAPSHOT.jar;%APP_LIB_DIR%\org.sunbird.search-core-1.0-SNAPSHOT.jar;%APP_LIB_DIR%\org.joda.joda-convert-2.1.2.jar;%APP_LIB_DIR%\com.github.danielwegener.logback-kafka-appender-0.2.0-RC2.jar;%APP_LIB_DIR%\net.logstash.logback.logstash-logback-encoder-5.2.jar;%APP_LIB_DIR%\io.lemonlabs.scala-uri_2.12-1.4.10.jar;%APP_LIB_DIR%\net.codingwell.scala-guice_2.12-4.2.5.jar;%APP_LIB_DIR%\com.typesafe.play.play-specs2_2.12-2.7.9.jar;%APP_LIB_DIR%\io.netty.netty-transport-native-epoll-4.1.60.Final.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-xml_2.12-1.2.0.jar;%APP_LIB_DIR%\com.typesafe.play.play_2.12-2.7.9.jar;%APP_LIB_DIR%\com.typesafe.netty.netty-reactive-streams-http-2.0.3.jar;%APP_LIB_DIR%\io.netty.netty-transport-native-epoll-4.1.60.Final-linux-x86_64.jar;%APP_LIB_DIR%\ch.qos.logback.logback-classic-1.2.3.jar;%APP_LIB_DIR%\com.google.inject.guice-4.2.2.jar;%APP_LIB_DIR%\com.google.inject.extensions.guice-assistedinject-4.2.2.jar;%APP_LIB_DIR%\org.sunbird.platform-common-1.0-SNAPSHOT.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-actor_2.12-2.5.31.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-slf4j_2.12-2.5.31.jar;%APP_LIB_DIR%\org.reflections.reflections-0.9.11.jar;%APP_LIB_DIR%\org.apache.commons.commons-lang3-3.9.jar;%APP_LIB_DIR%\commons-lang.commons-lang-2.6.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpclient-4.5.6.jar;%APP_LIB_DIR%\org.sunbird.platform-telemetry-1.0-SNAPSHOT.jar;%APP_LIB_DIR%\org.sunbird.schema-validator-1.0-SNAPSHOT.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpmime-4.5.6.jar;%APP_LIB_DIR%\net.sf.json-lib.json-lib-2.4-jdk15.jar;%APP_LIB_DIR%\org.elasticsearch.client.elasticsearch-rest-high-level-client-6.8.22.jar;%APP_LIB_DIR%\org.apache.kafka.kafka-clients-1.0.0.jar;%APP_LIB_DIR%\org.slf4j.slf4j-api-1.7.26.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-databind-2.9.10.1.jar;%APP_LIB_DIR%\org.parboiled.parboiled_2.12-2.1.7.jar;%APP_LIB_DIR%\com.chuusai.shapeless_2.12-2.3.3.jar;%APP_LIB_DIR%\com.google.guava.guava-27.1-jre.jar;%APP_LIB_DIR%\org.scala-lang.scala-reflect-2.12.11.jar;%APP_LIB_DIR%\com.google.code.findbugs.jsr305-3.0.2.jar;%APP_LIB_DIR%\com.typesafe.play.play-test_2.12-2.7.9.jar;%APP_LIB_DIR%\org.specs2.specs2-core_2.12-4.5.1.jar;%APP_LIB_DIR%\org.specs2.specs2-junit_2.12-4.5.1.jar;%APP_LIB_DIR%\org.specs2.specs2-mock_2.12-4.5.1.jar;%APP_LIB_DIR%\io.netty.netty-common-4.1.60.Final.jar;%APP_LIB_DIR%\io.netty.netty-buffer-4.1.60.Final.jar;%APP_LIB_DIR%\io.netty.netty-transport-4.1.60.Final.jar;%APP_LIB_DIR%\io.netty.netty-transport-native-unix-common-4.1.60.Final.jar;%APP_LIB_DIR%\com.typesafe.play.build-link-2.7.9.jar;%APP_LIB_DIR%\com.typesafe.play.play-streams_2.12-2.7.9.jar;%APP_LIB_DIR%\org.slf4j.jul-to-slf4j-1.7.26.jar;%APP_LIB_DIR%\org.slf4j.jcl-over-slf4j-1.7.26.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-core-2.9.10.jar;%APP_LIB_DIR%\com.fasterxml.jackson.core.jackson-annotations-2.9.10.jar;%APP_LIB_DIR%\com.fasterxml.jackson.datatype.jackson-datatype-jdk8-2.9.10.jar;%APP_LIB_DIR%\com.fasterxml.jackson.datatype.jackson-datatype-jsr310-2.9.10.jar;%APP_LIB_DIR%\com.typesafe.play.play-json_2.12-2.7.4.jar;%APP_LIB_DIR%\io.jsonwebtoken.jjwt-0.9.1.jar;%APP_LIB_DIR%\javax.xml.bind.jaxb-api-2.3.1.jar;%APP_LIB_DIR%\javax.transaction.jta-1.1.jar;%APP_LIB_DIR%\javax.inject.javax.inject-1.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-java8-compat_2.12-0.9.0.jar;%APP_LIB_DIR%\com.typesafe.ssl-config-core_2.12-0.4.1.jar;%APP_LIB_DIR%\org.scala-lang.modules.scala-parser-combinators_2.12-1.1.2.jar;%APP_LIB_DIR%\com.typesafe.netty.netty-reactive-streams-2.0.3.jar;%APP_LIB_DIR%\io.netty.netty-codec-http-4.1.34.Final.jar;%APP_LIB_DIR%\ch.qos.logback.logback-core-1.2.3.jar;%APP_LIB_DIR%\aopalliance.aopalliance-1.0.jar;%APP_LIB_DIR%\com.typesafe.config-1.4.0.jar;%APP_LIB_DIR%\org.apache.commons.commons-collections4-4.4.jar;%APP_LIB_DIR%\com.moparisthebest.junidecode-0.1.1.jar;%APP_LIB_DIR%\commons-io.commons-io-2.7.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-scala_2.12-2.9.8.jar;%APP_LIB_DIR%\org.apache.poi.poi-ooxml-3.15.jar;%APP_LIB_DIR%\org.apache.commons.commons-csv-1.4.jar;%APP_LIB_DIR%\com.mashape.unirest.unirest-java-1.4.9.jar;%APP_LIB_DIR%\org.javassist.javassist-3.21.0-GA.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpcore-4.4.10.jar;%APP_LIB_DIR%\commons-logging.commons-logging-1.2.jar;%APP_LIB_DIR%\commons-codec.commons-codec-1.10.jar;%APP_LIB_DIR%\org.leadpony.justify.justify-1.1.0.jar;%APP_LIB_DIR%\org.glassfish.jakarta.json-1.1.5-module.jar;%APP_LIB_DIR%\commons-beanutils.commons-beanutils-1.8.0.jar;%APP_LIB_DIR%\commons-collections.commons-collections-3.2.1.jar;%APP_LIB_DIR%\net.sf.ezmorph.ezmorph-1.0.6.jar;%APP_LIB_DIR%\org.elasticsearch.elasticsearch-6.8.22.jar;%APP_LIB_DIR%\org.elasticsearch.client.elasticsearch-rest-client-6.8.22.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.parent-join-client-6.8.22.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.aggs-matrix-stats-client-6.8.22.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.rank-eval-client-6.8.22.jar;%APP_LIB_DIR%\org.elasticsearch.plugin.lang-mustache-client-6.8.22.jar;%APP_LIB_DIR%\org.lz4.lz4-java-1.4.jar;%APP_LIB_DIR%\org.xerial.snappy.snappy-java-1.1.4.jar;%APP_LIB_DIR%\org.typelevel.macro-compat_2.12-1.1.1.jar;%APP_LIB_DIR%\com.google.guava.failureaccess-1.0.1.jar;%APP_LIB_DIR%\com.google.guava.listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_LIB_DIR%\org.checkerframework.checker-qual-2.5.2.jar;%APP_LIB_DIR%\com.google.errorprone.error_prone_annotations-2.2.0.jar;%APP_LIB_DIR%\com.google.j2objc.j2objc-annotations-1.1.jar;%APP_LIB_DIR%\org.codehaus.mojo.animal-sniffer-annotations-1.17.jar;%APP_LIB_DIR%\com.typesafe.play.play-akka-http-server_2.12-2.7.9.jar;%APP_LIB_DIR%\junit.junit-4.12.jar;%APP_LIB_DIR%\com.novocode.junit-interface-0.11.jar;%APP_LIB_DIR%\org.fluentlenium.fluentlenium-core-3.7.1.jar;%APP_LIB_DIR%\org.seleniumhq.selenium.htmlunit-driver-2.33.3.jar;%APP_LIB_DIR%\org.seleniumhq.selenium.selenium-api-3.141.59.jar;%APP_LIB_DIR%\org.seleniumhq.selenium.selenium-support-3.141.59.jar;%APP_LIB_DIR%\org.seleniumhq.selenium.selenium-firefox-driver-3.141.59.jar;%APP_LIB_DIR%\org.specs2.specs2-matcher_2.12-4.5.1.jar;%APP_LIB_DIR%\org.scala-sbt.test-interface-1.0.jar;%APP_LIB_DIR%\org.hamcrest.hamcrest-core-1.3.jar;%APP_LIB_DIR%\org.mockito.mockito-core-2.23.4.jar;%APP_LIB_DIR%\io.netty.netty-resolver-4.1.60.Final.jar;%APP_LIB_DIR%\com.typesafe.play.play-exceptions-2.7.9.jar;%APP_LIB_DIR%\org.reactivestreams.reactive-streams-1.0.2.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-stream_2.12-2.5.31.jar;%APP_LIB_DIR%\com.typesafe.play.play-functional_2.12-2.7.4.jar;%APP_LIB_DIR%\joda-time.joda-time-2.10.10.jar;%APP_LIB_DIR%\javax.activation.javax.activation-api-1.2.0.jar;%APP_LIB_DIR%\io.netty.netty-handler-4.1.34.Final.jar;%APP_LIB_DIR%\io.netty.netty-codec-4.1.34.Final.jar;%APP_LIB_DIR%\com.fasterxml.jackson.module.jackson-module-paranamer-2.9.8.jar;%APP_LIB_DIR%\org.apache.poi.poi-3.15.jar;%APP_LIB_DIR%\org.apache.poi.poi-ooxml-schemas-3.15.jar;%APP_LIB_DIR%\com.github.virtuald.curvesapi-1.04.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpasyncclient-4.1.2.jar;%APP_LIB_DIR%\org.json.json-20160212.jar;%APP_LIB_DIR%\jakarta.json.jakarta.json-api-1.1.5.jar;%APP_LIB_DIR%\com.ibm.icu.icu4j-64.2.jar;%APP_LIB_DIR%\org.elasticsearch.elasticsearch-core-6.8.22.jar;%APP_LIB_DIR%\org.elasticsearch.elasticsearch-secure-sm-6.8.22.jar;%APP_LIB_DIR%\org.elasticsearch.elasticsearch-x-content-6.8.22.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-core-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-analyzers-common-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-backward-codecs-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-grouping-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-highlighter-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-join-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-memory-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-misc-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-queries-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-queryparser-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-sandbox-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-spatial-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-spatial-extras-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-spatial3d-7.7.3.jar;%APP_LIB_DIR%\org.apache.lucene.lucene-suggest-7.7.3.jar;%APP_LIB_DIR%\org.elasticsearch.elasticsearch-cli-6.8.22.jar;%APP_LIB_DIR%\com.carrotsearch.hppc-0.7.1.jar;%APP_LIB_DIR%\com.tdunning.t-digest-3.2.jar;%APP_LIB_DIR%\org.hdrhistogram.HdrHistogram-2.1.9.jar;%APP_LIB_DIR%\org.apache.logging.log4j.log4j-api-2.17.0.jar;%APP_LIB_DIR%\org.elasticsearch.jna-5.5.0.jar;%APP_LIB_DIR%\org.apache.httpcomponents.httpcore-nio-4.4.5.jar;%APP_LIB_DIR%\com.github.spullara.mustache.java.compiler-0.9.3.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-http-core_2.12-10.1.12.jar;%APP_LIB_DIR%\org.seleniumhq.selenium.selenium-remote-driver-3.141.59.jar;%APP_LIB_DIR%\org.atteo.classindex.classindex-3.4.jar;%APP_LIB_DIR%\net.sourceforge.htmlunit.htmlunit-2.33.jar;%APP_LIB_DIR%\net.bytebuddy.byte-buddy-1.9.3.jar;%APP_LIB_DIR%\org.apache.commons.commons-exec-1.3.jar;%APP_LIB_DIR%\com.squareup.okhttp3.okhttp-3.11.0.jar;%APP_LIB_DIR%\com.squareup.okio.okio-1.14.0.jar;%APP_LIB_DIR%\org.specs2.specs2-common_2.12-4.5.1.jar;%APP_LIB_DIR%\net.bytebuddy.byte-buddy-agent-1.9.3.jar;%APP_LIB_DIR%\org.objenesis.objenesis-2.6.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-protobuf_2.12-2.5.31.jar;%APP_LIB_DIR%\com.thoughtworks.paranamer.paranamer-2.8.jar;%APP_LIB_DIR%\org.apache.xmlbeans.xmlbeans-2.6.0.jar;%APP_LIB_DIR%\org.yaml.snakeyaml-1.17.jar;%APP_LIB_DIR%\com.fasterxml.jackson.dataformat.jackson-dataformat-smile-2.8.11.jar;%APP_LIB_DIR%\com.fasterxml.jackson.dataformat.jackson-dataformat-yaml-2.8.11.jar;%APP_LIB_DIR%\com.fasterxml.jackson.dataformat.jackson-dataformat-cbor-2.8.11.jar;%APP_LIB_DIR%\net.sf.jopt-simple.jopt-simple-5.0.2.jar;%APP_LIB_DIR%\com.typesafe.akka.akka-parsing_2.12-10.1.12.jar;%APP_LIB_DIR%\xalan.xalan-2.7.2.jar;%APP_LIB_DIR%\org.apache.commons.commons-text-1.4.jar;%APP_LIB_DIR%\net.sourceforge.htmlunit.htmlunit-core-js-2.33.jar;%APP_LIB_DIR%\net.sourceforge.htmlunit.neko-htmlunit-2.33.jar;%APP_LIB_DIR%\net.sourceforge.htmlunit.htmlunit-cssparser-1.2.0.jar;%APP_LIB_DIR%\commons-net.commons-net-3.6.jar;%APP_LIB_DIR%\org.eclipse.jetty.websocket.websocket-client-9.4.12.v20180830.jar;%APP_LIB_DIR%\org.specs2.specs2-fp_2.12-4.5.1.jar;%APP_LIB_DIR%\stax.stax-api-1.0.1.jar;%APP_LIB_DIR%\xalan.serializer-2.7.2.jar;%APP_LIB_DIR%\xerces.xercesImpl-2.12.0.jar;%APP_LIB_DIR%\org.eclipse.jetty.jetty-client-9.4.12.v20180830.jar;%APP_LIB_DIR%\org.eclipse.jetty.jetty-xml-9.4.12.v20180830.jar;%APP_LIB_DIR%\org.eclipse.jetty.jetty-util-9.4.12.v20180830.jar;%APP_LIB_DIR%\org.eclipse.jetty.jetty-io-9.4.12.v20180830.jar;%APP_LIB_DIR%\org.eclipse.jetty.websocket.websocket-common-9.4.12.v20180830.jar;%APP_LIB_DIR%\xml-apis.xml-apis-1.4.01.jar;%APP_LIB_DIR%\org.eclipse.jetty.jetty-http-9.4.12.v20180830.jar;%APP_LIB_DIR%\org.eclipse.jetty.websocket.websocket-api-9.4.12.v20180830.jar;%APP_LIB_DIR%\org.sunbird.search-service-1.0-SNAPSHOT-assets.jar"
set "APP_MAIN_CLASS=play.core.server.ProdServerStart"
set "SCRIPT_CONF_FILE=%APP_HOME%\conf\application.ini"

rem if configuration files exist, prepend their contents to the script arguments so it can be processed by this runner
call :parse_config "%SCRIPT_CONF_FILE%" SCRIPT_CONF_ARGS

call :process_args %SCRIPT_CONF_ARGS% %%*

set _JAVA_OPTS=!_JAVA_OPTS! !_JAVA_PARAMS!

if defined CUSTOM_MAIN_CLASS (
    set MAIN_CLASS=!CUSTOM_MAIN_CLASS!
) else (
    set MAIN_CLASS=!APP_MAIN_CLASS!
)

rem Call the application and pass all arguments unchanged.
"%_JAVACMD%" !_JAVA_OPTS! !SEARCH_SERVICE_OPTS! -cp "%APP_CLASSPATH%" %MAIN_CLASS% !_APP_ARGS!

@endlocal

exit /B %ERRORLEVEL%


rem Loads a configuration file full of default command line options for this script.
rem First argument is the path to the config file.
rem Second argument is the name of the environment variable to write to.
:parse_config
  set _PARSE_FILE=%~1
  set _PARSE_OUT=
  if exist "%_PARSE_FILE%" (
    FOR /F "tokens=* eol=# usebackq delims=" %%i IN ("%_PARSE_FILE%") DO (
      set _PARSE_OUT=!_PARSE_OUT! %%i
    )
  )
  set %2=!_PARSE_OUT!
exit /B 0


:add_java
  set _JAVA_PARAMS=!_JAVA_PARAMS! %*
exit /B 0


:add_app
  set _APP_ARGS=!_APP_ARGS! %*
exit /B 0


rem Processes incoming arguments and places them in appropriate global variables
:process_args
  :param_loop
  call set _PARAM1=%%1
  set "_TEST_PARAM=%~1"

  if ["!_PARAM1!"]==[""] goto param_afterloop


  rem ignore arguments that do not start with '-'
  if "%_TEST_PARAM:~0,1%"=="-" goto param_java_check
  set _APP_ARGS=!_APP_ARGS! !_PARAM1!
  shift
  goto param_loop

  :param_java_check
  if "!_TEST_PARAM:~0,2!"=="-J" (
    rem strip -J prefix
    set _JAVA_PARAMS=!_JAVA_PARAMS! !_TEST_PARAM:~2!
    shift
    goto param_loop
  )

  if "!_TEST_PARAM:~0,2!"=="-D" (
    rem test if this was double-quoted property "-Dprop=42"
    for /F "delims== tokens=1,*" %%G in ("!_TEST_PARAM!") DO (
      if not ["%%H"] == [""] (
        set _JAVA_PARAMS=!_JAVA_PARAMS! !_PARAM1!
      ) else if [%2] neq [] (
        rem it was a normal property: -Dprop=42 or -Drop="42"
        call set _PARAM1=%%1=%%2
        set _JAVA_PARAMS=!_JAVA_PARAMS! !_PARAM1!
        shift
      )
    )
  ) else (
    if "!_TEST_PARAM!"=="-main" (
      call set CUSTOM_MAIN_CLASS=%%2
      shift
    ) else (
      set _APP_ARGS=!_APP_ARGS! !_PARAM1!
    )
  )
  shift
  goto param_loop
  :param_afterloop

exit /B 0
