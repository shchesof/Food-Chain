package foodchain.products;

import foodchain.states.AliveState;
import java.util.ArrayList;
import java.util.HashMap;

public class Pork extends Product {

    public Pork() {
        this.state = new AliveState();
        price = 80;
        name = "Pork";
        demoStorageParametres = new HashMap<>();
        demoProcessorParametres = new HashMap<>();
        currentlyProcessingParties = new ArrayList<>();
        demoSellerParametres = new HashMap<>();
    }
}