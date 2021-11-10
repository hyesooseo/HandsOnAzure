package developery.dev.azurestorageaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import developery.dev.azurestorageaccount.service.Bservice;

@SpringBootApplication
public class AzureStorageAccountApplication {

	public static void main(String[] args) {
		
		
		
	
		SpringApplication.run(AzureStorageAccountApplication.class, args);
		
		Bservice b = new Bservice();
		b.run();
		
	
	}

}
