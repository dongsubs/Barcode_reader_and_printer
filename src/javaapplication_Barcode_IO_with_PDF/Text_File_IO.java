/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication_Barcode_IO_with_PDF;

/**
 *
 * @author user
 */
import java.io.*;

public class Text_File_IO {
    public String Current_Path_of_Image ;
    public String Vertical_Precision_Step ;
    public String Horizontal_Precision_Step ;        
    public String Column_Count_of_Image   ;
    public String Row_Count_of_Image   ;
    public String Horizontal_Gap;
    public String Vertical_Gap;
    public String Margin_Quality;
    public String Sub_Image_Surplus_Horizontal;
    public String Sub_Image_Surplus_Vertical;
    public String QR_Size, Font_Size;
    
    
    public  void Write_Config_to_Text_FIle(){
        try {
            String Current_Path_of_Project =   new java.io.File( "." ).getCanonicalPath();
            FileWriter Text_File_Writer = new FileWriter(Current_Path_of_Project + "/Config.txt", false);
            BufferedWriter Text_Buffered_Writer = new BufferedWriter(Text_File_Writer);
            Text_Buffered_Writer.write("Current_Path_of_Image;" + Current_Path_of_Image);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Vertical_Precision_Step;" + Vertical_Precision_Step);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Horizontal_Precision_Step;" + Horizontal_Precision_Step);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Column_Count_of_Image;" + Column_Count_of_Image);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Row_Count_of_Image;" + Row_Count_of_Image);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Horizontal_Gap;" + Horizontal_Gap);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Vertical_Gap;" + Vertical_Gap);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Margin_Quality;" + Margin_Quality);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Sub_Image_Surplus_Horizontal;" + Sub_Image_Surplus_Horizontal);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Sub_Image_Surplus_Vertical;" + Sub_Image_Surplus_Vertical);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("QR_Size;" + QR_Size);
            Text_Buffered_Writer.newLine();
            Text_Buffered_Writer.write("Font_Size;" + Font_Size);
            Text_Buffered_Writer.newLine();
                       
            Text_Buffered_Writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }    
    
    }
   public void Read_Config_from_Text_File(){
     
     String Each_Line="";
            try {
            String Current_Path_of_Project =   new java.io.File( "." ).getCanonicalPath();
    //        System.out.print(Current_Path_of_Project);
            FileReader Text_File_Reader = new FileReader(Current_Path_of_Project + "/Config.txt");
            BufferedReader Text_Buffered_Reader = new BufferedReader (Text_File_Reader);
            
            while ( (Each_Line = Text_Buffered_Reader.readLine()) != null ){
                String[] Splited_Each_Line= Each_Line.split(";");
                if(Splited_Each_Line[0].equals("Current_Path_of_Image") ){
                    Current_Path_of_Image= Splited_Each_Line[1];
                    
                }
                if(Splited_Each_Line[0].equals("Vertical_Precision_Step") ) {
                    Vertical_Precision_Step= Splited_Each_Line[1];
                }
                if(Splited_Each_Line[0].equals("Horizontal_Precision_Step" )){
                    Horizontal_Precision_Step= Splited_Each_Line[1];
                }
                if(Splited_Each_Line[0].equals("Column_Count_of_Image")){
                    Column_Count_of_Image= Splited_Each_Line[1];
                }
                if(Splited_Each_Line[0].equals("Row_Count_of_Image")){
                    Row_Count_of_Image= Splited_Each_Line[1];
                }
                 if(Splited_Each_Line[0].equals("Horizontal_Gap")){
                    Horizontal_Gap= Splited_Each_Line[1];
                }
                if(Splited_Each_Line[0].equals("Vertical_Gap")){
                    Vertical_Gap= Splited_Each_Line[1];
                }
                if(Splited_Each_Line[0].equals("Margin_Quality")){
                    Margin_Quality= Splited_Each_Line[1];
                }
                  if(Splited_Each_Line[0].equals("Sub_Image_Surplus_Horizontal")){
                    Sub_Image_Surplus_Horizontal= Splited_Each_Line[1];
                }
                 if(Splited_Each_Line[0].equals("Sub_Image_Surplus_Vertical")){
                    Sub_Image_Surplus_Vertical= Splited_Each_Line[1];
                }
                 if(Splited_Each_Line[0].equals("QR_Size")){
                    QR_Size= Splited_Each_Line[1];
                }
                 if(Splited_Each_Line[0].equals("Font_Size")){
                    Font_Size= Splited_Each_Line[1];
                }
           }
       
     //       System.out.print(Column_Count_of_Image);
            Text_File_Reader.close();
            } catch (IOException e) {
             Current_Path_of_Image = "C:/";
             Horizontal_Precision_Step ="20";
             Horizontal_Precision_Step ="3";
             Column_Count_of_Image = "3";
             Row_Count_of_Image   = "2";
             Horizontal_Gap = "0";
             Vertical_Gap = "0";
             Margin_Quality= "4";
             Sub_Image_Surplus_Horizontal="0.05";
             Sub_Image_Surplus_Vertical="0.1";
             QR_Size="80";
             Font_Size="15";
            //e.printStackTrace();
             }
            
    }
   
   
}
