package Controller;



import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 * @author kderb,Adel,shoukoufa,taheri
 *
 */



@Controller
public class FileUploadController {
	  TransportClient client;
		
	  int j=0;
	    public FileUploadController() throws UnknownHostException {
	        client = new PreBuiltTransportClient(Settings.EMPTY)
	                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

	    }
	
	public static String UploadDirectory= System.getProperty("user.dir")+"/uploads";
	String texte;
	Path fileNamePath;
	ArrayList<String > contennue =new ArrayList<String>();
	ArrayList<Path > name =new ArrayList<Path>();

	@RequestMapping("/")
	public String UploadPage(Model model) {
		return "uploadview";
	}
	/**
	 * cette methode permet de parser un fichier et de l'indexer
	 * @param model
	 * @param files
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/upload")
	public String upload(Model model,@RequestParam ("files") MultipartFile[] files) throws IOException {
		byte[] data = null;
		
		InputStream data1 = null ;


		Scanner sc = null;
		String s = null;
		String line;
		File file2;
		String langageString=null;
		StringBuilder fileNames=new StringBuilder();
		FileInputStream fileStream;

		for(MultipartFile file:files) {
			fileNames.append(file.getOriginalFilename());
			fileNamePath =Paths.get(UploadDirectory,file.getOriginalFilename());
			 file2 = new File(fileNamePath.toString());
		
			Files.write(fileNamePath, file.getBytes());
			name.add(fileNamePath);
		 

	
			 String extension = "";
			 int i = fileNames.toString().lastIndexOf('.');
		      if (i > 0) {
		             extension = fileNames.toString().substring(i+1);
		      }
		    
		     
		     if(extension.equals("pdf")) {
		    	 PDDocument document = PDDocument.load(file2);
			     
		    	 PDFTextStripper pdfStripper = new PDFTextStripper();
			    
			      System.out.println("extensionnnnn"+fileNamePath);
			       texte = pdfStripper.getText(document);
			      System.out.println(texte);
			      if(texte.contains("java")) {
			    	  langageString="java";
			      }
			      
		    
			 	 j=j+1;
			
			      IndexResponse response = client.prepareIndex("cvdata", "id",Integer.toString(j))
			                .setSource(jsonBuilder()
			                        .startObject()
			                        .field("path",fileNamePath.toString())
			                        .field("langage", langageString)
			                      
			                        
			                        .endObject()
			                )
			                .get();
		
			 	 contennue.add(texte);
		    
			      model.addAttribute("msg","succsesufl upload"+texte); 
			      
			      
			        document.close();
		     }else if (extension.equals("docx")) {
		    	 System.out.println("le nom du fichier :"+fileNames);
		    	 fileStream= new FileInputStream(fileNamePath.toString());
		    	 //this class is used to extract the content 
		    	 XWPFDocument docx =new XWPFDocument(fileStream);
		    	 XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
				   System.out.println(extractor.getText());
		    	 texte=extractor.getText();
		    	  if(texte.contains("java")) {
			    	  langageString="java";
			      }
		    	j=j+1;
		    	  
			      IndexResponse response = client.prepareIndex("cvdata", "id",Integer.toString(j))
			                .setSource(jsonBuilder()
			                        .startObject()
			                        .field("path",fileNamePath.toString())
			                        .field("langage",langageString)
			                        
			                        .endObject()
			                )
			                .get();
			      
			      
			 
			   System.out.println( "+++++++++++++++"+response.getResult().toString());
			 
			      
		    	 contennue.add(texte);
		    	 model.addAttribute("msg",texte);
		     }
	
		}
		
	
		  
		return "uploadstatusview";
	}
	
	

    

}
