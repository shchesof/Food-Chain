package foodchain.parties;

import foodchain.transactions.MoneyTransaction;
import foodchain.products.Product;
import foodchain.transactions.ProductTransaction;
import foodchain.reporters.PartiesReporter;
import java.util.ArrayList;

public class Customer extends AbstractParty {

    public Customer() {
        currentRequestingParty = null;
        demoTransactionsList = new ArrayList<>();
        demoOwnTransactionsList = new ArrayList<>();
        demoProductsList = new ArrayList<>();
        partyName = "Customer";
        moneyReceived = false; // customer doesn't receive money
    }
    
    // process product as customer
    private void buyProduct(Product product) {
        super.prepareProductToNextStage(product);
        System.out.println("Product state in customer is "+product.getState().getStateName());
        System.out.println("CONGRATULATIONS, FOOD CHAIN IS COMPLETED");
        System.out.println("--------------------------------------------------");
        addProduct(product);
    }

    /**
     *
     * @param partiesReporter
     */
    @Override
    public void acceptReporter(PartiesReporter partiesReporter) {
        partiesReporter.generateReportForParty(this);
    }

    /**
     * Receives product transaction transmitted by selling channel and
     * process product as customer.
     * @param transaction
     */
    @Override
    public void receiveProduct(ProductTransaction transaction) {
        super.receiveProduct(transaction);
        Product product = transaction.getProduct();
        System.out.println("Customer has received "+product.getName());
        buyProduct(product);
    }

    /**
     * Only prints warning that customer doesn't receive money.
     * @param transaction
     */
    @Override
    public void receiveMoney(MoneyTransaction transaction) {
        System.out.println("Customer doesn't receive money, but pays!");
    }

    /**
     * Only prints warning that customer doesn't sell products.
     * @param receiver
     * @param product
     */
    @Override
    protected void makeTransaction(Party receiver, Product product) {
        System.out.println("Customer doesn't sell any products!");
    }
}