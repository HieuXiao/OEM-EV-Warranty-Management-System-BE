package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.Model;
import com.mega.warrantymanagementsystem.model.request.ModelRequest;
import com.mega.warrantymanagementsystem.service.ModelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/model")
@CrossOrigin
@SecurityRequirement(name = "api")
public class ModelController {

    @Autowired
    private ModelService service;

    @PostMapping
    public Model add(@RequestBody ModelRequest req) {
        Model m = new Model();
        m.setModelName(req.getModelName());
        return service.add(m);
    }

    @PutMapping("/{id}")
    public Model update(@PathVariable Long id, @RequestBody ModelRequest req) {
        return service.update(id, req.getModelName());
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        service.remove(id);
    }

    @GetMapping
    public List<Model> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Model getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
