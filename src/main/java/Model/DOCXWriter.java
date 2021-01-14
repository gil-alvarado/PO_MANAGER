/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.apache.poi.ss.usermodel.Row;


//import java.io.File;

//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;




import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


//##############################################################################

import org.apache.poi.wp.usermodel.HeaderFooterType;
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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;


//##############################################################################

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

/**
 *
 * @author Gilbert Alvarado
 */
public class DOCXWriter {
    
    
    private static final String[] days = {
            "Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday"};

    private static final String[]  months = {
            "January", "February", "March","April", "May", "June","July", "August",
            "September","October", "November", "December"};
    
    
    public static void creatDoc(String purchase_order) throws Exception{
        
//        System.out.println("DOC TEST");
//        
//        String path = "C:\\Users\\gilbe\\Documents\\Custom Office Templates\\test.docx";
//        
//        POIFSFileSystem fs = null;        
//        try {            
//            fs = new POIFSFileSystem(new FileInputStream(path));            
//            HWPFDocument doc = new HWPFDocument(fs);
//            doc = replaceText(doc, "$name", "Gil");
//            saveWord(path, doc);
//        }
//        catch(FileNotFoundException e){
//            e.printStackTrace();
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        }

//##############################################################################

//XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet("Java Books");
//         
//        Object[][] bookData = {
//                {"Head First Java", "Kathy Serria", 79},
//                {"Effective Java", "Joshua Bloch", 36},
//                {"Clean Code", "Robert martin", 42},
//                {"Thinking in Java", "Bruce Eckel", 35},
//        };
// 
//        int rowCount = 0;
//         
//        for (Object[] aBook : bookData) {
//            Row row = sheet.createRow(++rowCount);
//             
//            int columnCount = 0;
//             
//            for (Object field : aBook) {
//                Cell cell = row.createCell(++columnCount);
//                if (field instanceof String) {
//                    cell.setCellValue((String) field);
//                } else if (field instanceof Integer) {
//                    cell.setCellValue((Integer) field);
//                }
//            }
//             
//        }
//         
//         
//        try (FileOutputStream outputStream = new FileOutputStream("C:\\Users\\gilbe\\Documents\\Vie\\FilesTest\\JavaBooks.xlsx")) {
//            workbook.write(outputStream);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
  

//##############################################################################
//                  CAMENDAR EXAMPLE
//##############################################################################

/*
Calendar calendar = LocaleUtil.getLocaleCalendar();
        if(args.length > 0) calendar.set(Calendar.YEAR, Integer.parseInt(args[0]));

        int year = calendar.get(Calendar.YEAR);

        System.out.println("ATTEMPTING TO WRITE FILE");
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Map<String, XSSFCellStyle> styles = createStyles(wb);

            for (int month = 0; month < 12; month++) {
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                //create a sheet for each month
                XSSFSheet sheet = wb.createSheet(months[month]);

                //turn off gridlines
                sheet.setDisplayGridlines(false);
                sheet.setPrintGridlines(false);
                XSSFPrintSetup printSetup = sheet.getPrintSetup();
                printSetup.setOrientation(PrintOrientation.LANDSCAPE);
                sheet.setFitToPage(true);
                sheet.setHorizontallyCenter(true);

                //the header row: centered text in 48pt font
                XSSFRow headerRow = sheet.createRow(0);
                headerRow.setHeightInPoints(80);
                XSSFCell titleCell = headerRow.createCell(0);
                titleCell.setCellValue(months[month] + " " + year);
                titleCell.setCellStyle(styles.get("title"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$N$1"));

                //header with month titles
                XSSFRow monthRow = sheet.createRow(1);
                for (int i = 0; i < days.length; i++) {
                    //for compatibility with HSSF we have to set column width in units of 1/256th of a character width
                    sheet.setColumnWidth(i * 2, 5 * 256); //the column is 5 characters wide
                    sheet.setColumnWidth(i * 2 + 1, 13 * 256); //the column is 13 characters wide
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, i * 2, i * 2 + 1));
                    XSSFCell monthCell = monthRow.createCell(i * 2);
                    monthCell.setCellValue(days[i]);
                    monthCell.setCellStyle(styles.get("month"));
                }

                int cnt = 1, day = 1;
                int rownum = 2;
                for (int j = 0; j < 6; j++) {
                    XSSFRow row = sheet.createRow(rownum++);
                    row.setHeightInPoints(100);
                    for (int i = 0; i < days.length; i++) {
                        XSSFCell dayCell_1 = row.createCell(i * 2);
                        XSSFCell dayCell_2 = row.createCell(i * 2 + 1);

                        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                        if (cnt >= day_of_week && calendar.get(Calendar.MONTH) == month) {
                            dayCell_1.setCellValue(day);
                            calendar.set(Calendar.DAY_OF_MONTH, ++day);

                            if (i == 0 || i == days.length - 1) {
                                dayCell_1.setCellStyle(styles.get("weekend_left"));
                                dayCell_2.setCellStyle(styles.get("weekend_right"));
                            } else {
                                dayCell_1.setCellStyle(styles.get("workday_left"));
                                dayCell_2.setCellStyle(styles.get("workday_right"));
                            }
                        } else {
                            dayCell_1.setCellStyle(styles.get("grey_left"));
                            dayCell_2.setCellStyle(styles.get("grey_right"));
                        }
                        cnt++;
                    }
                    if (calendar.get(Calendar.MONTH) > month) break;
                }
            }

            // Write the output to a file
            try (FileOutputStream out = new FileOutputStream("C:\\Users\\gilbe\\Documents\\Vie\\FilesTest\\calendar"+year+".xlsx")) {
                System.out.println("WRITING TO FILE");
                wb.write(out);
            } catch (FileNotFoundException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
//##############################################################################
//                  END CALENDAR EXAMPLE
//------------------------------------------------------------------------------
//                  XWPF/WORD DOCX EXAMPLE
//##############################################################################
        try (XWPFDocument doc = new XWPFDocument()) {

            // Create a header with a 1 row, 3 column table
            // changes made for issue 57366 allow a new header or footer
            // to be created empty. This is a change. You will have to add
            // either a paragraph or a table to the header or footer for
            // the document to be considered valid.
            XWPFHeader hdr = doc.createHeader(HeaderFooterType.DEFAULT);
            XWPFTable tbl = hdr.createTable(1, 3);

            // Set the padding around text in the cells to 1/10th of an inch
            int pad = (int) (.1 * 1440);
            tbl.setCellMargins(pad, pad, pad, pad);

            // Set table width to 6.5 inches in 1440ths of a point
            tbl.setWidth((int) (6.5 * 1440));
            
            // Can not yet set table or cell width properly, tables default to
            // autofit layout, and this requires fixed layout
            CTTbl ctTbl = tbl.getCTTbl();
            CTTblPr ctTblPr = ctTbl.addNewTblPr();
            CTTblLayoutType layoutType = ctTblPr.addNewTblLayout();
            layoutType.setType(STTblLayoutType.FIXED);

            // Now set up a grid for the table, cells will fit into the grid
            // Each cell width is 3120 in 1440ths of an inch, or 1/3rd of 6.5"
            BigInteger w = new BigInteger("3120");
            CTTblGrid grid = ctTbl.addNewTblGrid();
            for (int i = 0; i < 3; i++) {
                CTTblGridCol gridCol = grid.addNewGridCol();
                gridCol.setW(w);
            }

            
            // Add paragraphs to the cells
            XWPFTableRow row = tbl.getRow(0);
            XWPFTableCell cell = row.getCell(0);
            XWPFParagraph p = cell.getParagraphArray(0);
//##############################################################################            
            //ADDED CODE
//##############################################################################            
            p.setAlignment(ParagraphAlignment.CENTER);
//------------------------------------------------------------------------------            
            XWPFRun r = p.createRun();
            r.setText("BMS REPORT");

            cell = row.getCell(1);
            p = cell.getParagraphArray(0);
//##############################################################################            
            //ADDED CODE
//##############################################################################               
            p.setAlignment(ParagraphAlignment.CENTER);
//------------------------------------------------------------------------------            
            r = p.createRun();
            r.setText("center");

            cell = row.getCell(2);
            p = cell.getParagraphArray(0);
            r = p.createRun();
            r.setText(new Date().toString());

            // Create a footer with a Paragraph
            XWPFFooter ftr = doc.createFooter(HeaderFooterType.DEFAULT);
            p = ftr.createParagraph();

            r = p.createRun();
            r.setText(new Date().toString());

            try (OutputStream os = new FileOutputStream(new File("C:\\Users\\gilbe\\Documents\\Vie\\FilesTest\\headertable.docx"))) {
                doc.write(os);
            } catch (IOException ex) {
                Logger.getLogger(DOCXWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(DOCXWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//##############################################################################
//                  END DOCX EXAMPLE
//------------------------------------------------------------------------------
//                  SIMPLE TABLE EXAMPLE
//##############################################################################
        try {
    		createSimpleTable();
    	}
    	catch(Exception e) {
    		System.out.println("Error trying to create simple table.");
    		throw(e);
    	}
    	try {
    		createStyledTable();
    	}
    	catch(Exception e) {
    		System.out.println("Error trying to create styled table.");
    		throw(e);
    	}
    }
    
    //##########################################################################
    //          end main
    //##########################################################################
    
    private static HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText){
        Range r1 = doc.getRange(); 

        for (int i = 0; i < r1.numSections(); ++i ) { 
            Section s = r1.getSection(i); 
            for (int x = 0; x < s.numParagraphs(); x++) { 
                Paragraph p = s.getParagraph(x); 
                for (int z = 0; z < p.numCharacterRuns(); z++) { 
                    CharacterRun run = p.getCharacterRun(z); 
                    String text = run.text();
                    if(text.contains(findText)) {
                        run.replaceText(findText, replaceText);
                    } 
                }
            }
        } 
        return doc;
    }

    private static void saveWord(String filePath, HWPFDocument doc) throws FileNotFoundException, IOException{
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(filePath);
            doc.write(out);
        }
        finally{
            out.close();
        }
    }
    
    
    /**
     * cell styles used for formatting calendar sheets
     */
    private static Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb){
        Map<String, XSSFCellStyle> styles = new HashMap<>();

        XSSFCellStyle style;
        XSSFFont titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)48);
        titleFont.setColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        XSSFFont monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short)12);
        monthFont.setColor(new XSSFColor(new java.awt.Color(255, 255, 255), wb.getStylesSource().getIndexedColors()));
        monthFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        styles.put("month", style);

        XSSFFont dayFont = wb.createFont();
        dayFont.setFontHeightInPoints((short)14);
        dayFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(228, 232, 243), wb.getStylesSource().getIndexedColors()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        style.setFont(dayFont);
        styles.put("weekend_left", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(228, 232, 243), wb.getStylesSource().getIndexedColors()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        styles.put("weekend_right", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255), wb.getStylesSource().getIndexedColors()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setLeftBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        style.setFont(dayFont);
        styles.put("workday_left", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255), wb.getStylesSource().getIndexedColors()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        styles.put("workday_right", style);

        style = wb.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(234, 234, 234), wb.getStylesSource().getIndexedColors()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        styles.put("grey_left", style);

        style = wb.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(234, 234, 234), wb.getStylesSource().getIndexedColors()));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new java.awt.Color(39, 51, 89), wb.getStylesSource().getIndexedColors()));
        styles.put("grey_right", style);

        return styles;
    }
    
