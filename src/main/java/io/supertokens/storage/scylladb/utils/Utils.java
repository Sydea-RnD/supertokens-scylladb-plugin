package io.supertokens.storage.scylladb.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Utils {
    public static String exceptionStacktraceToString(Exception e) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();

    }
    
    public static String getIndexNameFromTableName(String schema, String tableName, String column, String suffix) {
        
        StringBuilder indexName = new StringBuilder(tableName);
        
        if(tableName.startsWith(schema) == true) {
            // We also have to delete the . after the schema name
            indexName.delete(0, schema.length() + 1);
        }
        
        indexName.append("_").append(suffix);
        
        return indexName.toString();
    }
}