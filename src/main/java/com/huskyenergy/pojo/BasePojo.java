package com.huskyenergy.pojo;

import static org.apache.commons.lang3.StringUtils.startsWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import org.apache.commons.csv.CSVRecord;

import com.huskyenergy.constants.Common;
import com.huskyenergy.utils.StringUtils;


public class BasePojo {

    transient static SimpleDateFormat timestampDateFormatter    = new SimpleDateFormat(Common.TIMESTAMP_FORMAT);
    transient static SimpleDateFormat db2DateFormatter          = new SimpleDateFormat(Common.DB2_DATE_FORMAT);
    
   @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Class<?> thisClass = null;
        try {
            thisClass = Class.forName(this.getClass().getName());

            Field[] aClassFields = thisClass.getDeclaredFields();
            sb.append("[");
            for(Field f : aClassFields){
                f.setAccessible(true);
                String fName = f.getName();
                
                String[] ignoreFields   = { "serialVersionUID"};
                
                boolean containsAny = StringUtils.containsAny(fName,ignoreFields);
                if (containsAny){
                    continue;
                } 
                
                if (f.getType().isAssignableFrom(java.text.SimpleDateFormat.class)) {
                    continue;
                }
                
                String fieldName = StringUtils.substringAfter(fName,"_");
                
                if (f.get(this) != null) {
                    if (f.getType().isAssignableFrom(java.util.Date.class) ) {
                        sb.append( fieldName + "=" + timestampDateFormatter.format(f.get(this)) + ", ");
                    } else if (f.getType().isAssignableFrom(java.lang.String.class) ) {
                        
                        String valueAsString = (String) f.get(this);
                        sb.append(fieldName + "=" + valueAsString + ", ");
                        
                    } else {
                        sb.append(fieldName + "=" + f.get(this) + ", ");
                    }
                }
            }
            if (sb.lastIndexOf(", ") > 0 ){
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
            sb.append("]");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public void setRecord(CSVRecord record) {

        Class<?> thisClass = null;
        try {
            thisClass = Class.forName(this.getClass().getName());

            Method[] aClassMethods = thisClass.getDeclaredMethods();
            for(Method method : aClassMethods){
                
                if (!startsWith(method.getName(), "set")) {
                    continue;
                }
              
                for (int i=0 ; i < record.size() ; i++) {
                    
                    String fieldNumberAsStr = String.valueOf(i+1);
                    if (i < 10) {
                        fieldNumberAsStr = "0".concat(fieldNumberAsStr);
                    }
                    
                    if (method.getName().indexOf( "setField".concat(fieldNumberAsStr)) >= 0)  {
                        method.invoke(this, record.get(i));
                    }
                }
               
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    
    
}
