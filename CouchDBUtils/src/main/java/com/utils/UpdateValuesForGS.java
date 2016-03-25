package com.utils;

import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 * Updates values of a given key/val pair inside all docs of a given couchdb view
 * @author Ankur Srivastava
 *
 */

public class UpdateValuesForGS {
  @SuppressWarnings("rawtypes")
  public static void main(String[] args) {
    String user = "asriv5";
    String password = "asriv5";
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, user, password);
    // CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "localhost", 5984, "admin", "admin");
    // conn.context().compact();

    String fieldName = "ProductFromBuild";
    String fromVal = "No";
    String toVal = "Yes";
    String lookupField = "ScriptName";
    String lookupTermStartsWith = "GS";
    View view = conn.view("reg/all_docs_1");
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    res.stream().map(map -> (Map) map.get("value")).filter(m -> m.containsKey(fieldName) && m.get(fieldName).toString().equalsIgnoreCase(fromVal)
        && m.containsKey(lookupField) && m.get(lookupField).toString().startsWith(lookupTermStartsWith)).forEach(m -> {
          // String region = ((Map) m.get("value")).get("Region").toString();
          m.put(fieldName, toVal);
          m.put("lastModifiedBy", user);
          conn.update(m);
          System.out.println("Updated - " + m.get("_id") + " - " + m.get(lookupField));
        });

  }
}
