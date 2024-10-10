package com.example.spring_elastic.controller;

import com.example.spring_elastic.model.EmployeeMongo;
import com.example.spring_elastic.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Value("${syncfusion.license.key}")
    private String syncfusionLicenseKey;

    // Fetch all employees and return to employee-list.jsp
    @GetMapping("/list")
    public String viewEmployeesList(Model model) {
        List<EmployeeMongo> employees = employeeService.getAllEmployeesFromMongo();
        model.addAttribute("employees", employees);
        model.addAttribute("syncfusionLicenseKey", syncfusionLicenseKey);  // Pass the license key to the view
        return "employee-list";
    }

    // Show form to add new employee
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeMongo());
        model.addAttribute("syncfusionLicenseKey", syncfusionLicenseKey);
        return "employee-form";
    }

    // Save employee data (handles both Add and Edit)
    @PostMapping("/save")
    @ResponseBody
    public String saveEmployee(@RequestBody EmployeeMongo employee) throws IOException {
        employeeService.saveEmployee(employee);  // Save to MongoDB and Elasticsearch
        return "Employee saved successfully";
    }

    // Delete employee by ID
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteEmployee(@PathVariable String id) throws IOException {
        employeeService.deleteEmployeeById(id);  // Delete from MongoDB and Elasticsearch
        return "Employee deleted successfully";
    }
}
