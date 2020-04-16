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

//    @Autowired
//    PlanService planService;

    public static final int TOTAL_ROW_IN_PAGE = 10;

    /**
     * @param plan
     * @return
     * @throws IOException
     * @method Add Plan
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
     * @method Latest Plan after login
     */
    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResponseEntity<PageResponse<Plan>> findAllLatestPlan(Pageable pageable) {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
//        if (!(au instanceof AnonymousAuthenticationToken)) {
//            String currentUserName = au.getName();
//            return ResponseEntity.ok().body(currentUserName);
//        }
        User user = userRepository.findByEmail(au.getName()).orElse(null);
        //PlanInteractor interactor = planInteractorRepository.findByPlanAndUser();
        PlanService planService = new PlanService();
        Page<Plan> page = (Page<Plan>) planRepository.findAllByOrderByCreatedDayDesc(pageable).stream()
                                                    .map(p ->  planService.convertDtoWithInterac(user, p));

        PageResponse<Plan> response = new PageResponse<Plan>();
        response.setCurrentPage(pageable.getPageNumber());
        response.setTotalPage(page.getTotalPages());
        response.setContent(page.getContent());
        return ResponseEntity.ok().body(response);
    }

    /**
     * @method Newsfeed no Login
     * @param pageable
     * @return
     */

    @RequestMapping(value = "/discovery", method = RequestMethod.GET)
    public ResponseEntity<PageResponse<PlanDto>> findAllLatestPlanWithoutLogin(Pageable pageable) {

        Page<PlanDto> page = planRepository.findAllByOrderByCreatedDayDesc(pageable).map(Plan::convertNewsToDto);
        //Page page= planRepository.findAllByOrderByCreatedDayDesc(pageable);
        PageResponse<PlanDto> response = new PageResponse<PlanDto>();
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
    public ResponseEntity<PageResponse<PlanDto>> getListHotPlan(Pageable pageable) {
        Page<PlanDto> page = planRepository.findListHotPlan(pageable).map(Plan::convertNewsToDto);
        PageResponse<PlanDto> response = new PageResponse<PlanDto>();
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
    public ResponseEntity<List<PlanInteractorDto>> findInteractiveByUser() {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(au.getName()).orElseThrow(() -> new NullPointerException(Constants.AUTHENTICATION_REQUIRED));
        List<PlanInteractor> planInteractor = planInteractorRepository.findAllByUser(user);
        // @formatter:on
        List<PlanInteractorDto> list = planInteractor.stream()
                                                     .map(PlanInteractor::convertToDto)
                                                     .collect(Collectors.toList());
        // @formatter:off
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
    public ResponseEntity<String> followPlan(@PathVariable Long id) {

        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        Plan plan;
        User  user = userRepository.findByEmail(au.getName()).orElse(null);      // sao cho nay lai findbyEmail?? & au.getName()
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> unFollowPlan(@PathVariable Long id) {
        Optional<Plan> plan = planRepository.findById(id);
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(au.getName()).orElseThrow(() -> new NullPointerException(Constants.AUTHENTICATION_REQUIRED));
        PlanInteractor interactor = planInteractorRepository.findByPlanAndUser(plan.orElse(null), user).orElse(null);
        if (interactor != null){
            planInteractorRepository.deleteById(interactor.getId());
            return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
        } else {
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
    public ResponseEntity<String> joinPlan(@PathVariable Long id) {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user;
        Plan plan;
        user = userRepository.findByEmail(au.getName()).orElse(null);  // sao cho nay lai findbyEmail?? & au.getName()
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> disjoinPlan(@PathVariable Long id) {
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user;
        Plan plan;
        user = userRepository.findByEmail(au.getName()).orElse(null);  // sao cho nay lai findbyEmail?? & au.getName()
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
     * @param id plan
     * @return plan detail include basic info
     * @method Plan detail
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PlanDetail> detailPlan(@PathVariable Long id) {

        Plan plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));
        PlanDetail planDetail = new PlanDetail(plan, plan.getUser());
        return ResponseEntity.ok().body(planDetail);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}/interactive", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> detailInteractiveByPlan(@PathVariable Long id) {
        PlanDto planDto = new PlanDto();
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(au.getName()).orElse(null);
        Plan plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));

        List<UserDto> userDtos2 = plan.getPlanInteractors().stream().filter(PlanInteractor::isFollow)
                .map(planInteractor -> planInteractor.convertToDto().getUserDto()).collect(Collectors.toList());

//        List<UserDto> userDtos = planDto.getPlanInteractorDtos().stream()
//                .filter(PlanInteractorDto::isFollow)
//                .map(planInteractorDto -> planInteractorDto.getUserDto()).collect(Collectors.toList());

        return ResponseEntity.ok().body(userDtos2);
    }

    //@PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}/schedule", method = RequestMethod.GET)
    public ResponseEntity<List<Schedule>> detailScheduleByPlan(@PathVariable Long id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new NullPointerException(Constants.PLAN_NOT_EXIST));
        List<Schedule> schedules = plan.getSchedules();
        return ResponseEntity.ok().body(schedules);
    }


}