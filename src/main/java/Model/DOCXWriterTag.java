/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

/**
 *
 * @author Gilbert Alvarado
 */
public class DOCXWriterTag {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
//    private  LocalDate start, end;
    public static boolean initializeDocumentValues(String docx_template, String output_file_AbsolutePath,
            HashMap<String, List<BMSPurchaseOrderModel>> supplier_map, String sheet_number,LocalDate start, LocalDate end) {

        // Template conversion default success
        boolean changeFlag = true;
        try {
            
            XWPFDocument document_template = null;
            if(docx_template != null){
                System.out.println("USING FILE FROM LOCAL");
                document_template = new XWPFDocument(POIXMLDocument.openPackage(docx_template));
            }
            else{
                return false;
            }
            
            File output_file = new File(output_file_AbsolutePath);

            if(DOCXWriterTag.createWordDocument
            (document_template, output_file, supplier_map, sheet_number, start, end) ){
                System.out.println("SUCCESSFULY CREATED DOCUMENT");
            }
            else{
                System.out.println("FAILED TO CREATE DOCUMENT");
            }

        } catch (IOException e) {
            e.printStackTrace();
            changeFlag = false;
        }

        return changeFlag;

    }



//##############################################################################
  private static boolean createWordDocument(XWPFDocument document_template,File output_file,
         HashMap<String, List<BMSPurchaseOrderModel>> supplier_map, String sheet_number,
         LocalDate start, LocalDate end) throws IOException{

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
    Date date = new Date();
    System.out.println(dateFormat.format(date));   
     //USED ADD HEADER INFORMATION
     //"${PO}","${DATE}","${BRG}","${PARAMETER}","${QUANTITY}","${LC}","${IP}"
     Map<String, Object> header_content = new HashMap<>();
     String []d = dateFormat.format(date).split(" ", -1);
     header_content.put("${TODAY_DATE}", d[0]);
     header_content.put("${TIME}", d[1] + " "  + d[2]);
     if(!sheet_number.trim().isEmpty())
        header_content.put("${SHEET_NUMBER}", sheet_number);
     else
         header_content.put("${SHEET_NUMBER}", "N/A");
     
     if(document_template == null)//model
         return false;
//     replace(document_template, map);//header/title
    XWPFHeaderFooterPolicy headerFooterPolicy;
    int section = 1;
    for (XWPFParagraph paragraph : document_template.getParagraphs()) {
        if (paragraph.getCTP().isSetPPr()) { //paragraph has paragraph properties set
            if (paragraph.getCTP().getPPr().isSetSectPr()) { //paragraph property has section properties set
             //headers and footers in paragraphs section properties:
             headerFooterPolicy = new XWPFHeaderFooterPolicy(document_template, paragraph.getCTP().getPPr().getSectPr());
             System.out.println("headers and footers in section properties of section " + section++ + ":");
             updatePageHeader(headerFooterPolicy,header_content);
            }   
        }
    }         
    
    Map <String, Object> dates = new HashMap<>();
    if(start!=null)
        dates.put("${START_DATE}", formatter.format(start));
    else
        dates.put("${START_DATE}", "N/A");

    if(end!=null)
        dates.put("${END_DATE}", formatter.format(end));
    else
        dates.put("${END_DATE}", "N/A");
    
    replace(document_template,dates);
    
       //ADDS ROW TEMPLATE FOR EACH VALUE
     //THEN FILLS EACH ROW TEMPLATE ACCORDING TO NUMBER OF VALUES TO ENTER
    if(extendTable(document_template,supplier_map)){
        
        if(!writeToFile(document_template,output_file)){
            return false;
        }
        if((document_template = readFromFile(output_file)) == null){
            output_file.delete();
            return false;
        }        
        if(!fillTable(document_template, supplier_map)){
            return false;
        }

     }
     
    if(!writeToFile(document_template, output_file)){//write failed
           return false;
    }

    document_template.close();
    return true;

 }
  
static void replaceHeaderContent(XWPFHeader header, Map<String, Object> header_content){

    for(XWPFTable t:header.getTables()){

        for(int i=0;i<t.getNumberOfRows();i++){

            XWPFTableRow r=t.getRow(i);
            for(Map.Entry<String, Object> entry : header_content.entrySet()){
                for(XWPFTableCell cell:r.getTableCells()){
                    for(XWPFParagraph paragraph:cell.getParagraphs()){
                        if(paragraph.getParagraphText().contains(entry.getKey())){
                            replace(paragraph, header_content);
                        }
                    }
                }
            }
        }
    }    

}
 static void updatePageHeader(XWPFHeaderFooterPolicy headerFooterPolicy, Map<String, Object> header_content) {
  XWPFHeader header;
  XWPFFooter footer;
  header = headerFooterPolicy.getDefaultHeader();
    if (header != null){
        System.out.println("DefaultHeader: " + header.getText());
        replaceHeaderContent(header,header_content);
    }
  
//  header = headerFooterPolicy.getFirstPageHeader();
//  if (header != null) System.out.println("FirstPageHeader: " + header.getText());
//  header = headerFooterPolicy.getEvenPageHeader();
//  if (header != null) System.out.println("EvenPageHeader: " + header.getText());
//  header = headerFooterPolicy.getOddPageHeader();
//  if (header != null) System.out.println("OddPageHeader: " + header.getText());
//
//  footer = headerFooterPolicy.getDefaultFooter();
//  if (footer != null) System.out.println("DefaultFooter: " + footer.getText());
//  footer = headerFooterPolicy.getFirstPageFooter();
//  if (footer != null) System.out.println("FirstPageFooter: " + footer.getText());
//  footer = headerFooterPolicy.getEvenPageFooter();
//  if (footer != null) System.out.println("EvenPageFooter: " + footer.getText());
//  footer = headerFooterPolicy.getOddPageFooter();
//  if (footer != null) System.out.println("OddPageFooter: " + footer.getText());
 }  
  
//##############################################################################
  
