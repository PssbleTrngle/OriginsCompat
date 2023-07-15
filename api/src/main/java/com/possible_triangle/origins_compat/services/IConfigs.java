package com.possible_triangle.origins_compat.services;

public interface IConfigs {

    Server server();

    interface Server {
        int waterInBacktank();
        int enchantedBacktankCapacity();
    }

}
