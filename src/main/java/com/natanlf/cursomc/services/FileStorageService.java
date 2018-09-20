package com.natanlf.cursomc.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.natanlf.cursomc.services.exceptions.FileException;
import com.natanlf.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class FileStorageService {

	private Path fileStorageLocation;
	
	@Value("${file.upload-dir}")
	private String directory; 
	
	public void location() {
		   this.fileStorageLocation = Paths.get(directory)
	               .toAbsolutePath().normalize();
	
	       try {
	           Files.createDirectories(this.fileStorageLocation);
	       } catch (Exception ex) {
	           throw new FileException("Could not create the directory where the uploaded files will be stored.", ex);
	       }
	}
	     

	
	public String storeFile(MultipartFile file) {
		location();
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
	
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new ObjectNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ObjectNotFoundException("File not found " + fileName, ex);
        }
    }
}
