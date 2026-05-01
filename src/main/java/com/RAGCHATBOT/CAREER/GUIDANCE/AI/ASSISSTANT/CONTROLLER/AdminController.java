package com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.CONTROLLER;

import com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.ENTITY.Career;
import com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.REPOSITORY.CareerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private CareerRepository careerRepository;

    // ✅ Add career
    @PostMapping("/career")
    public ResponseEntity<String> addCareer(@RequestBody Career career) {

        if (career.getRole() == null || career.getRole().isEmpty()) {
            return ResponseEntity.badRequest().body("Role is required");
        }

        careerRepository.save(career);

        return ResponseEntity.ok("Career added successfully");
    }

    // ✅ Get all careers
    @GetMapping("/careers")
    public List<Career> getAllCareers() {
        return careerRepository.findAll();
    }

    // ✅ Delete career
    @DeleteMapping("/career/{id}")
    public String deleteCareer(@PathVariable Long id) {

        careerRepository.deleteById(id);

        return "Career deleted successfully";
    }

}