//##############################################################################
//                  SIMPLE TABLE METHODS
//##############################################################################
    public static void createSimpleTable() throws Exception {
        try (XWPFDocument doc = new XWPFDocument()) {
            
            XWPFTable table = doc.createTable(3, 3);

            table.getRow(1).getCell(1).setText("EXAMPLE OF TABLE");
            
            
            
            // table cells have a list of paragraphs; there is an initial
            // paragraph created when the cell is created. If you create a
            // paragraph in the document to put in the cell, it will also
            // appear in the document following the table, which is probably
            // not the desired result.
            XWPFParagraph p1 = table.getRow(0).getCell(0).getParagraphs().get(0);

            p1.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun r1 = p1.createRun();
            
            r1.setBold(true);
            r1.setText("The quick brown fox");
            r1.setItalic(true);
            r1.setFontFamily("Courier");
            r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
            r1.setTextPosition(100);

            table.getRow(2).getCell(2).setText("only text");

            try (OutputStream out = new FileOutputStream("C:\\Users\\gilbe\\Documents\\Vie\\FilesTest\\simpleTable.docx")) {
                doc.write(out);
            }
        }
    }

    /**
     * Create a table with some row and column styling. I "manually" add the
     * style name to the table, but don't check to see if the style actually
     * exists in the document. Since I'm creating it from scratch, it obviously
     * won't exist. When opened in MS Word, the table style becomes "Normal".
     * I manually set alternating row colors. This could be done using Themes,
     * but that's left as an exercise for the reader. The cells in the last
     * column of the table have 10pt. "Courier" font.
     * I make no claims that this is the "right" way to do it, but it worked
     * for me. Given the scarcity of XWPF examples, I thought this may prove
     * instructive and give you ideas for your own solutions.
     */
    public static void createStyledTable() throws Exception {
    	// Create a new document from scratch

        try (XWPFDocument doc = new XWPFDocument()) {
            
            
//##############################################################################
//              ADDED HEADER
//##############################################################################
            
            XWPFHeader hdr = doc.createHeader(HeaderFooterType.DEFAULT);
            XWPFTable tbl = hdr.createTable(1, 3);

            // Set the padding around text in the cells to 1/10th of an inch
            int pad = (int) (.1 * 1440);
            tbl.setCellMargins(pad, pad, pad, pad);

            // Set table width to 6.5 inches in 1440ths of a point
            tbl.setWidth((int) (6.5 * 1440));
            
            // Can not yet set table or cell width properly, tables default to
            // autofit layout, and this requires fixed layout
            CTTbl ctTbl = tbl.getCTTbl();
            CTTblPr ctTblPr = ctTbl.addNewTblPr();
            CTTblLayoutType layoutType = ctTblPr.addNewTblLayout();
            layoutType.setType(STTblLayoutType.FIXED);

            // Now set up a grid for the table, cells will fit into the grid
            // Each cell width is 3120 in 1440ths of an inch, or 1/3rd of 6.5"
            BigInteger w = new BigInteger("3120");
            CTTblGrid grid = ctTbl.addNewTblGrid();
            for (int i = 0; i < 3; i++) {
                CTTblGridCol gridCol = grid.addNewGridCol();
                gridCol.setW(w);
            }

            
            // Add paragraphs to the cells
            XWPFTableRow xwpfRow = tbl.getRow(0);
            XWPFTableCell xwpfCell = xwpfRow.getCell(0);
            XWPFParagraph p = xwpfCell.getParagraphArray(0);
//##############################################################################            
            //ADDED CODE
//##############################################################################            
            p.setAlignment(ParagraphAlignment.CENTER);
//------------------------------------------------------------------------------            
            XWPFRun r = p.createRun();
            r.setText("BMS REPORT");

            xwpfCell = xwpfRow.getCell(1);
            p = xwpfCell.getParagraphArray(0);
//##############################################################################            
            //ADDED CODE
//##############################################################################               
            p.setAlignment(ParagraphAlignment.CENTER);
//------------------------------------------------------------------------------            
            r = p.createRun();
            r.setText("center");

            xwpfCell = xwpfRow.getCell(2);
            p = xwpfCell.getParagraphArray(0);
            r = p.createRun();
            r.setText(new Date().toString());

            // Create a footer with a Paragraph
            XWPFFooter ftr = doc.createFooter(HeaderFooterType.DEFAULT);
            p = ftr.createParagraph();

            r = p.createRun();
            r.setText(new Date().toString());
            
            
//##############################################################################            
//                  END HEADER
////##############################################################################

            // -- OR --
            // open an existing empty document with styles already defined
            //XWPFDocument doc = new XWPFDocument(new FileInputStream("base_document.docx"));

            // Create a new table with 6 rows and 3 columns
            int nRows = 6;
            int nCols = 3;
            XWPFTable table = doc.createTable(nRows, nCols);

//##############################################################################
//              ADDED CODE: ALIGN TABLE
//##############################################################################
            setTableAlign(table,ParagraphAlignment.CENTER);

            // Set the table style. If the style is not defined, the table style
            // will become "Normal".
            CTTblPr tblPr = table.getCTTbl().getTblPr();
            CTString styleStr = tblPr.addNewTblStyle();
            styleStr.setVal("StyledTable");

            // Get a list of the rows in the table
            List<XWPFTableRow> rows = table.getRows();
            int rowCt = 0;
            int colCt = 0;
            for (XWPFTableRow row : rows) {
                // get table row properties (trPr)
                CTTrPr trPr = row.getCtRow().addNewTrPr();
                // set row height; units = twentieth of a point, 360 = 0.25"
                CTHeight ht = trPr.addNewTrHeight();
                ht.setVal(BigInteger.valueOf(1000));

                // get the cells in this row
                List<XWPFTableCell> cells = row.getTableCells();
                // add content to each cell
                for (XWPFTableCell cell : cells) {
                    // get a table cell properties element (tcPr)
                    CTTcPr tcpr = cell.getCTTc().addNewTcPr();
                    // set vertical alignment to "center"
                    CTVerticalJc va = tcpr.addNewVAlign();
                    va.setVal(STVerticalJc.CENTER);

                    // create cell color element
                    CTShd ctshd = tcpr.addNewShd();
                    ctshd.setColor("auto");
                    ctshd.setVal(STShd.CLEAR);
                    if (rowCt == 0) {
                        // header row
                        ctshd.setFill("A7BFDE");
                    } else if (rowCt % 2 == 0) {
                        // even row
                        ctshd.setFill("D3DFEE");
                    } else {
                        // odd row
                        ctshd.setFill("EDF2F8");
                    }

                    // get 1st paragraph in cell's paragraph list
                    XWPFParagraph para = cell.getParagraphs().get(0);
                    
                    
                    
                    // create a run to contain the content
                    XWPFRun rh = para.createRun();
                    // style cell as desired
                    if (colCt == nCols - 1) {
                        // last column is 10pt Courier
                        rh.setFontSize(10);
                        rh.setFontFamily("Courier");
                    }
                    if (rowCt == 0) {
                        // header row
                        rh.setText("header row, col " + colCt);
                        rh.setBold(true);
                        para.setAlignment(ParagraphAlignment.CENTER);
                    } else {
                        // other rows
                        rh.setText("row " + rowCt + ", col " + colCt);
                        para.setAlignment(ParagraphAlignment.CENTER);
                    }
                    colCt++;
                } // for cell
                colCt = 0;
                rowCt++;
            } // for row

            // write the file
            try (OutputStream out = new FileOutputStream("C:\\Users\\gilbe\\Documents\\Vie\\FilesTest\\styledTable.docx")) {
                doc.write(out);
            }
        }
    }
    
//##############################################################################
//              ADDED CODE: TABLE ALIGNMENT
//############################################################################## 
    private static void setTableAlign(XWPFTable table,ParagraphAlignment align) {
        
        table.setWidth("100%");
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTJc jc = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
        STJc.Enum en = STJc.Enum.forInt(align.getValue());
        jc.setVal(en);
    }
}
