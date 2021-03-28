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
import java.io.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDF_IO {
    

Barcode_read_write Barcode_IO = new  Barcode_read_write();
PDDocument pdf_doc = new PDDocument();
PDPageContentStream pdf_cont_strm =null;
float a4_landscap_width = PDRectangle.A4.getHeight();
float a4_landscap_height = PDRectangle.A4.getWidth();
int code_39_width = 40, code_39_height=20;
int margin = 10;
int qr_size = 100;
int text_size = 15;
float text_ver_size= 20;

float hor_distance = (int) a4_landscap_width/3 - qr_size/3 -5;
float ver_distance = (int) a4_landscap_height/2 - qr_size/2+20;

public ArrayList <String>  case_number = new ArrayList<>();
public ArrayList <String>  evidence_number = new ArrayList<>();
public ArrayList <String>  evidence_name = new ArrayList<>();
public ArrayList <String>  request_org = new ArrayList<>();
public ArrayList <String> related_person = new ArrayList<>();

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


private void add_str_to_pdf(String input_str,  int pos_x, int pos_y){
    try{
        pdf_cont_strm.beginText();
        pdf_cont_strm.newLineAtOffset(pos_x, pos_y);
        pdf_cont_strm.showText(input_str);
        pdf_cont_strm.endText();
    }
    catch(Exception e){
        e.printStackTrace();
    }
}
private void draw_guide_lines() throws IOException{
    pdf_cont_strm.moveTo(0, a4_landscap_height-margin-code_39_height-2);
    pdf_cont_strm.lineTo(a4_landscap_width, a4_landscap_height-margin-code_39_height-2);
    pdf_cont_strm.stroke();
    pdf_cont_strm.moveTo(0, a4_landscap_height/2);
    pdf_cont_strm.lineTo(a4_landscap_width, a4_landscap_height/2);
    pdf_cont_strm.stroke();
    pdf_cont_strm.moveTo(0, margin+code_39_height+2);
    pdf_cont_strm.lineTo(a4_landscap_width, margin+code_39_height+2);
    pdf_cont_strm.stroke();
    pdf_cont_strm.moveTo(hor_distance+margin+code_39_width-2, 0);
    pdf_cont_strm.lineTo(hor_distance+margin+code_39_width-2, a4_landscap_height);
    pdf_cont_strm.stroke();
    pdf_cont_strm.moveTo(hor_distance*2+margin+code_39_width-2, 0);
    pdf_cont_strm.lineTo(hor_distance*2+margin+code_39_width-2, a4_landscap_height);
    pdf_cont_strm.stroke();
   
}

private void add_each_inf( int input_number) throws IOException{
   int remain_number = input_number % 6;
   int hor_step = remain_number % 3, ver_step = remain_number / 3 ;
   
   String comb_evi_num= case_number.get(input_number) + "-" + evidence_number.get(input_number);
   int hor_pos = (int) hor_distance*hor_step+margin+code_39_width;
   int ver_pos = (int) a4_landscap_height- (int)ver_distance*ver_step-margin-code_39_height-5;
   add_qr_img_to_pdf(comb_evi_num, qr_size, qr_size,hor_pos,ver_pos-qr_size);
   
   InputStream font_stream = new FileInputStream("C:/Windows/Fonts/Malgun.ttf");
   PDType0Font font_batang=   PDType0Font.load(pdf_doc, font_stream, true);
//   PDFont font_batang = PDType1Font.HELVETICA;  
   pdf_cont_strm.setFont(font_batang, text_size); 
   pdf_cont_strm.beginText();
   pdf_cont_strm.newLineAtOffset( hor_pos + qr_size, ver_pos-text_size);
   pdf_cont_strm.setLeading(text_ver_size);
   pdf_cont_strm.newLine();
   pdf_cont_strm.showText(request_org.get(input_number))  ;
   pdf_cont_strm.newLine();
   pdf_cont_strm.showText(comb_evi_num)  ;
   pdf_cont_strm.newLine();
   pdf_cont_strm.showText(evidence_name.get(input_number))  ;
   pdf_cont_strm.newLine();
   pdf_cont_strm.showText(related_person.get(input_number))  ;
   pdf_cont_strm.newLine();
   pdf_cont_strm.endText();
}

private void initialize_pdf_new_page(){

   PDPage pdf_page = new PDPage(new PDRectangle(a4_landscap_width,a4_landscap_height));
   
   // pdf_page.setRotation(90);
   pdf_doc.addPage(pdf_page);
   try {
   pdf_cont_strm = new PDPageContentStream(pdf_doc, pdf_page);
 
   // add QRCode 
   
   add_code_39_img_to_pdf("TL", code_39_width,  code_39_height, (int) margin ,(int) a4_landscap_height- code_39_height- margin);
   add_code_39_img_to_pdf("TR", code_39_width,  code_39_height, (int) a4_landscap_width - margin- code_39_width*2,  (int) a4_landscap_height- code_39_height- margin);
   add_code_39_img_to_pdf("BL", code_39_width,  code_39_height, (int) margin, (int) margin);
   add_code_39_img_to_pdf("BR", code_39_width,  code_39_height,(int) a4_landscap_width - margin-code_39_width*2,(int)margin);
   draw_guide_lines();

   
   } 
    catch (Exception e) {
        e.printStackTrace();
    }
  
}


 
    public void get_infor_from_each_text_file(File each_file){
     
     String Each_Line="";
            try {
  
            FileReader Text_File_Reader = new FileReader(each_file.getPath());
            BufferedReader Text_Buffered_Reader = new BufferedReader (Text_File_Reader);
            String text_title="";
            String text_info= "";
             
            while ( (Each_Line = Text_Buffered_Reader.readLine()) != null ){
                String[] Splited_Each_Line= Each_Line.split(";;");
                 text_title= Splited_Each_Line[0];
                if (Splited_Each_Line.length > 1) {
                      text_info= Splited_Each_Line[1];
                } else {
                      text_info= "";
                }
               
                if(text_title.equals("case_number") ){
                    case_number.add(text_info);
                }
                if(text_title.equals("evidence_number") ) {
                    evidence_number.add(text_info);
                }
                if(text_title.equals("evidence_name" )){
                    evidence_name.add(text_info);
                }
                if(text_title.equals("request_org")){
                    request_org.add(text_info);
                }
                if(text_title.equals("related_person")){
                    related_person.add(text_info);
                }
               
           }
       
     //       System.out.print(Column_Count_of_Image);
            Text_File_Reader.close();
            } catch (IOException e) {
                e.printStackTrace();
             }
            
    }

public void save_pdf_file(File input_file) throws IOException{
  //String myPDF = "d:/test/barcode_doc.pdf";
    pdf_doc.save(input_file.getPath());
    pdf_doc.close(); 

}
    
public  void add_qr_to_PDF(){
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

   // Closing the document  
   } catch (Exception e) {
        e.printStackTrace();
    }
  
   
   // Saving the document
   
 
 
 
   
 
}
    

}
