package developery.dev.azurestorageaccount.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import developery.dev.azurestorageaccount.service.Aservice;
import developery.dev.azurestorageaccount.service.AzureStorageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/Aservice")
public class Acontroller {


	@Autowired
	Aservice service;
	
	@GetMapping("/saveA/{containerName}/{blobName}/{message}")
	public String saveBlobByAccessKeyCredential(
			@PathVariable("containerName") String containerName
			, @PathVariable("blobName") String blobName
			, @PathVariable("message") String message) throws IOException {
	
		return service.saveBlobByAccessKeyCredential(containerName, blobName, message);		


  }
	

	@GetMapping("/read/{containerName}")
	public String[]   readFileList(
			@PathVariable("containerName") String containerName)
			throws IOException {
		
		return service.readFileList(containerName);		
	}
	
	@GetMapping("/select/{containerName}/{blobName}")
	public String readSelectFile(
			@PathVariable("containerName") String containerName
			, @PathVariable("blobName") String blobName) throws IOException {
		//log.info("readBlobByAccessKeyCredential. containerName: {}, blobName: {}", containerName, blobName);
		
		return service.readSelectFile(containerName, blobName);		
	}
	
	
	@GetMapping("insertQueue/{msg}")
	public String insertQueue(
			@PathVariable("msg") String msg
			) {
		
		return service.insertQueue(msg);		
	}
	
}
