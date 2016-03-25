package com.utils;

import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 * Updates a keyname in all given documents within a couchdb database view
 * @author Ankur Srivastava
 *
 */
public class UpdateKey {

  public static void main(String[] args) {
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "localhost", 5984, "admin", "admin");
    // conn.context().compact();
    String src = "ErrorRowID";
    String tgt = "ProductFromBuild";
    View view = conn.view("reg/StreetAddressOrderByCC");
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    res.stream().filter(m -> ((Map) m.get("value")).containsKey(src)).map(m -> {
      // ((Map) m.get("value")).put(tgt, ((Map) m.get("value")).get(src));
      ((Map) m.get("value")).remove(src);
      return m;
    }).forEach(m -> {
      conn.update(((Map) m.get("value")));
      System.out.println(((Map) m.get("value")).get("_id"));
    });

  }
}
