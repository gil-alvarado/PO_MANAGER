/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;




import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gilbert Alvarado
 */
public class DOCXHandler {
    
    public static boolean generateReport( String output_file_AbsolutePath, 
            HashMap<String, List<BMSPurchaseOrderModel>> supplier_map,
            String sheet_number, LocalDate start, LocalDate end){
        
        System.out.println("----------\nDOCXHandler\n---------------\nORGANIZED DATA\n----------------");
        for(Map.Entry<String, List<BMSPurchaseOrderModel>> entry : supplier_map.entrySet()){
                System.out.println("SUPPLIER: " + entry.getKey());
                System.out.println(entry.getValue());
           }
        //----------------------------------------------------------------------
        try {

            String file_template = null;
            if(DOCXHandler.class.getResource("/Templates/template_TAG.docx") != null){
                file_template = DOCXHandler.class.getResource("/Templates/template_TAG.docx").getPath();

                System.out.println("USING TEMPLATE FROM LOCAL PROJECT\n" + file_template);

            }else{
                file_template = FileHelper.getTemplateExternalSource();
                System.out.println("USING FILE FROM EXTERNAL SOURCE");
            }
        
            if( DOCXWriterTag.initializeDocumentValues( file_template, output_file_AbsolutePath, supplier_map,
                    sheet_number,start, end ) ){
                System.out.println("CREATE DIALOG FOR SUCCESS");
                return true;
            }else
                return false;
            
        } catch (Exception ex) {
            Logger.getLogger(DOCXHandler.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        return false;
    }
    
}