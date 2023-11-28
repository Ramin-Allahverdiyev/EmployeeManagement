package com.EmployeeManagement.controller;

import com.EmployeeManagement.dto.request.PositionRequest;
import com.EmployeeManagement.dto.response.PositionResponse;
import com.EmployeeManagement.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${root.url}/position")
public class PositionController {
    private final PositionService positionService;

    @PostMapping("/save")
    public PositionResponse savePosition(@RequestBody PositionRequest request){
        return positionService.savePosition(request);
    }

    @GetMapping("/{id}")
    public PositionResponse getPosition(@PathVariable int id){
        return positionService.getPosition(id);
    }

    @PutMapping("/{id}")
    public PositionResponse updatePosition(@PathVariable int id, @RequestBody PositionRequest request){
        return positionService.updatePosition(id,request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePosition(@PathVariable int id){
        positionService.deletePosition(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/all")
    public List<PositionResponse> getAllPositions(){
        return positionService.getAllPositions();
    }

}
