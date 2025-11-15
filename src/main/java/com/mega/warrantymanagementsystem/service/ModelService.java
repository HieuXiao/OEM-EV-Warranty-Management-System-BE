package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Model;
import com.mega.warrantymanagementsystem.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {

    @Autowired
    private ModelRepository repo;

    public Model add(Model m) {
        if (repo.existsByModelName(m.getModelName())) {
            throw new RuntimeException("Model already exists");
        }
        return repo.save(m);
    }

    public Model update(Long id, String modelName) {
        Model m = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Model not found"));
        if (repo.existsByModelNameAndIdNot(modelName, id)) {
            throw new RuntimeException("Model name already in use");
        }
        m.setModelName(modelName);
        return repo.save(m);
    }

    public void remove(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Model not found");
        }
        repo.deleteById(id);
    }

    public List<Model> getAll() {
        return repo.findAll();
    }

    public Model getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Model not found"));
    }
}
