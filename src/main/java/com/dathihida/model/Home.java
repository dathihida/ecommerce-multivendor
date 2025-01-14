package com.dathihida.model;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Home {
    private List<HomeCategory> grid;

    private List<HomeCategory> shopByCategories;

    private List<HomeCategory> electricalCategories;

    private List<HomeCategory> dealCategories;

    private List<Deal> deals;
}
