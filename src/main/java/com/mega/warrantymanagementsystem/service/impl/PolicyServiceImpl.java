//package com.mega.warrantymanagementsystem.service.impl;
//
//import com.mega.warrantymanagementsystem.entity.Policy;
//import com.mega.warrantymanagementsystem.model.request.PolicyRequest;
//import com.mega.warrantymanagementsystem.model.response.PolicyResponse;
//import com.mega.warrantymanagementsystem.repository.PolicyRepository;
//import com.mega.warrantymanagementsystem.service.PolicyService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class PolicyServiceImpl implements PolicyService {
//
//    @Autowired
//    private PolicyRepository policyRepository;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    //------------------Create Policy------------------------
//    @Override
//    public PolicyResponse createPolicy(PolicyRequest policyRequest) {
//        Policy policy = modelMapper.map(policyRequest, Policy.class);
//        Policy savedPolicy = policyRepository.save(policy);
//        return modelMapper.map(savedPolicy, PolicyResponse.class);
//    }
//
//    //------------------Update Policy------------------------
//    @Override
//    public PolicyResponse updatePolicy(int policyId, PolicyRequest policyRequest) {
//        Optional<Policy> optionalPolicy = policyRepository.findById(policyId);
//
//        if (optionalPolicy.isPresent()) {
//            Policy existingPolicy = optionalPolicy.get();
//            existingPolicy.setKilometer(policyRequest.getKilometer());
//            existingPolicy.setPolicyPart(policyRequest.getPolicyPart());
//            existingPolicy.setPolicyModel(policyRequest.getPolicyModel());
//            existingPolicy.setPolicyYear(policyRequest.getPolicyYear());
//            Policy updatedPolicy = policyRepository.save(existingPolicy);
//            return modelMapper.map(updatedPolicy, PolicyResponse.class);
//        } else {
//            throw new IllegalArgumentException("Policy not found with id: " + policyId);
//        }
//    }
//
//    //------------------Delete Policy------------------------
//    @Override
//    public void deletePolicy(int policyId) {
//        if (!policyRepository.existsById(policyId)) {
//            throw new IllegalArgumentException("Policy not found with id: " + policyId);
//        }
//        policyRepository.deleteById(policyId);
//    }
//
//    //------------------Get All Policies------------------------
//    @Override
//    public List<PolicyResponse> getAllPolicies() {
//        List<Policy> policies = policyRepository.findAll();
//        return policies.stream()
//                .map(policy -> modelMapper.map(policy, PolicyResponse.class))
//                .collect(Collectors.toList());
//    }
//
//    //------------------Get Policy By Id------------------------
//    @Override
//    public PolicyResponse getPolicyById(int policyId) {
//        Policy policy = policyRepository.findById(policyId)
//                .orElseThrow(() -> new IllegalArgumentException("Policy not found with id: " + policyId));
//        return modelMapper.map(policy, PolicyResponse.class);
//    }
//}
