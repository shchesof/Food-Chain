package foodchain.states;

import foodchain.products.Product;

public class RawState extends State {

    public RawState() {
        stateName = "Raw";
    }

    @Override
    public void prepare(Product productContext) {
        productContext.setState(new StoredState());
    }
}