package com.utils;

import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

public class GetAllDocsInView {

  public static void main(String[] args) {
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, "admin", "admin");
    View view = conn.view("reg/StreetAddressOrderByCC");
    List<Map> rows = view.query(Map.class);
    System.out.println(rows.size());
  }

}
