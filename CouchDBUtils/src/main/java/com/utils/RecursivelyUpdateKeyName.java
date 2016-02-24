package com.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

public class RecursivelyUpdateKeyName {

  public static void recursiveEdit(Map map, List list, String from, String to) {
    if (list != null) {
      for (Object obj : list) {
        if (obj instanceof Map) {
          Map currObj = (Map) obj;
          recursiveEdit(currObj, null, from, to);
        }
      }
      return;
    }
    if (map.containsKey(from)) {
      Object data = map.get(from);
      map.remove(from);
      map.put(to, data);
    }
    Set<String> keys = map.keySet();
    for (String key : keys) {

      if (map.get(key) instanceof Map) {
        Map currObj = (Map) map.get(key);
        recursiveEdit(currObj, null, from, to);
      } else if (map.get(key) instanceof List) {
        List currObj = (List) map.get(key);
        recursiveEdit(map, currObj, from, to);
      }
    }

  }

  public static void main(String[] args) {
    System.out.println(Base64.class.getProtectionDomain().getCodeSource().getLocation());
    String from = "ValidateEDD";
    String to = "ValidateEDDEDR";
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "localhost", 5984, "admin", "admin");
    // CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, "admin", "admin");
    View view = conn.view("reg/all_docs_1");
    List<Map> res = view.query(Map.class);
    System.out.println("Total Number of docs to scan: " + res.size());
    for (Map viewDataRow : res) {
      Map dataRow = (Map) viewDataRow.get("value");
      System.out.println(dataRow);
      recursiveEdit(dataRow, null, from, to);
      conn.update(dataRow);
    }
  }

}
