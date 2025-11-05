package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.exception.exception.BusinessLogicException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.CampaignReportRequest;
import com.mega.warrantymanagementsystem.model.response.CampaignReportResponse;
import com.mega.warrantymanagementsystem.model.response.CampaignResponse;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service xử lý nghiệp vụ cho CampaignReport:
 * - Upload PDF lên Cloudinary
 * - Lưu vào DB với các quan hệ (Campaign, ServiceCenter, Account)
 * - Tìm kiếm / cập nhật / xóa / lọc theo thời gian
 * - Quản lý đồng bộ file giữa DB và Cloudinary
 */
@Service
public class CampaignReportService {

    @Autowired
    private CampaignReportRepository campaignReportRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ServiceCenterRepository serviceCenterRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper modelMapper;

    // Map: fileUrl -> publicId (để dễ quản lý khi xóa)
    private final Map<String, String> filePublicIdMap = new HashMap<>();

    // ==================== CREATE ====================
    @Transactional
    public CampaignReportResponse createReport(CampaignReportRequest request, List<MultipartFile> pdfFiles) {
        validatePdfFiles(pdfFiles);

        Campaign campaign = campaignRepository.findById(request.getCampaignId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Campaign ID: " + request.getCampaignId()));

        ServiceCenter center = serviceCenterRepository.findById(request.getServiceCenterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ServiceCenter ID: " + request.getServiceCenterId()));

        List<Account> submitters = request.getSubmittedByIds().stream()
                .map(id -> accountRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Account ID: " + id)))
                .collect(Collectors.toList());

        CampaignReport report = new CampaignReport();
        report.setCampaign(campaign);
        report.setServiceCenter(center);
        report.setSubmittedBy(submitters);
        report.setOriginalFileName(request.getOriginalFileName());
        report.setSubmittedAt(request.getSubmittedAt() != null ? request.getSubmittedAt() : LocalDateTime.now());

        // Upload PDF lên Cloudinary và lưu public_id
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : pdfFiles) {
            Map uploadResult = cloudinaryService.uploadFileWithResult(file, "campaign_reports");
            String fileUrl = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();
            fileUrls.add(fileUrl);
            filePublicIdMap.put(fileUrl, publicId);
        }

        report.setReportFileUrls(fileUrls);

        CampaignReport saved = campaignReportRepository.save(report);
        return mapToResponse(saved);
    }

    // ==================== UPDATE ====================
    @Transactional
    public CampaignReportResponse update(int reportId, CampaignReportRequest request) {
        CampaignReport existing = campaignReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CampaignReport ID: " + reportId));

        if (request.getCampaignId() != null) {
            Campaign campaign = campaignRepository.findById(request.getCampaignId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Campaign ID: " + request.getCampaignId()));
            existing.setCampaign(campaign);
        }

        if (request.getServiceCenterId() != null) {
            ServiceCenter center = serviceCenterRepository.findById(request.getServiceCenterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ServiceCenter ID: " + request.getServiceCenterId()));
            existing.setServiceCenter(center);
        }

        if (request.getSubmittedByIds() != null && !request.getSubmittedByIds().isEmpty()) {
            List<Account> submitters = request.getSubmittedByIds().stream()
                    .map(id -> accountRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Account ID: " + id)))
                    .collect(Collectors.toList());
            existing.setSubmittedBy(submitters);
        }

        // Nếu có danh sách file mới, xóa file cũ trên Cloudinary
        if (request.getReportFileUrls() != null && !request.getReportFileUrls().isEmpty()) {
            for (String oldUrl : existing.getReportFileUrls()) {
                String publicId = filePublicIdMap.get(oldUrl);
                if (publicId != null) cloudinaryService.deleteFile(publicId);
            }
            existing.setReportFileUrls(request.getReportFileUrls());
        }

        existing.setOriginalFileName(request.getOriginalFileName());
        existing.setSubmittedAt(LocalDateTime.now());

        CampaignReport updated = campaignReportRepository.save(existing);
        return mapToResponse(updated);
    }

    // ==================== GET ALL ====================
    public List<CampaignReportResponse> getAll() {
        return campaignReportRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==================== GET BY ID ====================
    public CampaignReportResponse getById(int reportId) {
        CampaignReport report = campaignReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CampaignReport ID: " + reportId));
        return mapToResponse(report);
    }

    // ==================== SEARCH ====================
    public List<CampaignReportResponse> getByCampaignId(int campaignId) {
        return campaignReportRepository.findByCampaign_CampaignId(campaignId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CampaignReportResponse> getByServiceCenterId(int centerId) {
        return campaignReportRepository.findByServiceCenter_CenterId(centerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CampaignReportResponse> getByAccountId(String accountId) {
        return campaignReportRepository.findBySubmittedBy_AccountId(accountId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CampaignReportResponse> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return campaignReportRepository.findAll().stream()
                .filter(r -> !r.getSubmittedAt().isBefore(start) && !r.getSubmittedAt().isAfter(end))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==================== DELETE ====================
    @Transactional
    public void delete(int reportId) {
        CampaignReport report = campaignReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CampaignReport ID: " + reportId));

        // Xóa file PDF trên Cloudinary
        if (report.getReportFileUrls() != null) {
            for (String fileUrl : report.getReportFileUrls()) {
                String publicId = filePublicIdMap.get(fileUrl);
                if (publicId != null) cloudinaryService.deleteFile(publicId);
            }
        }

        campaignReportRepository.delete(report);
    }

    // ==================== MAPPER ====================
    private CampaignReportResponse mapToResponse(CampaignReport report) {
        CampaignReportResponse response = new CampaignReportResponse();

        // map cơ bản
        response.setReportId(report.getReportId());
        response.setReportFileUrls(report.getReportFileUrls());
        response.setOriginalFileName(report.getOriginalFileName());
        response.setSubmittedAt(report.getSubmittedAt());

        // map object thay vì id
        response.setCampaignId(modelMapper.map(report.getCampaign(), CampaignResponse.class));
        response.setServiceCenterId(modelMapper.map(report.getServiceCenter(), ServiceCenterResponse.class));

        // map danh sách accountId
        if (report.getSubmittedBy() != null) {
            response.setSubmittedByIds(
                    report.getSubmittedBy()
                            .stream()
                            .map(Account::getAccountId)
                            .collect(Collectors.toList())
            );
        }

        return response;
    }


    // ==================== VALIDATION ====================
    private void validatePdfFiles(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                throw new BusinessLogicException("Chỉ được phép upload file PDF, file '" + file.getOriginalFilename() + "' không hợp lệ.");
            }
        }
    }

    //================Special===========
    @Transactional
    public CampaignReportResponse updateWithFiles(int reportId, CampaignReportRequest request, List<MultipartFile> pdfFiles) {
        // Nếu không có file mới => gọi luôn update() cũ
        if (pdfFiles == null || pdfFiles.isEmpty()) {
            return update(reportId, request);
        }

        // Nếu có file mới => upload rồi set vào request
        List<String> newUrls = cloudinaryService.uploadFiles(pdfFiles, "campaign_reports");
        request.setReportFileUrls(newUrls);

        // Gọi lại method update() để tái sử dụng toàn bộ logic update cũ
        return update(reportId, request);
    }


}
