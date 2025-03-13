package com.dathihida.controller;

import com.dathihida.model.Deal;
import com.dathihida.model.User;
import com.dathihida.response.ApiResponse;
import com.dathihida.service.DealService;
import com.dathihida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {
    private final DealService dealService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Deal> dealList = dealService.getDeals();
        return new ResponseEntity<>(dealList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) throws Exception {
        Deal createDeal = dealService.createDeal(deal);
        return new ResponseEntity<>(createDeal, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable Long id, @RequestBody Deal deal) throws Exception {
        Deal updateDeal = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(updateDeal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long id) throws Exception {
        dealService.deleteDeal(id);
        ApiResponse response = new ApiResponse();
        response.setMessage("Deal deleted");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

}
