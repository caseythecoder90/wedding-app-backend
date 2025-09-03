package com.wedding.backend.wedding_app.controller;

import com.wedding.backend.wedding_app.annotations.FamilyGroupApiDocs;
import com.wedding.backend.wedding_app.dto.FamilyGroupResponseDTO;
import com.wedding.backend.wedding_app.dto.FamilyMemberResponseDTO;
import com.wedding.backend.wedding_app.model.request.FamilyGroupRequest;
import com.wedding.backend.wedding_app.model.request.FamilyMemberRequest;
import com.wedding.backend.wedding_app.service.FamilyGroupService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wedding.backend.wedding_app.util.WeddingServiceConstants.CREATED;

@RestController
@RequestMapping("/v1/api/family-groups")
@Tag(name = "Family Group Management", description = "APIs for managing wedding family groups and members")
@Slf4j
@RequiredArgsConstructor
public class FamilyGroupController {

    private final FamilyGroupService familyGroupService;

    @PostMapping
    @FamilyGroupApiDocs.CreateFamilyGroup
    public ResponseEntity<FamilyGroupResponseDTO> createFamilyGroup(@RequestBody FamilyGroupRequest request) {
        log.info("BEGIN - Creating family group: {}", request.getGroupName());
        
        FamilyGroupResponseDTO response = familyGroupService.createFamilyGroup(request);
        
        log.info("END - Family group {} successfully", CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @FamilyGroupApiDocs.GetAllFamilyGroups
    public ResponseEntity<List<FamilyGroupResponseDTO>> getAllFamilyGroups() {
        log.info("BEGIN - Fetching all family groups");
        
        List<FamilyGroupResponseDTO> response = familyGroupService.getAllFamilyGroups();
        
        log.info("END - Retrieved {} family groups", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @FamilyGroupApiDocs.GetFamilyGroupById
    public ResponseEntity<FamilyGroupResponseDTO> getFamilyGroupById(
            @Parameter(description = "The family group ID", required = true)
            @PathVariable Long id) {
        log.info("BEGIN - Fetching family group with ID: {}", id);
        
        FamilyGroupResponseDTO response = familyGroupService.getFamilyGroupById(id);
        
        log.info("END - Family group found: {}", response.getGroupName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/members")
    @FamilyGroupApiDocs.AddFamilyMember
    public ResponseEntity<FamilyMemberResponseDTO> addFamilyMember(
            @Parameter(description = "The family group ID", required = true)
            @PathVariable Long id,
            @RequestBody FamilyMemberRequest request) {
        log.info("BEGIN - Adding family member {} {} to family group: {}", 
                request.getFirstName(), request.getLastName(), id);
        
        FamilyMemberResponseDTO response = familyGroupService.addFamilyMember(id, request);
        
        log.info("END - Family member {} successfully", CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/members")
    @FamilyGroupApiDocs.GetFamilyMembers
    public ResponseEntity<List<FamilyMemberResponseDTO>> getFamilyMembers(
            @Parameter(description = "The family group ID", required = true)
            @PathVariable Long id) {
        log.info("BEGIN - Fetching family members for family group: {}", id);
        
        List<FamilyMemberResponseDTO> response = familyGroupService.getFamilyMembers(id);
        
        log.info("END - Retrieved {} family members", response.size());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @FamilyGroupApiDocs.DeleteFamilyGroup
    public ResponseEntity<Void> deleteFamilyGroup(
            @Parameter(description = "The family group ID", required = true)
            @PathVariable Long id) {
        log.info("BEGIN - Deleting family group with ID: {}", id);
        
        familyGroupService.deleteFamilyGroup(id);
        
        log.info("END - Family group deleted successfully");
        return ResponseEntity.noContent().build();
    }
}