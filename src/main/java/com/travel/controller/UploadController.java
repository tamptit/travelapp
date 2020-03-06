//package com.travel.controller;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import com.google.api.services.drive.model.File;
//import com.travel.entity.Plan;
//import com.travel.entity.User;
//import com.travel.repository.PlanRepository;
//import com.travel.repository.UserRepository;
//import com.travel.service.GoogleDriveService;
//import com.travel.service.StorageService;
//
//import com.travel.utils.Constants;
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.multipart.MultipartFile;
//import javax.websocket.server.PathParam;
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//
//
//@Controller
//@RequestMapping("/api/auth")
//
//public class UploadController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
//
//    @Autowired
//    private StorageService storageService;
//
//    @Autowired
//    GoogleDriveService driveService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    private PlanRepository planRepository;
//
//
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/upload2")
//    public ResponseEntity handelUpload(@PathParam("file") MultipartFile file, Plan plan) throws IOException {
//        Authentication au = SecurityContextHolder.getContext().getAuthentication();
//        Date currentDate = new Date();
//         String dateStr = String.valueOf(currentDate);
//         int pictureId = (au.getPrincipal().toString()+au.getName()).hashCode();
//         String pictureIdStr = String.valueOf(pictureId);
//
//        java.io.File temp = new java.io.File("C:\\Users\\Nguyen\\Pictures\\" + file.getOriginalFilename());
//        try {
//            FileUtils.writeByteArrayToFile(temp, file.getBytes());
//        } catch (IOException e) {
//            LOGGER.error(e.getMessage());
//        }
//        //File name: (idUser + username) hashCode + _date
//        File file2 = driveService.upLoadFile(pictureIdStr+dateStr , temp, "image/jpg");
//        try {
//            TypeReference<HashMap<String, Object>> typeRef
//                    = new TypeReference<HashMap<String, Object>>() {
//            };
//            HashMap<String, Object> map = objectMapper.readValue(file2.toPrettyString(), typeRef);
//            plan.setCreatedDay(currentDate);
//            plan.setImage(prefixUrlImage+map.get("id"));
//            planRepository.save(plan);
//            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
//        } catch (IOException e) {
//            LOGGER.error(e.getMessage());
//            return ResponseEntity.badRequest().body(Constants.ERROR_MESSAGE);
//        }
//    }
//}