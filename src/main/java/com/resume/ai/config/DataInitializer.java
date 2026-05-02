package com.resume.ai.config;

import com.resume.ai.model.ERole;
import com.resume.ai.model.Role;
import com.resume.ai.model.User;
import com.resume.ai.repository.RoleRepository;
import com.resume.ai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.resume.ai.service.SeedService;
import com.resume.ai.service.JobService;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    SeedService seedService;

    @Autowired
    JobService jobService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize Roles if not exists
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(null, ERole.ROLE_ADMIN));
        }
        if (roleRepository.findByName(ERole.ROLE_RECRUITER).isEmpty()) {
            roleRepository.save(new Role(null, ERole.ROLE_RECRUITER));
        }
        if (roleRepository.findByName(ERole.ROLE_CANDIDATE).isEmpty()) {
            roleRepository.save(new Role(null, ERole.ROLE_CANDIDATE));
        }

        // Initialize a default admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(encoder.encode("admin123"));
            
            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
            roles.add(adminRole);
            admin.setRoles(roles);
            
            userRepository.save(admin);
            System.out.println("Default Admin User created: admin / admin123");
        }
        
        // Seed initial data
        try {
            int jobs = seedService.seedJobs(10);
            int resumes = seedService.seedResumesFromCsv(50);
            System.out.println("Seeded " + jobs + " jobs and " + resumes + " resumes.");
            
            // Auto-rank resumes for all jobs
            if (jobs > 0 || resumes > 0) {
                 System.out.println("Auto-ranking resumes for all jobs...");
                 jobService.getAllJobs().forEach(job -> {
                     try {
                         jobService.rankResumesForJob(job.getId());
                         System.out.println("Ranked resumes for job: " + job.getTitle());
                     } catch (Exception e) {
                         System.err.println("Failed to rank for job " + job.getId() + ": " + e.getMessage());
                     }
                 });
            }
        } catch (Exception e) {
            System.err.println("Seeding failed: " + e.getMessage());
        }
    }
}

