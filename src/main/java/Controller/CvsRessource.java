package Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
/**
 * 
 * @author kderb,Adel,shokoufeh,taheri
 *
 */

@RestController
@RequestMapping("/rest/cv")
/**
 * cette classe permet de parser et d'indexer un CV
 * 
 *
 */
public class CvsRessource {
	ArrayList<String >cvs=new ArrayList<String>();

		  TransportClient client;
		int count=0;
		
	    public CvsRessource() throws UnknownHostException {
	        client = new PreBuiltTransportClient(Settings.EMPTY)
	                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

	    }

	
	
	 
	  
	/**
	 * cette methode permet la recuperation  des données enregistrées  par id 
	 * @param id
	 * @return
	 * @throws UnknownHostException
	 */
    @GetMapping("/view/{id}")
    public Map<String, Object>  view(@PathVariable final String id) throws UnknownHostException {
   
    	
        GetResponse getResponse = client.prepareGet("cvdata","id",id).get();
        String langage=(String) getResponse.getSource().get("langage");
    

        System.out.println("langage"+langage);
        return getResponse.getSource();
    }
    
    /**
     * cette methode permet la recupération des données par langage 
     * @param model
     * @return
     * @throws UnknownHostException
     */
    
	
    @GetMapping("/viewlangage")
    public Map<String, Object> viewlangage(Model model) throws UnknownHostException {
      count=count+1;

      GetResponse getResponse = client.prepareGet("cvdata","id",Integer.toString(count)).get();
    
      String langage=(String) getResponse.getSource().get("langage");
       
   
       return getResponse.getSource();
    }
    
   

   
	
}
