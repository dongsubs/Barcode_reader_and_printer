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



import java.io.File;
import java.io.IOException;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.*;
import com.google.zxing.oned.OneDReader;
import com.google.zxing.oned.Code39Reader;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.QRCodeReader;


import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;


public class Barcode_read_write {
    
    public class Margin_Position{
    int Horizontal_Position;
    int Vertical_Position;    
    }

    public static void main(String args[]){
  
    }
    
    public int Height_Depth;
    public int Width_Depth;
    public float Horizontal_Gap;
    public float Vertical_Gap;
    public float Sub_Image_Surplus_Horizontal;
    public float Sub_Image_Surplus_Vertical;

    public int Margin_Quality;
    
    
    Margin_Position Current_Margin = new Margin_Position();
    public int Success_Count;
    int Top_Margin;
    int Bottom_Margin;
    int Left_Margin;
    int Right_Margin;
    
    public String Sub_Folder_String_to_Save ;
    OneDReader Code39_Reader= new Code39Reader();
 
    public void Init_Value(){
     Top_Margin=0;
     Bottom_Margin=0;
     Left_Margin=0;
     Right_Margin=0;
        
    }
    
    public boolean Decode_Single_Image_with_Complex_Barcode(File File_to_Decode) throws IOException{
    boolean Succeed_to_Get_Margin = false;    
    int Horizontal_Real_Gap=0;
    int Vertical_Real_Gap=0;
    Success_Count=0;
    Init_Value();
    Margin_Position Top_Left_Margin = new Margin_Position();
    Margin_Position Top_Right_Margin = new Margin_Position();
    Margin_Position Bottom_Left_Margin = new Margin_Position();
    Margin_Position Bottom_Right_Margin = new Margin_Position();
    
    String   File_Path_to_Decode = File_to_Decode.getPath();
    BinaryBitmap Original_Image = Get_Image_From_File(File_Path_to_Decode);

     Top_Left_Margin = Get_Each_Margin( Original_Image,   "T","L");
     Top_Right_Margin = Get_Each_Margin( Original_Image,   "T","R");
     Bottom_Left_Margin =Get_Each_Margin( Original_Image,   "B","L");
     Bottom_Right_Margin= Get_Each_Margin( Original_Image,   "B","R");
    
     System.out.println(Success_Count);
    if (Success_Count>=Margin_Quality) {
        Top_Margin = Get_Average_Margin(Top_Left_Margin.Vertical_Position, Top_Right_Margin.Vertical_Position) ;
        Bottom_Margin = Get_Average_Margin(Bottom_Left_Margin.Vertical_Position, Bottom_Right_Margin.Vertical_Position) ;
        Left_Margin = Get_Average_Margin(Top_Left_Margin.Horizontal_Position, Bottom_Left_Margin.Horizontal_Position) ;
        Right_Margin = Get_Average_Margin(Top_Right_Margin.Horizontal_Position, Bottom_Right_Margin.Horizontal_Position);
        Horizontal_Real_Gap = (int)((Bottom_Margin - Top_Margin) * Horizontal_Gap);
        Vertical_Real_Gap = (int)((Right_Margin - Left_Margin) * Vertical_Gap) ;
       
    }
    if(Top_Margin>0 && Bottom_Margin>0 && Left_Margin>0 && Right_Margin>0){
        Top_Margin= Top_Margin + Vertical_Real_Gap;
        Bottom_Margin = Bottom_Margin - Vertical_Real_Gap;
        Left_Margin = Left_Margin + Horizontal_Real_Gap;
        Right_Margin = Right_Margin - Horizontal_Real_Gap ;
        Succeed_to_Get_Margin=true;
    }
    return Succeed_to_Get_Margin ;
    }
    
    private int Get_Average_Margin(int First_Value, int Second_Value) {
    int Result_Value=0;
    int Number_Count=0;
    if (First_Value>0){
        Number_Count++;
    }
    if (Second_Value>0){
        Number_Count++;
    }
    if (Number_Count>0){
        Result_Value = ( First_Value + Second_Value) / Number_Count;
    } 
    return Result_Value;
    }
    
