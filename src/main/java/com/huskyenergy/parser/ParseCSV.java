package com.huskyenergy.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.huskyenergy.pojo.BasePojo;
import com.huskyenergy.pojo.SMSPojo;

public class ParseCSV {
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    
    public static List<SMSPojo> parseCSV(String                     filename,
                                         Class<?extends BasePojo>   aClass,
                                         Boolean                    skipHeader) {
        
        Reader in = null;
        try {
            in = new FileReader(filename);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
     
        Iterable<CSVRecord> records = null;
        try {
            records = CSVFormat.RFC4180.parse(in);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        List list = new ArrayList();
        for (CSVRecord record : records) {
            
            if (skipHeader) {
                skipHeader = false;
                continue;
            }
            
            Class pojo = null;
            try {
                pojo =  Class.forName(aClass.getName());
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
          
            Method method = null;
            try {
                method = pojo.getMethod("setRecord", CSVRecord.class);
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
            
            Object pojoInstance = null;
            try {
                pojoInstance = pojo.newInstance();
            }
            catch (InstantiationException e1) {
                e1.printStackTrace();
            }
            catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            
            try {
                method.invoke(pojoInstance, record);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            
            list.add(pojoInstance);
        }
        
        
        return list;
        
    }


}
