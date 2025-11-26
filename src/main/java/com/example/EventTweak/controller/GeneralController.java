package com.example.EventTweak.controller;

import com.example.EventTweak.model.io.user.ApiResponse;
import com.example.EventTweak.service.GeneralService;
import com.example.EventTweak.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GeneralController {

    private GeneralService generalService;

    GeneralController(GeneralService generalService){
        this.generalService = generalService;
    }

    @PostMapping("/deleteImages")
    public ResponseEntity<Object> deleteImagesFromCloud(@RequestBody ArrayList<String> publicIds){
        return generalService.deleteImageFromCloud(publicIds);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<ApiResponse> home(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),"SuccessFull", null));
    }



}
