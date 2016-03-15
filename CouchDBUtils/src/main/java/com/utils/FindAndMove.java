package com.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 * Finds a test data row and moves it to another database
 * @author asriv5
 *
 */
public class FindAndMove {

  public static void main(String[] args) {
    String srcDB = "ocp_runscripts";
    String tgtDB = "ocp_reference_data";
    String lookupDB = "failedrows";
    String lookupViewName = "ocp/id_doc";
    String lookupField = "Result";
    String lookupFilterVal = "Pass";
    String referenceFieldName = "TestID";
    CouchDbClient srcConn = new CouchDbClient(srcDB, true, "http", "10.216.138.222", 5984, "admin", "admin");
    CouchDbClient tgtConn = new CouchDbClient(tgtDB, true, "http", "10.216.138.222", 5984, "admin", "admin");
    CouchDbClient lookupConn = new CouchDbClient(lookupDB, true, "http", "10.216.138.222", 5984, "admin", "admin");
    View lookupView = lookupConn.view(lookupViewName);
    List<Map> lookupRows = lookupView.query(Map.class);
    List<String> idsToMove = new ArrayList<>();
    lookupRows.stream().map(row -> (Map) row.get("value")).filter(row -> row.containsKey(lookupField) && row.get(lookupField).equals(lookupFilterVal))
        .forEach(row -> idsToMove.add(row.get(referenceFieldName).toString()));
    System.out.println(idsToMove.size());
    System.out.println(idsToMove);
    idsToMove.stream().map(id -> {
      Map doc = srcConn.find(Map.class, id);
      doc.put("MovedBy", "Code");
      if (doc.containsKey("_rev"))
        doc.remove("_rev");
      return doc;
    }).forEach(doc -> {
      System.out.println(doc.get("_id"));
      try {
        tgtConn.save(doc);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
  }

}
