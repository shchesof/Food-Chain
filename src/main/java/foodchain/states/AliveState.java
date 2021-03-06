package foodchain.states;

import foodchain.products.Product;

public class AliveState extends State {

    public AliveState() {
        stateName = "Alive";
    }

    @Override
    public void prepare(Product productContext) {
        productContext.setState(new RawState());
    }
}