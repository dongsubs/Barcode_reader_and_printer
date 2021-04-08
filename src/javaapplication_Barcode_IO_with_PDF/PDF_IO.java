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
import org.apache.pdfbox.tools.PrintPDF;


public class PDF_IO {
    

Barcode_read_write Barcode_IO = new  Barcode_read_write();

PDPageContentStream pdf_cont_strm ;

public float a4_landscap_width = PDRectangle.A4.getHeight();
public float a4_landscap_height = PDRectangle.A4.getWidth();
public int code_39_width = 60, code_39_height=30;
public int margin = 10;

public int qr_size = 80;
public int font_size = 12;
public int Column_Count_of_Image=3;
public int Row_Count_of_Image=2;

public float text_ver_size= font_size;

float hor_distance = (int) a4_landscap_width/Column_Count_of_Image;
float ver_distance = (int) (a4_landscap_height-margin-code_39_height)/Row_Count_of_Image;

public ArrayList <String>  case_number = new ArrayList<>();
public ArrayList <String>  evidence_name = new ArrayList<>();
public ArrayList <String>  request_org = new ArrayList<>();
public ArrayList <String> related_person = new ArrayList<>();

PDImageXObject code_39_bit_img = null, qr_bit_img=null;

InputStream font_stream ;
PDType0Font font_batang ;
 PDDocument pdf_doc;
 
private void add_code_39_img_to_pdf(PDDocument input_pdf,  String input_str, int width, int height, int pos_x, int pos_y){
    try{
        BufferedImage new_qr_bit_mat = Barcode_IO.generate_code_39_buff_img(input_str, width,  height);
        PDImageXObject qr_bit_img =  JPEGFactory.createFromImage(input_pdf, new_qr_bit_mat);
        pdf_cont_strm.drawImage(qr_bit_img, pos_x, pos_y);
    }
    catch(Exception e){
        e.printStackTrace();
    }
}

private void add_qr_img_to_pdf(PDDocument input_pdf, String input_str, int width, int height, int pos_x, int pos_y){
    try{
        BufferedImage new_qr_bit_mat = Barcode_IO.generate_QR_Code_buff_img(input_str, width,  height);
        PDImageXObject qr_bit_img =  JPEGFactory.createFromImage(input_pdf, new_qr_bit_mat);
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
    pdf_cont_strm.moveTo(0, margin+code_39_height+Row_Count_of_Image);
    pdf_cont_strm.lineTo(a4_landscap_width, margin+code_39_height+Row_Count_of_Image);
    pdf_cont_strm.stroke();
    
    for (int horizontal_line_step=1;horizontal_line_step<Row_Count_of_Image;horizontal_line_step++){
        pdf_cont_strm.moveTo(0, a4_landscap_height-margin-code_39_height- ver_distance*horizontal_line_step);
        pdf_cont_strm.lineTo(a4_landscap_width,  a4_landscap_height-margin-code_39_height- ver_distance*horizontal_line_step); //a4_landscap_height/Row_Count_of_Image*horizontal_line_step);
        pdf_cont_strm.stroke();
    }
    
    for (int vertical_line_step=1;vertical_line_step<Column_Count_of_Image;vertical_line_step++){
        pdf_cont_strm.moveTo(margin+hor_distance*vertical_line_step-10, 0);
        pdf_cont_strm.lineTo(margin+hor_distance*vertical_line_step-10, a4_landscap_height);
        pdf_cont_strm.stroke();
    }
   
}

private void add_each_inf(PDDocument input_pdf, int input_number) throws IOException{
   int remain_number = input_number % (Column_Count_of_Image * Row_Count_of_Image);
   int hor_step = remain_number % Column_Count_of_Image, ver_step = remain_number / Column_Count_of_Image ;
   
   int hor_pos = (int) hor_distance*hor_step+margin;
   int ver_pos = (int) a4_landscap_height-margin-code_39_height- (int)ver_distance*ver_step-5;
 
   
   add_qr_img_to_pdf(input_pdf, case_number.get(input_number), qr_size, qr_size,hor_pos,ver_pos-qr_size);
   

//   PDFont font_batang = PDType1Font.HELVETICA;  
   pdf_cont_strm.setFont(font_batang, font_size); 
   pdf_cont_strm.beginText();
   pdf_cont_strm.newLineAtOffset( hor_pos + qr_size, ver_pos-font_size);
   pdf_cont_strm.setLeading(text_ver_size);
   pdf_cont_strm.newLine();
   pdf_cont_strm.showText(request_org.get(input_number))  ;
   pdf_cont_strm.newLine();
   pdf_cont_strm.showText(case_number.get(input_number))  ;
   pdf_cont_strm.newLine();
   pdf_cont_strm.showText(evidence_name.get(input_number))  ;
   pdf_cont_strm.newLine();
   pdf_cont_strm.showText(related_person.get(input_number))  ;
   pdf_cont_strm.newLine();
   pdf_cont_strm.endText();
}

private void initialize_pdf_new_page( ){
   PDPage pdf_page = new PDPage(new PDRectangle(a4_landscap_width,a4_landscap_height));
   
   // pdf_page.setRotation(90);
   pdf_doc.addPage(pdf_page);
   try {
   pdf_cont_strm = new PDPageContentStream(pdf_doc, pdf_page);
 
   // add QRCode 
   
   add_code_39_img_to_pdf(pdf_doc, "TL", code_39_width,  code_39_height, (int) margin ,(int) a4_landscap_height- code_39_height- margin);
   add_code_39_img_to_pdf(pdf_doc, "TR", code_39_width,  code_39_height, (int) a4_landscap_width - margin- code_39_width-10,  (int) a4_landscap_height- code_39_height- margin);
   add_code_39_img_to_pdf(pdf_doc, "BL", code_39_width,  code_39_height, (int) margin, (int) margin);
   add_code_39_img_to_pdf(pdf_doc, "BR", code_39_width,  code_39_height,(int) a4_landscap_width - margin-code_39_width-10,(int)margin);
   draw_guide_lines();
   
   } 
    catch (Exception e) {
        e.printStackTrace();
    }
  
}


 
    public void get_infor_from_each_text_file(File each_file){
     
     String Each_Line="";
            try {
            String text_title="";
            String text_info= "";
            BufferedReader Text_Buffered_Reader = new BufferedReader (new InputStreamReader(new FileInputStream(each_file),"euc-kr"));
           
            while ( (Each_Line = Text_Buffered_Reader.readLine()) != null ){
                String[] Splited_Each_Line= Each_Line.split("==");
                 text_title= Splited_Each_Line[0].trim();
                if (Splited_Each_Line.length > 1) {
                      text_info= Splited_Each_Line[1].trim();
                } else {
                      text_info= "";
                }
               
                if(text_title.equals("case_number") ){
                    case_number.add(text_info);
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
            Text_Buffered_Reader.close();
            } catch (IOException e) {
                e.printStackTrace();
             }
            
    }

public  void add_qr_to_PDF(String input_file)throws IOException{
 pdf_doc= new PDDocument();
 font_stream = new FileInputStream("C:/Windows/Fonts/Malgun.ttf");
 font_batang=   PDType0Font.load(pdf_doc, font_stream, true);

  try{
        for (int current_number=0 ; current_number<case_number.size() ; current_number++){
            if (current_number %  (Column_Count_of_Image * Row_Count_of_Image)==0){
                if (current_number>0){
                    pdf_cont_strm.close();
                }
                initialize_pdf_new_page();
            }
            add_each_inf(pdf_doc, current_number);
        }
        pdf_cont_strm.close();

   // Closing the document  
   } catch (Exception e) {
        e.printStackTrace();
    }
   pdf_doc.save(input_file);
   pdf_doc.close(); 
  
   
   // Saving the document
 
}
 public void print_barcode_to_printer() throws IOException{
   
 }

}
