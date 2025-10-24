package com.mega.warrantymanagementsystem.config;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.model.response.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // dùng STRICT để tránh map nhầm field
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        // ===== Converter an toàn để lấy ID khi quan hệ không null =====
        Converter<ClaimReplacementPart, Integer> claimReplacementToClaimId =
                ctx -> ctx.getSource() != null && ctx.getSource().getWarrantyClaim() != null
                        ? ctx.getSource().getWarrantyClaim().getClaimId()
                        : null;

        Converter<ClaimReplacementPart, Integer> claimReplacementToPartId =
                ctx -> ctx.getSource() != null && ctx.getSource().getPart() != null
                        ? ctx.getSource().getPart().getPartId()
                        : null;

        Converter<ClaimAttachment, Integer> claimAttachmentToClaimId =
                ctx -> ctx.getSource() != null && ctx.getSource().getWarrantyClaim() != null
                        ? ctx.getSource().getWarrantyClaim().getClaimId()
                        : null;

        Converter<ServiceRecord, String> serviceRecordToVin =
                ctx -> ctx.getSource() != null && ctx.getSource().getVehicle() != null
                        ? ctx.getSource().getVehicle().getVin()
                        : null;

        Converter<ServiceRecord, Integer> serviceRecordToAppointmentId =
                ctx -> ctx.getSource() != null && ctx.getSource().getServiceAppointment() != null
                        ? ctx.getSource().getServiceAppointment().getAppointmentId()
                        : null;

        Converter<ServiceRecord, Integer> serviceRecordToCampaignId =
                ctx -> ctx.getSource() != null && ctx.getSource().getCampaign() != null
                        ? ctx.getSource().getCampaign().getCampaignId()
                        : null;

        Converter<ServiceRecord, Integer> serviceRecordToCenterId =
                ctx -> ctx.getSource() != null && ctx.getSource().getServiceCenter() != null
                        ? ctx.getSource().getServiceCenter().getCenterId()
                        : null;

        Converter<WarrantyClaim, String> claimToVin =
                ctx -> ctx.getSource() != null && ctx.getSource().getVehicle() != null
                        ? ctx.getSource().getVehicle().getVin()
                        : null;

        Converter<WarrantyClaim, String> claimToStaffId =
                ctx -> ctx.getSource() != null && ctx.getSource().getServiceCenterStaff() != null
                        ? ctx.getSource().getServiceCenterStaff().getAccountId()
                        : null;

        Converter<WarrantyClaim, String> claimToTechnicianId =
                ctx -> ctx.getSource() != null && ctx.getSource().getServiceCenterTechnician() != null
                        ? ctx.getSource().getServiceCenterTechnician().getAccountId()
                        : null;

        Converter<WarrantyClaim, String> claimToEvmId =
                ctx -> ctx.getSource() != null && ctx.getSource().getEvm() != null
                        ? ctx.getSource().getEvm().getAccountId()
                        : null;

        Converter<WarrantyClaim, Integer> claimToPolicyId =
                ctx -> ctx.getSource() != null && ctx.getSource().getPolicy() != null
                        ? ctx.getSource().getPolicy().getPolicyId()
                        : null;

        // ===== Mapping ClaimReplacementPart → ClaimReplacementPartResponse =====
        mapper.addMappings(new PropertyMap<ClaimReplacementPart, ClaimReplacementPartResponse>() {
            @Override
            protected void configure() {
                using(claimReplacementToClaimId).map(source).setClaimId(null);
                using(claimReplacementToPartId).map(source).setPartId(null);
            }
        });

        // ===== Mapping ClaimAttachment → ClaimAttachmentResponse =====
        mapper.addMappings(new PropertyMap<ClaimAttachment, ClaimAttachmentResponse>() {
            @Override
            protected void configure() {
                using(claimAttachmentToClaimId).map(source).setClaimId(null);
            }
        });

        // ===== Mapping ServiceRecord → ServiceRecordResponse =====
        mapper.addMappings(new PropertyMap<ServiceRecord, ServiceRecordResponse>() {
            @Override
            protected void configure() {
                using(serviceRecordToVin).map(source).setVin(null);
                using(serviceRecordToAppointmentId).map(source).setServiceAppointmentId(0);
                using(serviceRecordToCampaignId).map(source).setCampaignId(0);
                using(serviceRecordToCenterId).map(source).setServiceCenterId(0);
            }
        });

        // ===== Mapping WarrantyClaim → WarrantyClaimResponse =====
        mapper.addMappings(new PropertyMap<WarrantyClaim, WarrantyClaimResponse>() {
            @Override
            protected void configure() {
                using(claimToVin).map(source).setVin(null);
                using(claimToStaffId).map(source).setScStaffId(null);
                using(claimToTechnicianId).map(source).setScTechnicianId(null);
                using(claimToEvmId).map(source).setEvmId(null);
                using(claimToPolicyId).map(source).setPolicyId(0);
            }
        });

        return mapper;
    }
}
