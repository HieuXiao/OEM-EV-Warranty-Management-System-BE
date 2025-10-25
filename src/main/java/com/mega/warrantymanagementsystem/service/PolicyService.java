package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Policy;
import com.mega.warrantymanagementsystem.model.request.PolicyRequest;
import com.mega.warrantymanagementsystem.model.response.PolicyResponse;
import com.mega.warrantymanagementsystem.repository.PolicyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý toàn bộ logic liên quan đến Policy.
 * Bao gồm CRUD, search cơ bản và update riêng field isEnable.
 */
@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Tạo mới policy.
     */
    public PolicyResponse create(PolicyRequest request) {
        Policy policy = modelMapper.map(request, Policy.class);
        Policy saved = policyRepository.save(policy);
        return modelMapper.map(saved, PolicyResponse.class);
    }

    /**
     * Cập nhật policy theo ID.
     */
    public PolicyResponse update(Integer id, PolicyRequest request) {
        Policy existing = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy không tồn tại!"));
        modelMapper.map(request, existing);
        Policy updated = policyRepository.save(existing);
        return modelMapper.map(updated, PolicyResponse.class);
    }

    /**
     * Cập nhật riêng field isEnable.
     */
    public PolicyResponse updateEnable(Integer id, Boolean isEnable) {
        Policy existing = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy không tồn tại!"));
        existing.setIsEnable(isEnable);
        Policy updated = policyRepository.save(existing);
        return modelMapper.map(updated, PolicyResponse.class);
    }

    /**
     * Xóa policy theo ID.
     */
    public void delete(Integer id) {
        if (!policyRepository.existsById(id)) {
            throw new RuntimeException("Policy không tồn tại!");
        }
        policyRepository.deleteById(id);
    }

    /**
     * Lấy tất cả policy.
     */
    public List<PolicyResponse> getAll() {
        return policyRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PolicyResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy policy theo ID.
     */
    public PolicyResponse getById(Integer id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy không tồn tại!"));
        return modelMapper.map(policy, PolicyResponse.class);
    }

    /**
     * Tìm theo tên.
     */
    public List<PolicyResponse> getByName(String name) {
        return policyRepository.findAll().stream()
                .filter(p -> p.getPolicyName() != null && p.getPolicyName().toLowerCase().contains(name.toLowerCase()))
                .map(p -> modelMapper.map(p, PolicyResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Tìm theo isEnable.
     */
    public List<PolicyResponse> getByEnable(Boolean enable) {
        return policyRepository.findAll().stream()
                .filter(p -> p.getIsEnable().equals(enable))
                .map(p -> modelMapper.map(p, PolicyResponse.class))
                .collect(Collectors.toList());
    }
}