    private  Margin_Position Get_Each_Margin(BinaryBitmap Original_Image, String Margin_Marker_V, String Margin_Marker_H){
    Margin_Position Result_Margin = new Margin_Position();
    int Marginal_Height_Depth = 30;
    int Original_Height = Original_Image.getHeight();
    int Current_Height_Depth=1; 
    int Current_Top_Location=0;
    int Current_Height = Original_Height / Marginal_Height_Depth;
    boolean Succed_to_Get_Margin=false;
    
    while(Current_Height_Depth < Marginal_Height_Depth/2 && Succed_to_Get_Margin==false){
        
        if (Margin_Marker_V.equals("T") ){
        Current_Top_Location = (Original_Height/2) - ( Current_Height * Current_Height_Depth) ;
        } else {
        Current_Top_Location = (Original_Height/2) + ( Current_Height * Current_Height_Depth) ;
        }
        Succed_to_Get_Margin = Get_Each_Line_for_Margin(Original_Image,  Margin_Marker_V,  Margin_Marker_H,   Current_Top_Location,  Current_Height);
     Current_Height_Depth++;
    }
     Result_Margin.Vertical_Position = Current_Margin.Vertical_Position;
     Result_Margin.Horizontal_Position = Current_Margin.Horizontal_Position;
    
    return Result_Margin;
   }

    private boolean Get_Each_Line_for_Margin(BinaryBitmap Original_Image, String Margin_Marker_V, String Margin_Marker_H, int Current_Top_Location, int Current_Height){
    
    Current_Margin.Horizontal_Position=-1;
    Current_Margin.Vertical_Position =-1;
    BinaryBitmap Croped_Image = null; 
    int Marginal_Width_Depth  = 30;
    int Original_Width = Original_Image.getWidth();
    int Current_Width_Depth=1; 
    int Current_Width = 0;
    int Current_Left_Location=0;
 
     String Read_Result ="";
     Boolean Succed_to_Get_Margin=false;
     
     String Margin_Marker_Total= Margin_Marker_V + Margin_Marker_H;
 
     while(Current_Width_Depth < Marginal_Width_Depth/2 && Read_Result.equals(Margin_Marker_Total)==false){
      
        Current_Width = Original_Width  / Marginal_Width_Depth * Current_Width_Depth;
        
        if (Margin_Marker_H.equals("L") ){
        Current_Left_Location = Original_Width/2 - Current_Width ;
        } else {
            Current_Left_Location = Original_Width/2 ;
        }
        
        Croped_Image= Original_Image.crop( Current_Left_Location, Current_Top_Location, Current_Width , Current_Height);

        try{
        Read_Result = Code39_Reader.decode(Croped_Image).toString();
        }
        catch( NotFoundException | FormatException IMG_e){
        //System.out.println("Error: "+IMG_e);
        }
        
        Current_Width_Depth++;
    }
        if(Read_Result.equals(Margin_Marker_Total)){
        Current_Margin.Vertical_Position = Current_Top_Location + Current_Height ;
        if (Margin_Marker_V.equals("T") ){
            Current_Margin.Vertical_Position = Current_Top_Location;
        }
        Current_Margin.Horizontal_Position= Current_Left_Location + Current_Width;
        if (Margin_Marker_H.equals("L") ){
           Current_Margin.Horizontal_Position= Current_Left_Location ;
        }
        Success_Count++;
        Succed_to_Get_Margin= true;
        }
    return Succed_to_Get_Margin;
    }
    
    public void Create_Today_Sub_Folder(File Start_File){
    SimpleDateFormat Simple_Date_Format = new SimpleDateFormat("yyyy-MM-dd");
    Date Today = new Date();
    String Today_String = Simple_Date_Format.format(Today);
    String Parent_Path = Start_File.getParent();
    Sub_Folder_String_to_Save = Parent_Path +"/" + Today_String;
    File Sub_Folder_to_Save = new File(Sub_Folder_String_to_Save);

    if (Sub_Folder_to_Save.exists()==false){
        Sub_Folder_to_Save.mkdir();
    }
    }
    
