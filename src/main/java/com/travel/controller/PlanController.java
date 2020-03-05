package com.travel.controller;


import com.travel.config.JwtTokenProvider;
import com.travel.dto.PageResponse;
import com.travel.dto.PlanForm;
import com.travel.entity.Plan;
import com.travel.entity.PlanInteractor;
import com.travel.entity.User;
import com.travel.repository.PlanInteractorRepository;
import com.travel.repository.PlanRepository;
import com.travel.repository.UserRepository;
import com.travel.utils.Constants;
import com.travel.validator.MapValidationError;
import com.travel.service.PlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.security.Principal;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/plan")
@CrossOrigin
public class PlanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlanService planService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private MapValidationError mapValidationErrorService;
    @Autowired
    PlanInteractorRepository planInteractorRepository;

    public static final int TOTAL_ROW_IN_PAGE = 10;

    //Them ke hoach
    @PreAuthorize("isAuthenticated()")
    @PostMapping("add-plan")
    public ResponseEntity<?> createNewPlan(@Valid @RequestBody PlanForm planForm, BindingResult result) {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;
        Plan plan1 = planService.saveOrUpdate(planForm);
        return new ResponseEntity<Plan>(plan1, HttpStatus.CREATED);
    }

    // lấy 10 kế hoạch mới nhất
    @GetMapping("/list")
    public PageResponse getLatestPlan(@PathParam(value = "page") int page) throws ParseException {
        page = page <= 0 ? 0 : page - 1;
        Pageable sortedByCreatedDay = PageRequest.of(page, TOTAL_ROW_IN_PAGE, Sort.by("createdDay").descending());
        Page<Plan> planPager = planRepository.findAll(sortedByCreatedDay);
        PageResponse response = new PageResponse();
        response.setCurrentPage(page);
        response.setTotalPage(planPager.getTotalPages());
        response.setPlans(planPager.getContent());
        return response;
    }
    // 10 kế hoạch mới nhất
    @RequestMapping(value = "/lastest-plan", method = RequestMethod.GET)
    public List<Plan> findAll() {
        List<Plan> sortPlan = planRepository.findAllByOrderByCreatedDayDesc();
        List<Plan> hotPlan = new ArrayList<Plan>();
        for (int i = 0 ; i <10; i++){
            hotPlan.add(sortPlan.get(i));
        }
        return hotPlan;
    }
    //     lấy 10 kế hoạch HOT nhất
    @GetMapping("/plan-hot")
    public PageResponse getHotPlan(@PathParam(value = "page") int page) throws ParseException {
        page = page <= 0 ? 0 : page - 1;
        Pageable sortedByCountUser = PageRequest.of(page, TOTAL_ROW_IN_PAGE, Sort.by("countUser").descending());
        Page<Plan> hotPager = planRepository.findAll(sortedByCountUser);
        PageResponse response = new PageResponse();
        response.setCurrentPage(page);
        response.setTotalPage(hotPager.getTotalPages());
        response.setPlans(hotPager.getContent());
        return response;
    }

    @RequestMapping("/hot-plan")
    public ResponseEntity getListHotPlan( Pageable pageable) {

        Page page = planRepository.findListHotPlan(pageable);

        return ResponseEntity.ok().body(page.getContent());

    }

    @PutMapping("/follow/{id-plan}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity followPlan(@RequestParam Long idPlan){
        Optional<Plan> plan = planRepository.findById(idPlan);
        Authentication au = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(au.getName()).orElse(null);
        PlanInteractor planInteractor = new PlanInteractor();
        planInteractor.setUser(user);
        planInteractor.setPlan(plan.get());
        planInteractor.setStatus(0);
        planInteractorRepository.save(planInteractor);
        return ResponseEntity.ok().body(Constants.SUCCESS_MESSAGE);
    }

}