 private static XWPFTable getTableTemplate (List<XWPFTable> tables){
     
     for(Iterator<XWPFTable> iterator = tables.iterator();iterator.hasNext();){
         XWPFTable table = iterator.next();
         if(table.getText().contains("${tag}")){
             return table;
         }   
     }
     return null;
 }

//------------------------------------------------------------------------------
	/*
	  * Extended table
	 */
    private static boolean extendTable(XWPFDocument template_docu,
        HashMap<String, List<BMSPurchaseOrderModel>> supplier_map){
        
        XWPFTable tableTemplate;
        CTTbl cTTblTemplate;
        XWPFTable tableCopy;
        XWPFTable table = null;
        XWPFTableRow row_template;
        XmlCursor cursor;
        XWPFParagraph paragraph;
        XWPFRun run;
        CTRow ctRow = null; 

        tableTemplate = getTableTemplate(template_docu.getTables());//template_docu.getTableArray(0);

        
        cTTblTemplate = tableTemplate.getCTTbl();
        
        cursor = setCursorToNextStartToken(cTTblTemplate);
        row_template = tableTemplate.getRow(1);
        ctRow = (CTRow) row_template.getCtRow().copy();
        
        for(XWPFTableCell cell : row_template.getTableCells()){
            for(XWPFParagraph para : cell.getParagraphs()){
                if(para.getParagraphText().contains("${tag}")){
                     if(supplier_map.size()>=1){
                         Map<String, Object> map = new HashMap<>();
                         map.put("${tag}","");
                         replace(para,map);
                     }
                     break; 
                }

            }
        }
        
        int t = 0;
        for(Map.Entry<String,List<BMSPurchaseOrderModel>> entry : supplier_map.entrySet()){
            paragraph = template_docu.insertNewParagraph(cursor); //insert new empty paragraph
            cursor = setCursorToNextStartToken(paragraph.getCTP());
          //   paragraph.setStyle(heading.getStyleId());
            paragraph.setStyle("Heading1");
            
            run = paragraph.createRun();
            run.setText(entry.getKey() );
            run.setBold(true);
                    
             run = paragraph.createRun();
             run.setText(": Number of PO's: ");
             run = paragraph.createRun();
             run.setBold(true);
             run.setText(String.valueOf(entry.getValue().size()));


            table = template_docu.insertNewTbl(cursor); //insert new empty table at position t
            cursor = setCursorToNextStartToken(table.getCTTbl());

            tableCopy = new XWPFTable((CTTbl)cTTblTemplate.copy(), template_docu); //copy the template table
            
            int itemEntryNum ;
            System.out.println("NUMBER POS TO PRINT: " + entry.getValue().size());
            //DETERMINE NUMBER OF ROWS(> 1) TO ADD TO TABLE TEMPLATE
            if(entry.getValue().size() > 1){
                for(int i = 0; i < entry.getValue().size() - 2; i++){
                    itemEntryNum = tableCopy.getRows().size()-1;
                    tableCopy.addRow(new XWPFTableRow((CTRow) row_template.getCtRow().copy(), tableCopy), itemEntryNum);
                }
                XWPFTableRow newRow =new XWPFTableRow( (CTRow) ctRow.copy(), tableCopy);
                replaceTagValue(newRow, entry.getKey()); 
                tableCopy.addRow(newRow,1);
            }else{
                replaceTagValue(tableCopy.getRow(1), entry.getKey());
            }       

            
            template_docu.setTable(t+2, tableCopy);//+1 (since we're adding new tablew) //set tableCopy at position t instead of table
            
//            totalTable = template_docu.insertNewTbl(cursor); //insert new empty table at position t
//            cursor = setCursorToNextStartToken(totalTable.getCTTbl());            
//            totalTblCopy = new XWPFTable((CTTbl)cTTblTotalTemplate.copy(), template_docu); //copy the template table
//            
//            template_docu.setTable(t+1,totalTblCopy);
            paragraph = template_docu.insertNewParagraph(cursor); //insert new empty paragraph
            cursor = setCursorToNextStartToken(paragraph.getCTP());
            t++;
        }
        
        deleteOneTable(template_docu,1);//0
        return true;
    }
    private static void replaceTagValue(XWPFTableRow newRow, String newTag){
        
        for(XWPFTableCell c : newRow.getTableCells()){
            for(XWPFParagraph p : c.getParagraphs()){
                if(p.getParagraphText().contains("${tag}")){
                         Map<String, Object> map = new HashMap<>();
                         map.put("${tag}","${"+newTag+"}");
                         replace(p,map);

                     break; 
                }else if(p.getParagraphText().contains("${PO}")){
                    Map<String, Object> map = new HashMap<>();
                    map.put("${PO}","${"+newTag+"}${PO}");
                    replace(p,map);
                    break;
                }
                 else if(p.getParagraphText().contains("${LC_TOTAL}")){
                    Map<String, Object> map = new HashMap<>();
                    map.put("${PO}","${"+newTag+"}${PO}");
                    replace(p,map);
                    break;
                }           
            }
        }
    }
//##############################################################################

 
    static XmlCursor setCursorToNextStartToken(XmlObject object) {
        XmlCursor cursor = object.newCursor();
        cursor.toEndToken(); //Now we are at end of the XmlObject.
        //There always must be a next start token.
        while(cursor.hasNextToken() && cursor.toNextToken() != org.apache.xmlbeans.XmlCursor.TokenType.START);
        //Now we are at the next start token and can insert new things here.
        return cursor;
    }
    
