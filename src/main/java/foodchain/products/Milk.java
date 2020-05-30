package foodchain.products;

import foodchain.states.CollectedState;
import java.util.ArrayList;
import java.util.HashMap;

public class Milk extends Product {

    public Milk() {
        this.state = new CollectedState();
        price = 45;
        name = "Milk";
        demoStorageParametres = new HashMap<>();
        demoProcessorParametres = new HashMap<>();
        currentlyProcessingParties = new ArrayList<>();
        demoSellerParametres = new HashMap<>();
    }
}