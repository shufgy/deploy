package com.gu.deploy
package json

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import java.io.File

class JsonReaderTest extends FlatSpec with ShouldMatchers {
  val contentApiExample = """
  {
    "packages":{
      "index-builder":{
        "type":"jetty-webapp",
        "roles":["index-builder"],
      },
      "api":{
        "type":"jetty-webapp",
        "roles":["api"],
      },
      "solr":{
        "type":"jetty-webapp",
        "roles":["solr"],
        "data": {
          "port": "8400"
        }
      }
    },
    "recipes":{
      "all":{
        "default":true,
        "depends":["index-build-only","api-only"]
      },
      "index-build-only":{
        "default":false,
        "actions":["index-builder.deploy"],
      },
      "api-only":{
        "default":false,
        "actions":[
          "api.deploy","solr.deploy"
        ],
      }
    }
  }
"""

  "json parser" should "parse json and resolve links" in {
    val parsed = JsonReader.parse(contentApiExample, new File("/tmp/abc/123.json"))

    parsed.roles should be (Set(Role("index-builder"), Role("api"), Role("solr")))

    parsed.packages.size should be (3)
    parsed.packages("index-builder") should be (Package("index-builder", Set(Role("index-builder")), Map.empty, "jetty-webapp", new File("/tmp/abc/packages/index-builder")))
    parsed.packages("api") should be (Package("api", Set(Role("api")), Map.empty, "jetty-webapp", new File("/tmp/abc/packages/api")))
    parsed.packages("solr") should be (Package("solr", Set(Role("solr")), Map("port" -> "8400"), "jetty-webapp", new File("/tmp/abc/packages/solr")))

    val recipes = parsed.recipes
    recipes.size should be (3)
    recipes("all") should be (Recipe("all", Nil, List("index-build-only", "api-only")))
  }

}