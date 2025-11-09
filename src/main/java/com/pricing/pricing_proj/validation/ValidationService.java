package com.pricing.pricing_proj.validation;

import java.util.*;

import org.springframework.stereotype.Service;

import com.pricing.pricing_proj.model.PricingRow;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class ValidationService {    
    
    //CONTAINS A SINGLE ISSUE IN A ROW
    public static record Issue(
        int rowNumber,
        Type type,
        String message
    ) { public enum Type { MISSING_VALUE, INVALID_TRADE_DATE, DUPLICATE_GUID}}

    //CONTAINS SUMMARY OF THE WHOLE VALIDATION PROCESS AND WILL BE RERTURNED TO THE USER
    public static record Summary(
        int total,
        int valid,
        int invalid,
        int missingValues,
        int invalidDates,
        int duplicateRecords
    ) {}

    //THIS IS FOR BUNDLING AND RETURNING THE SUMMARY AND ISSUES TOGETHER
    public static record Result(
        Summary summary,
        List<Issue> issues
    ) {}

    public Result validateData(List<PricingRow> rows){
        List<Issue> issues = new ArrayList<>();
        Set<String> uniqueRecords = new HashSet<>();

        int missingValuesCount = 0; 
        int invalidDatesCount = 0; 
        int duplicateRecordsCount = 0;

        int rowNumber = 0;
        for(PricingRow r : rows){ 
            rowNumber++;

            //NORMALIZING THE STRINGS BY TRIMMING WHITESPACES
            String guid      = trimOrNull(r.instrumentGuid());
            String tradeDate = trimOrNull(r.tradeDate());
            String exchange  = trimOrNull(r.exchange());
            String ptype     = trimOrNull(r.productType());
            Double price     = r.price(); 

            //CHECKING FOR ANY MISSING VALUE ON ALL THE COLUMNS AND OTHER SPECIFIC VALIDATIONS

            //INSTRUMENT GUID: MISSING OR DUPLICATE
            if(guid == null || guid.isEmpty()){
                missingValuesCount++;
                issues.add(new Issue(rowNumber, Issue.Type.MISSING_VALUE, "Missing instrumentGuid value."));
            }
            else{
                if(!uniqueRecords.add(guid)){
                    duplicateRecordsCount++;
                    issues.add(new Issue(rowNumber, Issue.Type.DUPLICATE_GUID, "Duplicate instrumentGuid value."));
                }
            }

            //TRADE DATE: MISSING OR IF IN INVALID FORMAT    
            if(tradeDate == null || tradeDate.isEmpty()){
                missingValuesCount++;
                issues.add(new Issue(rowNumber, Issue.Type.MISSING_VALUE, "Missing tradeDate value."));
            }
            else{
                if (!isValidIsoDate(tradeDate)) {
                    issues.add(new Issue(rowNumber, Issue.Type.INVALID_TRADE_DATE, "tradeDate must be yyyy-MM-dd"));
                    invalidDatesCount++;
                }
            }

            //PRICE: MISSING
            if(price == null){ 
                missingValuesCount++;
                issues.add(new Issue(rowNumber, Issue.Type.MISSING_VALUE, "Missing price value."));
            }
            //EXCHANGE: MISSING
            if(exchange == null || r.exchange().isEmpty()){ 
                missingValuesCount++;
                issues.add(new Issue(rowNumber, Issue.Type.MISSING_VALUE, "Missing exchange value."));
            }
            //PRODUCT TYPE: MISSING
            if(ptype == null || r.productType().isEmpty()){ 
                missingValuesCount++;
                issues.add(new Issue(rowNumber, Issue.Type.MISSING_VALUE, "Missing productType value."));
            }   
        }

        int total = rows.size();
        int invalid = missingValuesCount + invalidDatesCount + duplicateRecordsCount;
        int valid = total - invalid;

        Summary summary = new Summary(
            total,
            valid,
            invalid,
            missingValuesCount,
            invalidDatesCount,
            duplicateRecordsCount
        );

        return new Result(summary, issues);
    }
    
    //THIS TRIMS A STRING OR RETURNS NULL IF THE STRING IS NULL OR EMPTY AFTER TRIMMING
    private String trimOrNull(String s) {
    if (s == null) return null;
    String t = s.trim();
    return t.isEmpty() ? null : t;
    }

    //THIS CHECKS IF THE DATE STRING IS IN VALID ISO FORMAT (yyyy-MM-dd)
    private boolean isValidIsoDate(String s) {
        if (s == null || s.isBlank()) return false;
        try {
            LocalDate.parse(s); // ISO-8601 (yyyy-MM-dd)
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
