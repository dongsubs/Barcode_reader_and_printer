/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication_Barcode_IO_with_PDF;

/**
 *
 * @author LDS
 */
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
 
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDF_IO {
    

Barcode_read_write Barcode_IO = new  Barcode_read_write();
    
public  void add_qr_to_PDF(){
    
    try {
 
   // Creating PDF document object 
   String myPDF = "docs/barcode_doc.pdf";
   PDDocument pdf_doc = new PDDocument();
   PDPage pdf_page = new PDPage(PDRectangle.A4);
   pdf_doc.addPage(pdf_page);
   PDPageContentStream pdf_cont_strm = new PDPageContentStream(pdf_doc, pdf_page);
   
   float margin = 25;
   float y = 700;
   
   // add QRCode 
  
   BufferedImage new_qr_bit_mat = Barcode_IO.generate_QR_Code_buff_img("https://www.mysamplecode.com",(int) margin + 300, (int) y);
   
   PDImageXObject bit_img =  JPEGFactory.createFromImage(pdf_doc, new_qr_bit_mat);
   pdf_cont_strm.drawImage(bit_img, 325, y);
  
   // Saving the document
   pdf_doc.save(myPDF);
   // Closing the document  
   pdf_doc.close();
 
 
  } 
  catch (Exception e) {
   e.printStackTrace();
  }
 
}
    

}
