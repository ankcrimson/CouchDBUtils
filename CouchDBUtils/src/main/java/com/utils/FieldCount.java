package com.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

public class FieldCount {

  public static void main(String[] args) {
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, "admin", "admin");
    // CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "localhost", 5984, "admin", "admin");
    View view = conn.view("reg/StreetAddressOrderByCC");
    List<Map> results = view.query(Map.class);
    Map<String, Integer> counts = new HashMap<>();
    results.stream().map(map -> (Map) map.get("value")).filter(map -> {
      return map.containsKey("Region") || map.containsKey("Country");
    }).forEach(map -> {

      String region = (map.containsKey("Region")) ? map.get("Region").toString() : map.get("Country").toString();
      if (counts.containsKey(region)) {
        int cnt = counts.get(region);
        cnt++;
        counts.put(region, cnt);
      } else {
        counts.put(region, 1);
      }

    });
    System.out.println(counts);
  }

}
