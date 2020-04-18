package BARCODE;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicReference;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import com.github.javafaker.Faker;

public class Barcode_Image {

    public static void main(String[] args) {
        String x = "";
        Integer y = 0;
        fakeNumbers(x, y);
        System.out.println("Procedure completed");
    }

    private static void fakeNumbers(String x, Integer y) {
        Faker faker = new Faker();
        Document doc = new Document();
        PdfWriter docWriter = null;
        String name = faker.address().streetName();
        Integer number = Integer.valueOf(faker.address().buildingNumber());
        Barcode_Image.createImage(name + ".png", number);
    }

    private static void createImage(String image_name, Integer mynumber) {
        AtomicReference<String> pdfName = new AtomicReference<String>("");
        try {
            Code128Bean code128 = new Code128Bean();
            code128.setHeight(15f);
            code128.setModuleWidth(0.3);
            code128.setQuietZone(10);
            code128.doQuietZone(true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(baos, "image/x-png", 300, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            code128.generateBarcode(canvas, String.valueOf(mynumber));
            canvas.finish();
            //write to png file
            FileOutputStream fos = new FileOutputStream("C:\\Users\\abalanikas\\Desktop\\" + image_name);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        pdfName.set(image_name + ".pdf");
        createPDF(pdfName, image_name);
    }

    private static void createPDF(AtomicReference<String> pdfName, String image_name2) {
        Document doc = new Document();
        PdfWriter docWriter = null;

        try {
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream("C:\\\\Users\\\\abalanikas\\\\Desktop\\\\" + pdfName));
            doc.addAuthor(image_name2);
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("Company SA");
            doc.addTitle("Company SA Barcode test");
            doc.setPageSize(PageSize.LETTER);
            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();

            Barcode128 code128 = new Barcode128();
            code128.setCode(image_name2.trim());
            code128.setCodeType(Barcode128.CODE128);
            Image code128Image = code128.createImageWithBarcode(cb, null, null);
            code128Image.setAbsolutePosition(10, 700);
            code128Image.scalePercent(125);
            doc.add(code128Image);

            BarcodeEAN codeEAN = new BarcodeEAN();
            codeEAN.setCode(image_name2);
            codeEAN.setCodeType(BarcodeEAN.EAN13);
            Image codeEANImage = code128.createImageWithBarcode(cb, null, null);
            codeEANImage.setAbsolutePosition(10, 600);
            codeEANImage.scalePercent(125);
            doc.add(codeEANImage);
        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            doc.close();
            if (docWriter != null) {
                docWriter.close();
            }
        }
    }
}

