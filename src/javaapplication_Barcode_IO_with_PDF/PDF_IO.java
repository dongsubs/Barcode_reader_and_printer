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
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDF_IO {
    

Barcode_read_write Barcode_IO = new  Barcode_read_write();
PDDocument pdf_doc = new PDDocument();
PDPageContentStream pdf_cont_strm =null;
float a4_landscap_width = PDRectangle.A4.getHeight();
float a4_landscap_height = PDRectangle.A4.getWidth();
int code_39_width = 40, code_39_height=20;
int margin = 50;
int qr_size = 100;
float hor_distance = (int) a4_landscap_width/3 - qr_size/3 ;
float ver_distance = (int) a4_landscap_height/2 - qr_size/2;

ArrayList <String>  case_number = new ArrayList<>();
ArrayList <String>  evidence_number = new ArrayList<>();
ArrayList <String>  evidence_name = new ArrayList<>();
ArrayList <String>  request_org = new ArrayList<>();
ArrayList <Integer> related_person = new ArrayList<>();

PDImageXObject code_39_bit_img = null, qr_bit_img=null;
    
private void add_code_39_img_to_pdf(  String input_str, int width, int height, int pos_x, int pos_y){
    try{
        BufferedImage new_qr_bit_mat = Barcode_IO.generate_code_39_buff_img(input_str, width,  height);
        PDImageXObject qr_bit_img =  JPEGFactory.createFromImage(pdf_doc, new_qr_bit_mat);
       pdf_cont_strm.drawImage(qr_bit_img, pos_x, pos_y);
    }
    catch(Exception e){
        e.printStackTrace();
    }
}

private void add_qr_img_to_pdf( String input_str, int width, int height, int pos_x, int pos_y){
    try{
        BufferedImage new_qr_bit_mat = Barcode_IO.generate_QR_Code_buff_img(input_str, width,  height);
        PDImageXObject qr_bit_img =  JPEGFactory.createFromImage(pdf_doc, new_qr_bit_mat);
       pdf_cont_strm.drawImage(qr_bit_img, pos_x, pos_y);
    }
    catch(Exception e){
        e.printStackTrace();
    }
}

private void add_each_inf( int input_number){
   int remain_number = input_number % 6;
   int hor_step = remain_number % 3, ver_step = remain_number / 3 ;

   String comb_evi_num= case_number.get(input_number); //+ "-" + evidence_number.get(input_number);
   add_qr_img_to_pdf(comb_evi_num, qr_size, qr_size,(int) hor_distance*hor_step+margin+code_39_width,(int) a4_landscap_height- (int)ver_distance*ver_step-margin-code_39_height-qr_size);
}

private void initialize_pdf_new_page(){

   PDPage pdf_page = new PDPage(new PDRectangle(a4_landscap_width,a4_landscap_height));
   
   // pdf_page.setRotation(90);
   pdf_doc.addPage(pdf_page);
   try {
   pdf_cont_strm = new PDPageContentStream(pdf_doc, pdf_page);
 
   // add QRCode 
   
   add_code_39_img_to_pdf("TL", code_39_width,  code_39_height, (int) margin ,(int) a4_landscap_height- code_39_height- margin);
   add_code_39_img_to_pdf("TR", code_39_width,  code_39_height, (int) a4_landscap_width - margin- code_39_width,  (int) a4_landscap_height- code_39_height- margin);
   add_code_39_img_to_pdf("BL", code_39_width,  code_39_height, (int) margin, (int) margin);
   add_code_39_img_to_pdf("BR", code_39_width,  code_39_height,(int) a4_landscap_width - margin-code_39_width,(int)margin);
   
   } 
    catch (Exception e) {
        e.printStackTrace();
    }
  
}

public  void add_qr_to_PDF(){
 
   // Creating PDF document object 
   case_number.add("a");
   case_number.add("b");
   case_number.add("c");
   case_number.add("d");
   case_number.add("e");
   case_number.add("f");
   case_number.add("g");
   case_number.add("h");
   case_number.add("i");
   case_number.add("j");
   String myPDF = "d:/test/barcode_doc.pdf";
   try{
        for (int current_number=0 ; current_number<case_number.size() ; current_number++){
            if (current_number% 6 ==0){
                if (current_number>0){
                    pdf_cont_strm.close();
                }
                initialize_pdf_new_page();
            }
            add_each_inf(current_number);
        }
        pdf_cont_strm.close();
        pdf_doc.save(myPDF);
   // Closing the document  
        pdf_doc.close(); 
   } catch (Exception e) {
        e.printStackTrace();
    }
  
   
   // Saving the document
   
 
 
 
   
 
}
    

}
