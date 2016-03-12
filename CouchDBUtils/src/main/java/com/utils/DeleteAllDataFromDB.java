package com.utils;

import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 * Deletes all data rows from DB view
 * @author asriv5
 *
 */
public class DeleteAllDataFromDB {
  public static void main(String[] args) {
    String viewName = "ocp/id_doc";
    CouchDbClient conn = new CouchDbClient("failedrows", true, "http", "10.216.138.222", 5984, "admin", "admin");
    // CouchDbClient conn = new CouchDbClient("ocp_newlog", true, "http", "localhost", 5984, "admin", "admin");
    View view = conn.view(viewName);
    List<Map> docs = view.query(Map.class);
    docs.stream().map(map -> map.get("value")).forEach(doc -> conn.remove(doc));
    conn.context().compact();
  }
}
