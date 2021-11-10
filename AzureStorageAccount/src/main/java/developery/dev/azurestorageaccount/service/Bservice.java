package developery.dev.azurestorageaccount.service;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobProperties;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.models.SendMessageResult;

import developery.dev.azurestorageaccount.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service


public class Bservice extends Thread{
	


	
	final String BLOB_END_POINT = "https://hyesooseo.queue.core.windows.net";
	final String accessKey = "DefaultEndpointsProtocol=https;AccountName=hyesooseo;AccountKey=0A7gHjFCZNcWFENsp1XmZXbUSZQyM+dbY7T6qzvpfSthpqkMonWPX3OyLLztDqvnTe91EvoXHOvKJGumj1sSdg==;EndpointSuffix=core.windows.net";
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
		    .endpoint(BLOB_END_POINT)
		    .connectionString(accessKey)
		    .buildClient();
	
	final String ACCOUNT_END_POINT = "https://hyesooseo.queue.core.windows.net/";
	final String QUEUE_END_POINT = ACCOUNT_END_POINT + "demo";
	String  SAS_TOKEN= "?sv=2020-08-04&ss=bfqt&srt=sco&sp=rwdlacupitfx&se=2021-11-10T18:34:33Z&st=2021-11-10T10:34:33Z&spr=https&sig=cjAJWPiNGhQi11b6WJWHON8eKYUCUTtFEtF8Ylu47CA%3D";

		
	int maxSize=10;
	
	
	@Override 
	public void run() {
	//public String saveBlobFromQue() throws IOException {

		
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("hscontain");
		QueueClient queueClient = new QueueClientBuilder()
				.endpoint(QUEUE_END_POINT)
				.sasToken(SAS_TOKEN)
				.buildClient();
	

		while(true) {
		List<String> msgIdList = queueClient.receiveMessages(maxSize).stream()
				.map( message -> {
					
					queueClient.deleteMessage(message.getMessageId(), message.getPopReceipt()); // 삭제
					
					String fileName = message.getMessageId();
					
					FileWriter writer=null;
					try {
						writer = new FileWriter(fileName, true);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						writer.write(message.getMessageText().toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					BlobClient blobClient =containerClient.getBlobClient(fileName); 
					blobClient.uploadFromFile(fileName);
					
					return message.getMessageId();
				})
				.collect(Collectors.toList());		
				
		}
	}
	 

}
