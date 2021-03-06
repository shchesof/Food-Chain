package foodchain.states;

import foodchain.products.Product;

public class PackedState extends State {

    public PackedState() {
        stateName = "Packed";
    }

    @Override
    public void prepare(Product productContext) {
        productContext.setState(new SoldState());
    }
}