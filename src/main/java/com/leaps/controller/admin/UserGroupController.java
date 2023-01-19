package com.leaps.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leaps.dto.admin.UserGroupDto;
import com.leaps.responses.admin.UserGroupResponse;
import com.leaps.serviceImpl.admin.UserGroupServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/userGroup")
public class UserGroupController {

    private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
    private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
    private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
    private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

    @Autowired
    private UserGroupServiceImpl userGroupService;

    // Get-all with Pagination
    @GetMapping("/getall-pagination")
    public ResponseEntity<UserGroupResponse> getallWithPagination(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        HttpHeaders header = new HttpHeaders();
        UserGroupResponse usergroup = userGroupService.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
        header.add("description", "Usergroup-getall-pagination");
        return new ResponseEntity<>(usergroup, header, HttpStatus.OK);
        // return ResponseEntity.status(HttpStatus.OK).headers(header).body(usergroup);
    }

    // Get-all
    @GetMapping("/getall")
    public ResponseEntity<List<UserGroupDto>> getall() {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "getall");
        List<UserGroupDto> userGroup = userGroupService.getall();
        return new ResponseEntity<>(userGroup, header, HttpStatus.OK);
    }

    // Get By Id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<UserGroupDto> getbyid(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "get-by-id");
        header.add("type", "UserGroup Object");
        UserGroupDto usergroup = userGroupService.getbyid(id);
        return new ResponseEntity<>(usergroup, header, HttpStatus.OK);
    }

    // Add
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody UserGroupDto userGroupDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "add-userGroup");
        String add = userGroupService.add(userGroupDto);
        return new ResponseEntity<>(add, header, HttpStatus.CREATED);
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@Valid @PathVariable Long id, @RequestBody UserGroupDto userGroupDto) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "update-userGroup");
        String update = userGroupService.update(id, userGroupDto);
        return new ResponseEntity<>(update, header, HttpStatus.OK);
    }

    // Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "soft-delete-userGroup");
        String softdelete = userGroupService.softdelete(id);
        return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "hard-delete-userGroup");
        String harddelete = userGroupService.harddelete(id);
        return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
    }

    // Global Search
    @GetMapping("/global-search/{key}")
    public ResponseEntity<List<UserGroupDto>> globalSearch(String key) {
        HttpHeaders header = new HttpHeaders();
        header.add("description", "global-search-userGroup");
        List<UserGroupDto> globalSearch = userGroupService.globalSearch(key);
        return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
    }

}
