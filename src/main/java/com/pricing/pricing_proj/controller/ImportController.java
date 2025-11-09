package com.pricing.pricing_proj.controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.io.*;

import com.pricing.pricing_proj.model.PricingRow;


@RestController
public class ImportController {

    //THIS IS NOT FLEXIBLE AND WILL THROW AN ERROR IF JSON ISNT THE IN THE CORRECT FORMAT
    private final ObjectMapper om = new ObjectMapper();

    
    @GetMapping("/api/test")
    public String testing() {
        return "API is working";
    }
    
    
    //TO IMPORT THE FILE AND READ CONTENT AND ALSO FIGURE OUT IF ITS IN JSON OR CSV FORMAT
    //@PostMapping IS BASICALLY JUST HANDLING THE POST REQUEST TO THE /api/import ENDPOINT
    @PostMapping(value = "/api/import", consumes = {"multipart/form-data"})
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        
        
        byte[] data = file.getBytes();              
        String content = new String(data, java.nio.charset.StandardCharsets.UTF_8).trim();
        
        Map<String, Object> response = new HashMap<>();

        if(isJSONFormat(content))
        {
            List<PricingRow> rows = parseJSON(content);
            response.put("format", "JSON");
            response.put("size", rows.size());
            response.put("data", rows.stream().limit(5).toList());
            return response;
        }
        else
        {
            List<PricingRow> rows = parseCSV(data);
            response.put("format", "CSV");
            response.put("size", rows.size());
            response.put("data", rows.stream().limit(5).toList());
            return response;
        }
    }

    //JSON PARSING LOGIC
    private List<PricingRow> parseJSON(String content) throws Exception {
        String t = content.trim();
        //THS IS FOR PARSING ARRAYS
        if (t.startsWith("[")) {
            return om.readValue(t, new com.fasterxml.jackson.core.type.TypeReference<List<PricingRow>>() {});
        }
        //THIS IS FOR SINGLE OBJECTS AS I WAS FACING ISSUES WHENEVER THERE WAS A SINGLE OBJECT IN THE JSON FILE
        if (t.startsWith("{")) {
            PricingRow one = om.readValue(t, PricingRow.class);
            return java.util.List.of(one);
        }
        throw new IllegalArgumentException("JSON must be an object or an array.");
    }

    //THIS CHECKS IF THE CONTENT OF THE FILE IS IN JSON FORMAT
    private boolean isJSONFormat(String content) {
        content = content.trim();
        return (content.startsWith("{") || content.startsWith("["));
    }

    //CSV PARSING LOGIC 
    //THIS IS FOR HANDLING COMMAS INSIDE QUOTES IN CSV FILES 
    private String[] splitCSV(String content) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for(int i = 0; i < content.length(); i++)
        {
            char c = content.charAt(i);
            if(c == '"')
            {
                inQuotes = !inQuotes;
            }
            else if(c == ',' && !inQuotes)
            {
                out.add(cur.toString());
                cur.setLength(0);
            }
            else
            {
                cur.append(c);
            }
        }

        out.add(cur.toString());
        return out.toArray(new String[0]);
    }

    //THIS GETS THE CORRECT COLUMN FROM THE CSV ROW. NEEDED CUZ COLUMNS CAN BE IN ANY ORDER
    private String getCorrectColumn(String[] row, Map<String, Integer> idx, String col)
    {
        Integer i = idx.get(col);   
        if(i == null || i < 0 || i >= row.length)
        {
            return "";
        }
        else{
            return row[i].trim();
        }
    }
    //THIS CONVERTS STRING TO DOUBLE OR RETURNS NULL IF IT CANT BE CONVERTED
    private Double toDoubleOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        try { 
            return Double.valueOf(s.trim()); 
        } catch (Exception e) 
        { 
            return null; 
        }
    }   

    //THIS PARSES THE CSV DATA INTO PRICINGROW OBJECTS
    private List<PricingRow> parseCSV(byte[] data) throws Exception
    {
        List<PricingRow> rows = new ArrayList<>();  
        try(BufferedReader br = new BufferedReader(     //READING THE BYTE DATA AS A STREAM
            new InputStreamReader(
            new ByteArrayInputStream(data), 
            java.nio.charset.StandardCharsets.UTF_8))) 
        {
            String headerLine = br.readLine();
            if(headerLine == null)
            {
                return rows;
            }

            //MAP HEADER NAMES TO THEIR INDEXES
            String[] headers = splitCSV(headerLine);        
            Map<String, Integer> idx = new HashMap<>();     
            for(int i = 0; i < headers.length; i++)
            {
                idx.put(headers[i].trim(), i);
            }

            //READ EACH LINE AND PARSE INTO PricingRow OBJECTS
            String line; 
            while((line = br.readLine()) != null)
            {
                String[] cols = splitCSV(line);
                PricingRow row = new PricingRow(
                    getCorrectColumn(cols, idx, "instrumentGuid"),
                    getCorrectColumn(cols, idx, "tradeDate"),
                    toDoubleOrNull(getCorrectColumn(cols, idx, "price")),
                    getCorrectColumn(cols, idx, "exchange"),
                    getCorrectColumn(cols, idx, "productType")
                );
                rows.add(row);
            }
        }
        return rows;
    }
}
