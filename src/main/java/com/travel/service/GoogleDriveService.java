package com.travel.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface GoogleDriveService {
	
	public com.google.api.services.drive.model.File uploadFile(String fileName, File file, String mimeType);

}
