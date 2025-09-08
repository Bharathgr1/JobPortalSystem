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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/applicant")
public class ApplicantController {

    @Autowired
    private ApplicationRepo applicationRepo;

    @Autowired
    private JobRepo jobRepo;


    // Display all jobs so applicant can apply
    @GetMapping("/jobs")
    public String viewJobs(Model model) {
        model.addAttribute("jobs", jobRepo.findAll());
        return "applicant/jobs"; // new template
    }

    @GetMapping("/my_apps")
    public String myApplications(Model model, Principal principal) {

        System.out.println("entered");
        // Get logged-in user
        User currentUser = userRepo.findByUsername(principal.getName());

        // Fetch applications of this user
        model.addAttribute("applications", applicationRepo.findByApplicant(currentUser));

        return "applicant/my_apps";
    }


    @Autowired
    private UserRepo userRepo;


    // Apply for a job
    // Apply for a job
    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId, Principal principal) {

        User currentUser = userRepo.findByUsername(principal.getName());
        Job job = jobRepo.findById(jobId).orElseThrow();

        Application app = new Application();
        app.setApplicant(currentUser);
        app.setJob(job);
        app.setStatus("APPLIED");

        applicationRepo.save(app);

        return "redirect:/applicant/my_apps";
    }

    @GetMapping("/available_jobs")
    public String availableJobs(Model model, Principal principal) {


        System.out.println("no one");
        User applicant = userRepo.findByUsername(principal.getName());

        // fetch jobs NOT applied by this applicant
        List<Job> allJobs = jobRepo.findAll();
        List<Application> myApps = applicationRepo.findByApplicant(applicant);
        Set<Long> appliedJobIds = myApps.stream()
                .map(app -> app.getJob().getId())
                .collect(Collectors.toSet());

        List<Job> availableJobs = allJobs.stream()
                .filter(job -> !appliedJobIds.contains(job.getId()))
                .toList();

        model.addAttribute("jobs", availableJobs);
        return "available_jobs";
    }

    @GetMapping("/application/{id}")
    public String viewApplicationStatus(@PathVariable Long id, Model model) {
        Application app = applicationRepo.findById(id).orElseThrow();

        System.out.println(app.getStatus());
        System.out.println("DEBUG JOB = " + (app.getJob() != null ? app.getJob().getTitle() : "NULL"));

        // force load Job if lazy
        if (app.getJob() != null) {
            Job job = jobRepo.findById(app.getJob().getId()).orElse(null);
            app.setJob(job);
        }

        model.addAttribute("application1", app);
        return "applicant/application_status";
    }


}