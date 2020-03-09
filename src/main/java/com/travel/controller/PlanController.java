package com.travel.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.drive.model.File;
import com.travel.config.JwtTokenProvider;
import com.travel.dto.PageResponse;
import com.travel.dto.PlanForm;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.service.GoogleDriveService;
import com.travel.utils.Constants;
import com.travel.repository.UserRepository;
import com.travel.utils.Constants;
import com.travel.validator.MapValidationError;
import com.travel.service.PlanService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.*;

@RestController
@RequestMapping(value = "/api/plan")
@CrossOrigin
public class PlanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanController.class);

    @Autowired
    private UserRepository userRepository;
    @Value("${preFixUrlImage}")
    private String prefixUrlImage;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MapValidationError mapValidationErrorService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PlanInteractorRepository planInteractorRepository;
    @Autowired
    GoogleDriveService driveService;

    public static final int TOTAL_ROW_IN_PAGE = 10;

    //Them ke hoach
    @PutMapping
    public ResponseEntity<?> handelUpload(@RequestBody Plan plan) throws IOException {
        java.io.File file = java.io.File.createTempFile("tmp", ".jpg");
        byte[] decodedBytes = Base64.getDecoder().decode(plan.getImageCover().split(",",2)[1]);
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        Date currentDate = new Date();
        String dateStr = String.valueOf(currentDate);
        String fileName = bCryptPasswordEncoder.encode(au.getPrincipal().toString() + au.getName());

//        java.io.File temp = new java.io.File("C:\\Users\\Nguyen\\Pictures\\" + file.getOriginalFilename());
        try {
            FileUtils.writeByteArrayToFile(file,decodedBytes)  ;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        //File name: (idUser + username) hashCode + _date
        File file2 = driveService.upLoadFile(fileName + dateStr + "jpg" ,file , "image/jpg");
        try {
            TypeReference<HashMap<String, Object>> typeRef
                    = new TypeReference<HashMap<String, Object>>() {
            };
            HashMap<String, Object> map = objectMapper.readValue(file2.toPrettyString(), typeRef);
            plan.setCreatedDay(currentDate);
            plan.setImageCover(prefixUrlImage + map.get("id"));
            planRepository.save(plan);
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(Constants.ERROR_MESSAGE);
        }
    }

    // -------10 kế hoạch mới nhất  ----//
    @RequestMapping(value = "/lastest", method = RequestMethod.GET)
    public ResponseEntity findAllHotPlan(Pageable pageable) {
        Page page= planRepository.findAllByOrderByCreatedDayDesc(pageable);
        PageResponse response = new PageResponse();
        response.setCurrentPage(pageable.getPageNumber());
        response.setTotalPage(page.getTotalPages());
        response.setPlans(page.getContent());
        return ResponseEntity.ok().body(response);
    }

    //     lấy 10 kế hoạch HOT nhất
    @RequestMapping("/hot-plan")
    public ResponseEntity getListHotPlan(Pageable pageable) {
        Page page = planRepository.findListHotPlan(pageable);
        PageResponse response = new PageResponse();
        response.setCurrentPage(pageable.getPageNumber());
        response.setTotalPage(page.getTotalPages());
        response.setPlans(page.getContent());
        return ResponseEntity.ok().body(response);
    }
    //----- 10 plan mới nhất -----//
    @GetMapping("/newest")
    public ResponseEntity getLatestPlan(@PathParam(value = "page") int page) throws ParseException {
        page = page <= 0 ? 0 : page - 1;
        Pageable sortedByCreatedDay = PageRequest.of(page, TOTAL_ROW_IN_PAGE, Sort.by("createdDay").descending());
        Page<Plan> planPager = planRepository.findAll(sortedByCreatedDay);
        PageResponse response = new PageResponse();
        response.setCurrentPage(page);
        response.setTotalPage(planPager.getTotalPages());
        response.setPlans(planPager.getContent());
        return ResponseEntity.ok().body(response);
    }
    //----- Follow plan ------//
    @Transactional
    @PutMapping(value = "/follow/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity followPLan(@PathVariable Long id) {
        Optional<Plan> plan = planRepository.findById(id);
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User uR = userRepository.findByEmail(au.getName()).orElse(null);
        PlanInteractor interactor = planInteractorRepository.findByPlanAndUser(plan.get(), uR);
        if (interactor != null) {
            return ResponseEntity.ok().body(Constants.MESSAGE);
        } else {
            PlanInteractor planInteractor = new PlanInteractor();
            planInteractor.setUser(uR);
            planInteractor.setPlan(plan.get());
            planInteractor.setStatus(0);
            planInteractorRepository.save(planInteractor);
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        }
    }
    //----- Unfollow plan ------//
    @Transactional
    @DeleteMapping("/follow/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id) {
        Optional<Plan> plan = planRepository.findById(id);
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User uR = userRepository.findByEmail(au.getName()).orElse(null);
        PlanInteractor interactor = planInteractorRepository.findByPlanAndUser(plan.get(), uR);
        if (interactor != null){
            planInteractorRepository.deleteById(interactor.getId());
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        }else{
            return ResponseEntity.ok().body(Constants.MESSAGE);
        }
    }

}