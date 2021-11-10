package developery.dev.azurestorageaccount.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class Aservice {
	
	final String BLOB_END_POINT = "https://hyesooseo.queue.core.windows.net";
	final String accessKey = "DefaultEndpointsProtocol=https;AccountName=hyesooseo;AccountKey=0A7gHjFCZNcWFENsp1XmZXbUSZQyM+dbY7T6qzvpfSthpqkMonWPX3OyLLztDqvnTe91EvoXHOvKJGumj1sSdg==;EndpointSuffix=core.windows.net";
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
		    .endpoint(BLOB_END_POINT)
		    .connectionString(accessKey)
		    .buildClient();
	
	final String ACCOUNT_END_POINT = "https://hyesooseo.queue.core.windows.net/";
	final String QUEUE_END_POINT = ACCOUNT_END_POINT + "demo";
	String  SAS_TOKEN= "?sv=2020-08-04&ss=bfqt&srt=sco&sp=rwdlacupitfx&se=2021-11-10T18:34:33Z&st=2021-11-10T10:34:33Z&spr=https&sig=cjAJWPiNGhQi11b6WJWHON8eKYUCUTtFEtF8Ylu47CA%3D";

		
	
	public String saveBlobByAccessKeyCredential(String containerName, String blobName, String message) throws IOException {
		

		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
		
	
		String fileName = blobName+ ".txt";
		File localFile = new File(fileName);

		
		FileWriter writer = new FileWriter(fileName, true);
		writer.write(message);
		writer.close();

		
		BlobClient blobClient = containerClient.getBlobClient(fileName);

		
		blobClient.uploadFromFile(fileName);
		
		
		return message;
	}
	

	public String[] readFileList (String containerName) throws IOException {
		
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
		ArrayList<String> blobList = new ArrayList();
		
		for (BlobItem blobItem : containerClient.listBlobs()) {
		    //System.out.println("\t" + blobItem.getName());
			blobList.add(blobItem.toString());
		}
		
		String [] list = new String [blobList.size()];

		for(int i=0; i< list.length;i++) {
			
			list[i] =  blobList.get(i).toString();
		}
		
		
		return list;
	}
	
	public String readSelectFile(String containerName, String blobName) throws IOException {
		
		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
		
		BlobClient blobClient = containerClient.getBlobClient(blobName);
		
		String textInFile = CommonUtils.readStringFromFile(blobName+".txt");
		
		return textInFile;
	}

	public String insertQueue(String msg) {
		
		QueueClient queueClient = new QueueClientBuilder()
				.endpoint(QUEUE_END_POINT)
				.sasToken(SAS_TOKEN)
				.buildClient();
		
		SendMessageResult result = queueClient.sendMessage(LocalDateTime.now() + "___________________" + msg);
		
		System.out.println("msgId: " + result.getMessageId());
		
		return result.getMessageId();
		
	}

}
