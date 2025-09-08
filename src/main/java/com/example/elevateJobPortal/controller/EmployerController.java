package com.example.elevateJobPortal.controller;

import com.example.elevateJobPortal.entity.Application;
import com.example.elevateJobPortal.entity.Job;
import com.example.elevateJobPortal.entity.User;
import com.example.elevateJobPortal.repository.ApplicationRepo;
import com.example.elevateJobPortal.repository.JobRepo;
import com.example.elevateJobPortal.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
   private ApplicationRepo applicationRepo;

    // ✅ Show only logged-in employer's jobs
    @GetMapping("/jobs")
    public String myJobs(Model model, Principal principal) {
        String username = principal.getName();
        User employer = userRepo.findByUsername(username);

        List<Job> jobs = jobRepo.findByEmployer(employer);
        model.addAttribute("jobs", jobs);

        return "employer/my_jobs";  // your Thymeleaf page
    }

    // ✅ When creating a job, set employer before saving
    @PostMapping("/jobs")
    public String createJob(@ModelAttribute Job job, Principal principal) {
        String username = principal.getName();
        User employer = userRepo.findByUsername(username);

        job.setEmployer(employer);  // important!
        jobRepo.save(job);

        return "redirect:/employer/jobs";
    }

    @GetMapping("/job/{jobId}/applicants")
    public String viewApplicants(@PathVariable Long jobId, Model model) {
        Job job = jobRepo.findById(jobId).orElseThrow();
        List<Application> applications = applicationRepo.findByJob(job);

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        model.addAttribute("count", applications.size());

        return "employer/applicants"; // new Thymeleaf page
    }

    @PostMapping("/application/{id}/approve")
    public String approveApplication(@PathVariable Long id) {
        Application app = applicationRepo.findById(id).orElseThrow();
        app.setStatus("APPROVED");
        applicationRepo.save(app);

        // ✅ redirect to applicants page for this job
        return "redirect:/employer/job/" + app.getJob().getId() + "/applicants";
    }

    @PostMapping("/application/{id}/reject")
    public String rejectApplication(@PathVariable Long id) {
        Application app = applicationRepo.findById(id).orElseThrow();
        app.setStatus("REJECTED");
        applicationRepo.save(app);

        // ✅ redirect to applicants page for this job
        return "redirect:/employer/job/" + app.getJob().getId() + "/applicants";
    }



}
