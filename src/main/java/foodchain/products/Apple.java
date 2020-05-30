package foodchain.products;

import foodchain.states.GrowingState;
import java.util.ArrayList;
import java.util.HashMap;

public class Apple extends Product {

    public Apple() {
        this.state = new GrowingState();
        price = 20;
        name = "Apple";
        demoStorageParametres = new HashMap<>();
        demoProcessorParametres = new HashMap<>();
        currentlyProcessingParties = new ArrayList<>();
        demoSellerParametres = new HashMap<>();
    }
}
