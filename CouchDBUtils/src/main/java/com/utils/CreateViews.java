package com.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.lightcouch.CouchDbClient;

public class CreateViews {

  /**
   * @param args
   */
  public void createView(CouchDbClient client) {
    // TODO Auto-generated method stub
    // CouchDbClient hardcodedClient = new CouchDbClient("ocp_hardcodeddata", true, "http", "127.0.0.1", 5984, "admin",
    // "admin");

    Map v1 = new HashMap();
    v1.put("_id", "_design/ocp");
    v1.put("language", "javascript");
    Map v2 = new HashMap();
    Map v3 = new HashMap();
    v3.put("map", "function(doc) {  emit(doc.Region, doc);}");
    v2.put("exec_region", v3);
    v1.put("views", v2);

    client.save(v1);

  }

  public void createMainViews() {
    // TODO Auto-generated method stub
    CouchDbClient client = new CouchDbClient("ocp_eddedr_exec", true, "http", "127.0.0.1", 5984, "admin", "admin");

    Map v1 = new HashMap();
    v1.put("_id", "_design/ocp");
    v1.put("language", "javascript");
    Map v2 = new HashMap();


    File dir = new File("src/test/resources/ocp/eddedr");
    String[] files = dir.list();
    for (String filename : files) {
      String fname = filename.substring(0, filename.indexOf("."));
      Map v3 = new HashMap();
      v3.put("map", "function(doc) {  if(doc.ScriptName=='" + fname + "'){emit(doc.ExecuteScript, doc);}}");
      v2.put(fname, v3);
    }


    v1.put("views", v2);

    client.save(v1);

  }

  public static void main(String[] args) {
    CreateViews cvs = new CreateViews();
    cvs.createMainViews();

  }
}
