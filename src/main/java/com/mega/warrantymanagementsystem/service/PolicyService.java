package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.PartUnderWarranty;
import com.mega.warrantymanagementsystem.entity.Policy;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.PolicyRequest;
import com.mega.warrantymanagementsystem.model.response.PolicyResponse;
import com.mega.warrantymanagementsystem.repository.PartUnderWarrantyRepository;
import com.mega.warrantymanagementsystem.repository.PolicyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private PartUnderWarrantyRepository partUnderWarrantyRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Tạo mới policy.
     */
    public PolicyResponse create(PolicyRequest request) {
        // kiểm tra trùng tên policy trước khi tạo
        boolean exists = policyRepository.findAll().stream()
                .anyMatch(p -> p.getPolicyName() != null && p.getPolicyName().equalsIgnoreCase(request.getPolicyName()));
        if (exists) {
            throw new DuplicateResourceException("Policy name already exists: " + request.getPolicyName());
        }

        // map phần cơ bản
        Policy policy = new Policy();
        policy.setPolicyName(request.getPolicyName());
        policy.setAvailableYear(request.getAvailableYear());
        policy.setKilometer(request.getKilometer());
        policy.setIsEnable(request.getIsEnable() != null ? request.getIsEnable() : Boolean.TRUE);

        // nếu client gửi partId thì phải lấy PartUnderWarranty từ DB
        if (request.getPartId() != null && !request.getPartId().isBlank()) {
            PartUnderWarranty part = partUnderWarrantyRepository.findById(request.getPartId())
                    .orElseThrow(() -> new ResourceNotFoundException("PartUnderWarranty not found: " + request.getPartId()));
            policy.setPartUnderWarranty(part);
        }

        Policy saved = policyRepository.save(policy);
        return modelMapper.map(saved, PolicyResponse.class);
    }

    /**
     * Cập nhật policy theo ID.
     */
    public PolicyResponse update(Integer id, PolicyRequest request) {
        Policy existing = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy does not exist: " + id));

        // check trùng tên policy nếu client gửi tên mới
        if (request.getPolicyName() != null) {
            boolean exists = policyRepository.findAll().stream()
                    .anyMatch(p -> p.getPolicyId() != id
                            && p.getPolicyName() != null
                            && p.getPolicyName().equalsIgnoreCase(request.getPolicyName()));
            if (exists) {
                throw new DuplicateResourceException("Policy name already exists: " + request.getPolicyName());
            }
            existing.setPolicyName(request.getPolicyName());
        }

        if (request.getAvailableYear() != null) existing.setAvailableYear(request.getAvailableYear());
        if (request.getKilometer() != null) existing.setKilometer(request.getKilometer());
        if (request.getIsEnable() != null) existing.setIsEnable(request.getIsEnable());

        // xử lý partId nếu có
        if (request.getPartId() != null) {
            if (request.getPartId().isBlank()) {
                existing.setPartUnderWarranty(null); // clear association
            } else {
                PartUnderWarranty part = partUnderWarrantyRepository.findById(request.getPartId())
                        .orElseThrow(() -> new ResourceNotFoundException("PartUnderWarranty not found: " + request.getPartId()));
                existing.setPartUnderWarranty(part);
            }
        }

        Policy updated = policyRepository.save(existing);
        return modelMapper.map(updated, PolicyResponse.class);
    }

    /**
     * Cập nhật riêng field isEnable.
     */
    public PolicyResponse updateEnable(Integer id, Boolean isEnable) {
        Policy existing = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy does not exist: " + id));
        existing.setIsEnable(isEnable);
        Policy updated = policyRepository.save(existing);
        return modelMapper.map(updated, PolicyResponse.class);
    }

    /**
     * Xóa policy theo ID.
     */
    public void delete(Integer id) {
        if (!policyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Policy does not exist: " + id);
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
                .orElseThrow(() -> new ResourceNotFoundException("Policy does not exist: " + id));
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
                .filter(p -> p.getIsEnable() != null && p.getIsEnable().equals(enable))
                .map(p -> modelMapper.map(p, PolicyResponse.class))
                .collect(Collectors.toList());
    }
}