    //##########################################################################
 private static DecimalFormat LCformat = new DecimalFormat("$###,###,##0.00");
    private static DecimalFormat IPformat = new DecimalFormat("$###,###,##0.000");    
    private static boolean fillTable(XWPFDocument template_document, 
            HashMap<String, List<BMSPurchaseOrderModel>> supplier_map){

        XWPFTable table=null;
        XWPFTableRow row=null;
        int rowPosition=0;
        XmlCursor cursor = null;
        XWPFParagraph para;

        HashMap<String, TableINFO> table_map = new HashMap<>();
        for(XWPFTable t:template_document.getTables()){
            for(String supplier : supplier_map.keySet()){
                for(int i=0;i<t.getNumberOfRows();i++){

                    XWPFTableRow r=t.getRow(i);
                    for(XWPFTableCell cell:r.getTableCells()){
                        for(XWPFParagraph paragraph:cell.getParagraphs()){
                            if(paragraph.getParagraphText().contains("${" +supplier+"}")){
                                System.out.println("FilTable: ROW " + i+  " CONTAINS ${"+supplier+"}");
                                table=t;
                                row=r;
                                rowPosition=i;
                                
                                Map<String,Object> m=new HashMap<>();
                                m.put("${"+supplier+"}", "");
                                replace(paragraph, m);
                                table_map.put(supplier,new TableINFO(supplier,t,r, i));
                                break;
                            }
                        }
                    }
                }
            }
        }

         if(table==null){//The table that needs to be expanded was not found
                 System.out.println("Failed to replace partial information");
                return false;
        }

        System.out.println("FillTable: ROW POS: " + rowPosition);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        
        for(Map.Entry<String, List<BMSPurchaseOrderModel>> entry : supplier_map.entrySet()){
            double ip = 0.0,LC = 0.0;
            for(int i=0,r=rowPosition;r<rowPosition+entry.getValue().size();r++,i++){
                     Map<String, Object> map=new HashMap<>();//Lookup table
                    map.put("${PO}", entry.getValue().get(i).getPurchase_order());
                    map.put("${DATE}", entry.getValue().get(i).getCurDateFormat());
                    map.put("${BRG}", entry.getValue().get(i).getBrg_number());
                    map.put("${PARAMETER}", entry.getValue().get(i).getParameter());
                    map.put("${QUANTITY}",entry.getValue().get(i).getQuantity());
                    map.put("${LC}",entry.getValue().get(i).getLanding_cost());
                    map.put("${IP}",String.valueOf(entry.getValue().get(i).getInvoice_price()));
                    map.put("${CONFIRMED}", entry.getValue().get(i).getConfirmed());
                    row=table_map.get(entry.getKey()).getTable().getRow(r);
                    
                    for(XWPFTableCell cell:row.getTableCells()){//per row
                            for(XWPFParagraph paragraph:cell.getParagraphs()){
                                     replace(paragraph, map);//Call the function to replace the XWPFParagraph content
                            }
                    }
                try {
                    Number number = format.parse(entry.getValue().get(i).getInvoice_price());
                    ip+= Double.parseDouble(number.toString());
                    number=format.parse(entry.getValue().get(i).getLanding_cost());
                    LC+= Double.parseDouble(number.toString());
                } catch (ParseException ex) {
                    Logger.getLogger(DOCXWriterTag.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
cursor = setCursorToNextStartToken(table_map.get(entry.getKey()).getTable().getCTTbl());
XWPFParagraph newParagraph = template_document.insertNewParagraph(cursor);
newParagraph.setAlignment(ParagraphAlignment.RIGHT);

XWPFRun run = newParagraph.createRun();
  run.setFontSize(14);
  run.setBold(true);
  run.setUnderline(UnderlinePatterns.SINGLE);
  run.setText("LC TOTAL:");
  
  run = newParagraph.createRun();
    run.addTab();
  run = newParagraph.createRun();
  run.setFontSize(14);
  run.setUnderline(UnderlinePatterns.DASH);
  run.setBold(true);
  run.setColor("85bb65");
  run.setText(LCformat.format(LC));  
  
  run = newParagraph.createRun(); 
  run.addBreak();
  run.setBold(true);
  run.setFontSize(14);
  run.setUnderline(UnderlinePatterns.SINGLE);
  run.setText("IP TOTAL:" );
  
  run = newParagraph.createRun();
  run.addTab();
  run = newParagraph.createRun();
  run.setFontSize(14);
  run.setUnderline(UnderlinePatterns.DASH);
  run.setBold(true);
  run.setColor("85bb65");
  
  run.setText(IPformat.format(ip));
  
  
  
        }
        return true;

    }
    
    //##########################################################################
        
    public static void replace(XWPFDocument document,Map<String, Object> map){

        for(XWPFParagraph paragraph:document.getParagraphs()){//traverse each XWPFParagraph in the document
                     replace(paragraph, map);//Call the function to replace the XWPFParagraph content
        }


        for(XWPFTable table:document.getTables()){//traverse the tables in the document
                for(XWPFTableRow row:table.getRows()){//traverse the rows in the table
                        for(XWPFTableCell cell:row.getTableCells()){//traverse each XWPFTableCell
                                for(XWPFParagraph paragraph:cell.getParagraphs()){//traverse each XWPFParagraph
                                        replace(paragraph, map);//Call the function to replace the XWPFParagraph content
                               }
                       }
               }
       }
    }
    //##########################################################################
    
    private static void replace(XWPFParagraph paragraph,Map<String,Object> map){

        String paragraphText=paragraph.getParagraphText();

        if(!Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE)
                         .matcher(paragraphText).find())//Determine whether the paragraph has a mark ${}
                return;
         List<Integer> indexPositions=new ArrayList<>();//The position of the mark to be replaced in XWPFParagraph
         List<String> indexKeys=new ArrayList<>();//The mark to be replaced corresponds to indexPositions
         List<String> replacements=new ArrayList<>();//The string after the tag is replaced, corresponding to indexKeys
        /*
          * First, determine the position of the mark in XWPFParagraph, mark the content and the string after the mark is replaced
         */
        for(Map.Entry<String, Object> entry:map.entrySet()){
                 String key=entry.getKey(),value=entry.getValue().toString();//Search key and its corresponding value
                int i=0;
                while((i=paragraphText.indexOf(key,i))!=-1){
                         //Find all tags
                        indexPositions.add(i);
                        indexKeys.add(key);
                        replacements.add(value);
                        i++;
                }
        }
        List<XWPFRun> runs=paragraph.getRuns();//Get all XWPFRuns of this XWPFParagraph
         List<Integer> runPositions=new ArrayList<>();//The position of each format string in XWPFParagraph
         List<XWPFRun> replaceRuns=new ArrayList<>();//XWPFRun to mark the string (first character)
        /*
          * Secondly, determine all XWPFRun of the paragraph, the position of each format string in XWPFParagraph and the XWPFRun of the mark string (first character)
         */
         for(int i=0,j=0,position=0;i<runs.size();i++){//traverse each XWPFRun
                 String runText=runs.get(i).text();//Get the string corresponding to the XWPFRun
                XWPFRun run=runs.get(i);
                runPositions.add(position);
                while(j<indexPositions.size()&&
                                indexPositions.get(j)<position+runText.length()){
                         //Mark the first character in the XWPFRun
                        replaceRuns.add(run);
                        j++;
                }
                position+=runText.length();
        }
        /*
          * Finally, replace the mark
          * If you replace from front to back, the length of the tag and the content of the replacement tag are not necessarily the same, the length of the string will change, resulting in the invalidation of the obtained position information, so choose to replace from back
          * There are 4 situations for the position of the mark in an XWPFRun:
          * 1. The mark is completely in XWPFRun;
          * 2. The mark is not in XWPFRun at all;
          * 3. Mark the front part in XWPFRun;
          * 4. Mark the back part in XWPFRun.
          * Replacement steps:
          * 1. Start from the last XWPFRun and the last mark;
          * 2. Determine whether the XWPFRun has a mark, YES: execute down, NO: clear the text content of XWPFRun, go to the previous XWPFRun, and continue to execute this step;
          * 3. Determine whether only the front part of the mark is in XWPFRun (that is, the mark is not completely in XWPFRun and the mark exists at the end of XWPFRun),
          * YES: replace the mark, if there is a previous mark, go to the previous mark and execute down, otherwise the replacement is completed, NO: execute down;
          * 4. Determine whether the mark is completely in XWPFRun, YES: replace the mark, if there is a previous mark, go to the previous mark and continue to execute this step, otherwise the replacement is complete,
          * NO: execute down;
          * 5. Determine whether only the back part of the mark is in XWPFRun (that is, the mark is not completely in XWPFRun and the mark exists in front of XWPFRun),
          * YES: Clear the marked content in XWPFRun, if there is a previous XWPFRun, go to the previous XWPFRun, go to step 2, otherwise the replacement is complete,
          * NO: If there is a previous XWPFRun, then go to the previous XWPFRun, go to step 2, otherwise the replacement is complete.
         */
        for(int i=runs.size()-1,j=replaceRuns.size()-1;i>=0&&j>=0;i--){
            String runText=runs.get(i).text();
            XWPFRun run=runs.get(i);
            int runPosition=runPositions.get(i);
            if(indexPositions.get(j)<runPosition&&
                            indexPositions.get(j)+indexKeys.get(j).length()>runPosition+runText.length()){
                    runText="";
                    run.setText(runText,0);
                    continue;
            }
            if(indexPositions.get(j)<runPosition+runText.length()&&
                            indexPositions.get(j)+indexKeys.get(j).length()>runPosition+runText.length()){
                    runText=runText.substring(0, indexPositions.get(j)-runPosition)+replacements.get(j);
                    j--;
                    if(j<0){
                            run.setText(runText,0);
                            return;
                    }
            }
            while(indexPositions.get(j)>=runPosition&&
                            indexPositions.get(j)+indexKeys.get(j).length()<=runPosition+runText.length()){
                    runText=runText.replace(indexKeys.get(j), replacements.get(j));
                    j--;
                    if(j<0){
                            run.setText(runText,0);
                            return;
                    }
            }
            if(indexPositions.get(j)+indexKeys.get(j).length()>runPosition){
                    runText=runText.substring(indexPositions.get(j)+indexKeys.get(j).length()-runPosition);
                    run.setText(runText,0);
            }
        }
    }	
        
    //##########################################################################    

    private static void deleteOneTable( XWPFDocument document, int tableIndex ) {
        try {
            int bodyElement = getBodyElementOfTable( document, tableIndex );
                System.out.println( "deleting table with bodyElement #" + bodyElement );
            document.removeBodyElement( bodyElement );
        } catch ( Exception e ) {
            System.out.println( "There is no table #" + tableIndex + " in the document." );
        }   
    }

    private static int getBodyElementOfTable( XWPFDocument document, int tableNumberInDocument ) {
        List<XWPFTable> tables = document.getTables();
        XWPFTable theTable = tables.get( tableNumberInDocument );

        return document.getPosOfTable( theTable );
    }
    
    //##########################################################################
    
    private static XWPFDocument readFromFile(File file){
		XWPFDocument document=null;
		try {
			 document = new XWPFDocument(new FileInputStream(file));//parse file
		} catch (FileNotFoundException e) {
			 System.out.println("error: File not found"+file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			 System.out.println("error:reading file"+file.getAbsolutePath()+"error while reading");
			e.printStackTrace();
		}
        return document;
	}
    
    private static boolean writeToFile(XWPFDocument document,File file){
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(file);
                document.write(outputStream);
                outputStream.close();
            } catch (IOException e) {
                             System.out.println("error:write file"+file.getAbsolutePath()+"failure");
                e.printStackTrace();
                return false;
            }
            return true;
    }
    
    static void replaceHeaderText(XWPFDocument document, String searchValue, String replacement)
    {
        List<XWPFHeader> headers = document.getHeaderList();
        for(XWPFHeader h : headers)
        {
            for (XWPFParagraph p : h.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if (text != null && text.contains(searchValue)) {
                            text = text.replace(searchValue, replacement);
                            r.setText(text, 0);
                        }
                    }
                }
            }
            for (XWPFTable tbl : h.getTables()) {
                for (XWPFTableRow row : tbl.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            for (XWPFRun r : p.getRuns()) {
                                String text = r.getText(0);
                                if (text != null && text.contains(searchValue)) {
                                    text = text.replace(searchValue, replacement);
                                    r.setText(text,0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class TableINFO{
    private String supplier;
    private XWPFTable table;
    private XWPFTableRow row;
    private int row_position;
    
    public TableINFO(String supplier,  XWPFTable table, XWPFTableRow row, int row_position) {
        this.supplier = supplier;
        this.table = table;
        this.row = row;
        this.row_position = row_position;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public XWPFTable getTable() {
        return table;
    }

    public void setTable(XWPFTable table) {
        this.table = table;
    }

    public XWPFTableRow getRow() {
        return row;
    }

    public void setRow(XWPFTableRow row) {
        this.row = row;
    }

    public int getRow_position() {
        return row_position;
    }

    public void setRow_position(int row_position) {
        this.row_position = row_position;
    }
}
