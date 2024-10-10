package com.example.spring_elastic.service;

import com.example.spring_elastic.model.EmployeeMongo;
import com.example.spring_elastic.repository.EmployeeMongoRepository;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMongoRepository mongoRepository;

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    // Save Employee to MongoDB and Elasticsearch with manual ID
    public String saveEmployee(EmployeeMongo employeeMongo) throws IOException {
        // Save to MongoDB
        EmployeeMongo savedEmployee = mongoRepository.save(employeeMongo);

        // Save to Elasticsearch
        IndexRequest indexRequest = new IndexRequest("employees")
                .id(savedEmployee.getId())  // Use custom/manual ID
                .source("name", savedEmployee.getName(),
                        "department", savedEmployee.getDepartment(),
                        "salary", savedEmployee.getSalary());
        IndexResponse indexResponse = elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);

        return savedEmployee.getId();
    }

    // Get an employee by ID from MongoDB
    public Optional<EmployeeMongo> getEmployeeFromMongoDBById(String id) {
        return mongoRepository.findById(id);
    }

    // Get all employees from MongoDB
    public List<EmployeeMongo> getAllEmployeesFromMongo() {
        return mongoRepository.findAll();
    }

    // Delete an employee by ID from MongoDB and Elasticsearch
    public void deleteEmployeeById(String id) throws IOException {
        mongoRepository.deleteById(id);
        elasticsearchClient.delete(new DeleteRequest("employees", id), RequestOptions.DEFAULT);
    }
}