    public void Save_Splited_Images(File File_to_Split, int Row_Count, int Column_Count){
    String   File_Path = File_to_Split.getPath();
    String   File_Parent= File_to_Split.getParent();
    String   File_Name = File_to_Split.getName();
    BufferedImage Original_Image = null;
    BufferedImage[][] Splited_Image = new BufferedImage[Row_Count][Column_Count]; 
    
    int Total_Image_Height = Bottom_Margin - Top_Margin;
    int Total_Image_Width = Right_Margin - Left_Margin;
    int Unit_Height = Total_Image_Height / Row_Count;
    int Unit_Width = Total_Image_Width / Column_Count;
    int Unit_Surplus_Width = (int) (Unit_Width * Sub_Image_Surplus_Horizontal);
    int Unit_Surplus_Height = (int) (Unit_Height * Sub_Image_Surplus_Vertical);
    int Current_Left_Location=0;
    int Current_Top_Location=0;
    int Current_Width = 0;
    int Current_Height = 0;
    
    try{
        Original_Image = ImageIO.read(File_to_Split);
        Original_Image = Original_Image.getSubimage(Left_Margin, Top_Margin, Total_Image_Width,  Total_Image_Height);
    }
    catch(IOException e){
    }
    for(int Current_Row=0 ; Current_Row < Row_Count; Current_Row++){
        for(int Current_Column=0 ; Current_Column< Column_Count; Current_Column++){
  
        Current_Left_Location = (Unit_Width*Current_Column) - Unit_Surplus_Width;
        Current_Top_Location = (Unit_Height*Current_Row) - Unit_Surplus_Height;
        Current_Width = Unit_Width + Unit_Surplus_Width;
        Current_Height = Unit_Height + Unit_Surplus_Height;
        
        if (Current_Left_Location<0) {Current_Left_Location=0;}
        if (Current_Top_Location<0) {Current_Top_Location=0;}
        if (Current_Left_Location+Current_Width>Total_Image_Width) {Current_Width=Unit_Width;}
        if (Current_Top_Location+Current_Height>Total_Image_Height) {Current_Height=Unit_Height;}
        
        try{
        Splited_Image[Current_Row][Current_Column] = Original_Image.getSubimage(Current_Left_Location,Current_Top_Location, Current_Width, Current_Height);
        
        String Barcode_Text= Get_Barcode_text_from_Sub_Image(Splited_Image[Current_Row][Current_Column]);
        if (Barcode_Text.equals("")==false){
            Barcode_Text = Barcode_Text + "-";
        }
        String File_Name_to_Save = Sub_Folder_String_to_Save + "/" + Barcode_Text  + File_Name  + "_Sub_Image_x" + Integer.toString(Current_Column) + "_y" + Integer.toString(Current_Row) + ".jpg";
        
        ImageIO.write(Splited_Image[Current_Row][Current_Column], "jpg",new File(File_Name_to_Save));
        }
        catch(IOException e){
        }
        }
    }
  
    }
    
    public  String Get_Barcode_text(String File_to_Decode) throws IOException {
    
    BinaryBitmap Original_Image = Get_Image_From_File(File_to_Decode);
    
    String Read_Result="";
    int Current_Height_Depth=1; 
    
    while(Current_Height_Depth <= Height_Depth && Read_Result.equals("")){
        Read_Result = Process_Each_Row_of_Image(   Current_Height_Depth,  Original_Image);
        Current_Height_Depth++;
    }
  
    return Read_Result;
    }  
    

    private  String Get_Barcode_text_from_Sub_Image(BufferedImage Image_to_Decode) throws IOException {
 
    LuminanceSource  source = new BufferedImageLuminanceSource(Image_to_Decode);
    BinaryBitmap Original_Image = new BinaryBitmap(new HybridBinarizer(source));
 
    String Read_Result="";
    
    int Current_Height_Depth=1; 
    
    while(Current_Height_Depth <= Height_Depth/2 && Read_Result.equals("")){
        Read_Result = Process_Each_Row_of_Image( Current_Height_Depth,  Original_Image);
        if(Read_Result.equals("TL") || Read_Result.equals("TR") || Read_Result.equals("BL") || Read_Result.equals("BR")){
            Read_Result="";
        }
        Current_Height_Depth++;
    }
  
    return Read_Result;
    }  
  
