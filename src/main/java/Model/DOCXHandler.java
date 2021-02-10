/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;




import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;



//import org.docx4j.dml.wordprocessingDrawing.Inline;
//import org.docx4j.jaxb.Context;
//import org.docx4j.model.table.TblFactory;
//import org.docx4j.openpackaging.exceptions.Docx4JException;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
//import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
//import org.docx4j.wml.*;
//
//import javax.xml.bind.JAXBElement;
//import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
//import org.docx4j.XmlUtils;


/**
 *
 * @author Gilbert Alvarado
 */
public class DOCXHandler {    
    
    public static void generateRport( String output_file_AbsolutePath, 
            HashMap<String, List<BMSPurchaseOrderModel>> supplier_map){
        
        System.out.println("----------\nDOCXHandler\n---------------\nORGANIZED DATA\n----------------");
        for(Map.Entry<String, List<BMSPurchaseOrderModel>> entry : supplier_map.entrySet()){
                System.out.println("SUPPLIER: " + entry.getKey());
                System.out.println(entry.getValue());
           }
        
            createDoc(output_file_AbsolutePath, supplier_map);

    }
    
    private static void createDoc(String selectedAbsolutePath,
            HashMap<String, List<BMSPurchaseOrderModel>> supplier_map){
        try {
//            DOCXWriter_TEST.createStyledTable(DOCXHandler.data, fileName);
//        String inputUrl = "C:\\Users\\gilbe\\Desktop\\BMStemp\\tempEmailDir\\template_poi.docx";
        
//        String inputUrl = "C:\\Users\\gilbe\\Desktop\\BMStemp\\tempEmailDir\\template_test_TAG.docx";
//        String templateUrl = ConnectionUtil.dbDirectoryLocation() + "\\template_test_TAG.docx";
//        String inputUrl = "C:\\Users\\gilbe\\Desktop\\BMStemp\\tempEmailDir\\template_test2.docx";
//        String inputUrl = "C:\\Users\\gilbe\\Desktop\\BMStemp\\tempEmailDir\\template_test3.docx";
        
        // Newly produced template file
//        String outputUrl = FileHelper.getTempEmailDirectoryLocation()+"\\report_inventory_output.docx";

        
        
        DOCXWriterTag.initializeDocumentValues(FileHelper.getTemplateDirectory(), selectedAbsolutePath, supplier_map );
        
        } catch (Exception ex) {
            Logger.getLogger(DOCXHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}