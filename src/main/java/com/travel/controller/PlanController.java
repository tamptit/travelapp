package com.travel.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.drive.model.File;
import com.travel.dto.*;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.Schedule;
import com.travel.entity.User;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.service.GoogleDriveService;
import com.travel.service.PlanService;
import com.travel.utils.Constants;
import com.travel.validator.MapValidationError;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private MapValidationError mapValidationError;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PlanInteractorRepository planInteractorRepository;
    @Autowired
    GoogleDriveService driveService;

    @Autowired
    PlanService planService;

    public static final int TOTAL_ROW_IN_PAGE = 10;

    /**
     * @method Add Plan
     * @param plan
     * @return
     * @throws IOException
     */
    @Transactional
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> handelUpload(@RequestBody Plan plan) throws IOException {
        java.io.File file = java.io.File.createTempFile("tmp", ".jpg");
        byte[] decodedBytes = Base64.getDecoder().decode(plan.getImageCover().split(",", 2)[1]);
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        Date currentDate = new Date();
        String dateStr = String.valueOf(currentDate);
        String fileName = bCryptPasswordEncoder.encode(au.getPrincipal().toString() + au.getName());
        FileUtils.writeByteArrayToFile(file, decodedBytes);

        //File name: (idUser + username) hashCode + _date
        File file2 = driveService.uploadFile(fileName + dateStr + "jpg", file, "image/jpg");
        try {
            TypeReference<HashMap<String, Object>> typeRef
                    = new TypeReference<HashMap<String, Object>>() {
            };
            HashMap<String, Object> map = objectMapper.readValue(file2.toPrettyString(), typeRef);
            User user = userRepository.findByEmail(au.getName()).orElse(null);
            plan.setUser(user);
            plan.setCreatedDay(currentDate);
            plan.setImageCover(prefixUrlImage + map.get("id"));
            planRepository.save(plan);
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body(Constants.ERROR_MESSAGE);
        }
    }

    /**
     * @param pageable
     * @return
     * @method Latest Plan
     */
    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    public ResponseEntity findAllLatestPlan(Pageable pageable) {
        Page page = planRepository.findAllByOrderByCreatedDayDesc(pageable)
                .map(Plan::convertToDto);
        //Page page= planRepository.findAllByOrderByCreatedDayDesc(pageable);
        PageResponse<Plan> response = new PageResponse<Plan>();
        response.setCurrentPage(pageable.getPageNumber());
        response.setTotalPage(page.getTotalPages());
        response.setContent(page.getContent());
        return ResponseEntity.ok().body(response);
    }

    /**
     * @param pageable
     * @return
     * @method Hot Plan
     */
    @RequestMapping("/hot-plan")
    public ResponseEntity getListHotPlan(Pageable pageable) {
        Page page = planRepository.findListHotPlan(pageable);
        PageResponse<Plan> response = new PageResponse<Plan>();
        response.setCurrentPage(pageable.getPageNumber());
        response.setTotalPage(page.getTotalPages());
        response.setContent(page.getContent());
        return ResponseEntity.ok().body(response);
    }

    /**
     * @return
     * @method get list PlanInterac by User
     */

    @RequestMapping(value = "/interactive", method = RequestMethod.GET)
    public ResponseEntity findInteractiveByUser() {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(au.getName()).orElseThrow(() -> new NullPointerException(Constants.AUTHENTICATION_REQUIRED));
        List<PlanInteractor> planInteractor = planInteractorRepository.findAllByUser(user);
        List<PlanInteractorDto> list = planInteractor.stream().map(PlanInteractor::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }

    /**
     * @param id Plan
     * @return
     * @metod Follow Plan
     */
    @Transactional
    @PutMapping(value = "/{id}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity followPlan(@PathVariable Long id) {

        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user;
        Plan plan;
        user = userRepository.findByEmail(au.getName()).get();  // sao cho nay lai findbyEmail?? & au.getName()
        plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));

        PlanInteractor interactor = planInteractorRepository.findByPlanAndUser(plan, user).orElse(null);
        if (interactor != null) {
            interactor.setFollow(true);
            planInteractorRepository.save(interactor);
            return ResponseEntity.ok().body(Constants.MESSAGE);
        } else {
            PlanInteractor planInteractor = new PlanInteractor(user, plan, true, false);
            planInteractorRepository.save(planInteractor);
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        }
    }

    /**
     * @method Unfollow plan
     * @param id
     * @return
     */
    @Transactional
    @DeleteMapping("/{id}/follow")
    public ResponseEntity unFollowPlan(@PathVariable Long id) {
        Optional<Plan> plan = planRepository.findById(id);
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(au.getName()).orElseThrow(() -> new NullPointerException(Constants.AUTHENTICATION_REQUIRED));
        PlanInteractor interactor = planInteractorRepository.findByPlanAndUser(plan.get(), user).orElse(null);
        if (interactor != null){
            planInteractorRepository.deleteById(interactor.getId());
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        }else{
            return ResponseEntity.ok().body(Constants.MESSAGE);
        }
    }

    /**
     * @param id Plan
     * @return
     * @method Join plan
     */
    @Transactional
    @PutMapping(value = "/{id}/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity joinPlan(@PathVariable Long id) {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user;
        Plan plan;
        user = userRepository.findByEmail(au.getName()).get();  // sao cho nay lai findbyEmail?? & au.getName()
        plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));

        PlanInteractor interactor = planInteractorRepository.findByPlanAndUser(plan, user).orElse(null);
        if (interactor != null) {
            interactor.setJoin(true);
            interactor.setFollow(true);

            planInteractorRepository.save(interactor);
            return ResponseEntity.ok().body(Constants.MESSAGE);
        } else {
            PlanInteractor planInteractor = new PlanInteractor(user, plan, true, true);
            planInteractorRepository.save(planInteractor);
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        }
    }

    /**
     * @param id Plan
     * @return
     * @method DisJoin Plan
     */
    @Transactional
    @DeleteMapping(value = "/{id}/disjoin")
    public ResponseEntity disjoinPlan(@PathVariable Long id) {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user;
        Plan plan;
        user = userRepository.findByEmail(au.getName()).get();  // sao cho nay lai findbyEmail?? & au.getName()
        plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));

        PlanInteractor interactor = planInteractorRepository.findByPlanAndUser(plan, user).orElse(null);
        if (interactor != null) {
            if (interactor.isJoin()) {
                interactor.setJoin(false);
                interactor.setFollow(false);
                //planInteractorRepository.delete(interactor);
            } else {
                // ?
            }
            planInteractorRepository.save(interactor);
            return ResponseEntity.ok().body(Constants.MESSAGE);
        } else {
            PlanInteractor planInteractor = new PlanInteractor(user, plan, true, true);
            planInteractorRepository.save(planInteractor);
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        }
    }

    /**
     * @param id
     * @return
     * @method info Plan detail
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/plan/{id}", method = RequestMethod.GET)
    public ResponseEntity detailPlan(@PathVariable Long id) {

        Plan plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));
        PlanDetail planDetail = new PlanDetail(plan, plan.getUser());
        return ResponseEntity.ok().body(planDetail);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/plan/{id}/interactive", method = RequestMethod.GET)
    public ResponseEntity detailInteractiveByPlan(@PathVariable Long id) {
        PlanDto planDto = new PlanDto();
        Plan plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));
        planDto = plan.convertToDto();
        List<UserDto> userDtos = planDto.getPlanInteractorDtos().stream()
                .filter(planInteractorDto -> planInteractorDto.isFollow())
                .map(planInteractorDto -> planInteractorDto.getUserDto()).collect(Collectors.toList());
        return ResponseEntity.ok().body(userDtos);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/plan/{id}/schedule", method = RequestMethod.GET)
    public ResponseEntity detailScheduleByPlan(@PathVariable Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));
        List<Schedule> schedules = plan.getSchedules();
        return ResponseEntity.ok().body(schedules);
    }


}