    private  String Get_QR_code_text_from_Sub_Image(BufferedImage Image_to_Decode) throws IOException {
 
    LuminanceSource  source = new BufferedImageLuminanceSource(Image_to_Decode);
    BinaryBitmap Original_Image = new BinaryBitmap(new HybridBinarizer(source));
 
    String Read_Result="";
    
    int Current_Height_Depth=1; 
    QRCodeReader Sub_qr_reader = new QRCodeReader();
    try {
        Read_Result =   Sub_qr_reader.decode(Original_Image).getText();
    } catch (NotFoundException  |   ChecksumException  | FormatException e) {
        e.printStackTrace();
    }
    
    return Read_Result;
    }  
  
    
    private  String Process_Each_Row_of_Image( int Current_Height_Depth, BinaryBitmap Original_Image){
    BinaryBitmap Croped_Image = null; 
    int Original_Height = Original_Image.getHeight();
    int Original_Width = Original_Image.getWidth();
    int Current_Width_Depth=1; 
    String Read_Result="";
    boolean Suceed_in_Read=false;
  
    int Current_Left_Location=0;
    int Current_Top_Location=0;
    int Current_Width=0;
    int Current_Height=0;

    
    while(Current_Width_Depth <= Width_Depth && Suceed_in_Read==false){
        
        Current_Left_Location=0;
        Current_Top_Location=(Original_Height/Height_Depth)*(Current_Height_Depth-1);
        Current_Width= Original_Width;
        Current_Height=Original_Height/Height_Depth;

        if (Suceed_in_Read==false){
            Croped_Image= Original_Image.crop( Current_Left_Location, Current_Top_Location, Current_Width, Current_Height);
        try{
            Read_Result = Code39_Reader.decode(Croped_Image).toString();
            Suceed_in_Read = true;
        }
        catch( NotFoundException | FormatException IMG_e){
        }
        }

        Current_Left_Location=(Original_Width/Width_Depth)*(Current_Width_Depth-1);
        Current_Width= Original_Width/Width_Depth;
        if (Suceed_in_Read==false){
            Croped_Image= Original_Image.crop( Current_Left_Location, Current_Top_Location, Current_Width, Current_Height);
        try{

            Read_Result = Code39_Reader.decode(Croped_Image).toString();
            Suceed_in_Read = true;
        }
        catch( NotFoundException | FormatException IMG_e){
        //System.out.println("Error: "+IMG_e);
        }
        }
        
        Current_Left_Location=0;
        Current_Width= (Original_Width/Width_Depth)*(Current_Width_Depth);
     
        if (Suceed_in_Read == false) {
            Croped_Image= Original_Image.crop( Current_Left_Location, Current_Top_Location, Current_Width, Current_Height);
        try{
            Read_Result = Code39_Reader.decode(Croped_Image).toString();
            Suceed_in_Read = true;
        }
        catch( NotFoundException | FormatException IMG_e){
        }
        }

        Current_Left_Location=(Original_Width/Width_Depth)*(Current_Width_Depth);
        Current_Width= (Original_Width/Width_Depth)*(Width_Depth-Current_Width_Depth);
        if (Suceed_in_Read == false) {
            Croped_Image= Original_Image.crop( Current_Left_Location, Current_Top_Location, Current_Width, Current_Height);
        try{
            Read_Result = Code39_Reader.decode(Croped_Image).toString();
            Suceed_in_Read = true;
        }
        catch( NotFoundException | FormatException IMG_e){
        }
        }
        //System.out.println(Current_Width_Depth);
        Current_Width_Depth++;
        }
    
      
      if(Suceed_in_Read==false){
          Read_Result="";
      }
      return    Read_Result;
  
    }
    
    private static BinaryBitmap Get_Image_From_File (String File_to_Decode)throws IOException{
    
    File ImageFile = new File(File_to_Decode);
    BufferedImage Barcode_Image = null;

    try{
    Barcode_Image = ImageIO.read(ImageFile);
    }
    catch(IOException IO_e){
      System.out.println("Error: "+IO_e);
    }
    
    LuminanceSource  source = new BufferedImageLuminanceSource(Barcode_Image);
    BinaryBitmap Barcode_Bitmap = new BinaryBitmap(new HybridBinarizer(source));
   
    return Barcode_Bitmap;
    }

    private static void Save_QRCodeImage_to_path(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
    public BufferedImage generate_QR_Code_buff_img(String text, int width, int height)
            throws WriterException, IOException {
        
        QRCodeWriter qr_writer = new QRCodeWriter();
        BitMatrix qr_bit_image = qr_writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        BufferedImage qr_buff_img = MatrixToImageWriter.toBufferedImage(qr_bit_image);
        return qr_buff_img;
    }
    
    public BufferedImage generate_code_39_buff_img(String text, int width, int height)
            throws WriterException, IOException {
        
        Code39Writer code_39_writer = new Code39Writer();
        BitMatrix qr_bit_image = code_39_writer.encode(text, BarcodeFormat.CODE_39, width, height);
        BufferedImage qr_buff_img = MatrixToImageWriter.toBufferedImage(qr_bit_image);
        return qr_buff_img;
    }
    
  public BitMatrix generate_QR_Code_bitmat_img(String text, int width, int height)
            throws WriterException, IOException {
        
        QRCodeWriter qr_writer = new QRCodeWriter();
        BitMatrix qr_bit_image = qr_writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        return qr_bit_image;
    }
}

	
		
      
    
    
    

