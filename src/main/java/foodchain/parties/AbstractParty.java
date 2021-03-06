package foodchain.parties;

import foodchain.products.FoodFactory;
import foodchain.transactions.MoneyTransaction;
import foodchain.products.Product;
import foodchain.transactions.ProductTransaction;
import foodchain.states.State;
import foodchain.transactions.Transaction;
import foodchain.channels.PaymentChannel;
import foodchain.channels.SellingChannel;
import java.util.List;
import com.google.common.collect.ImmutableList;
import foodchain.strategies.AppleStrategy;
import foodchain.strategies.MilkStrategy;
import foodchain.strategies.PorkStrategy;
import foodchain.strategies.Strategy;

/**
 * Abstract class representing common attributes and methods for each party.
 * Must be abstract to avoid being instantiated directly.
 */
public abstract class AbstractParty implements Party {
    
    /* Demos are necessary to modify and then convert to immutable lists */

    /**
     * List of all transactions in the food chain to modify.
     */
    List<Transaction> demoTransactionsList;

    /**
     * Immutable list of all transactions in the food chain.
     */
    protected ImmutableList<Transaction> transactionsList;

    /**
     * List of all transactions of certain party to modify.
     */
    protected List<Transaction> demoOwnTransactionsList;

    /**
     * Immutable list of all transactions of certain party.
     */
    protected ImmutableList<Transaction> ownTransactionsList;

    /**
     * List of all products of certain party to modify.
     */
    protected List<Product> demoProductsList;

    /**
     * Immutable list of all products of certain party to modify.
     */
    protected ImmutableList<Product> productsList;

    /* Basic variables */
    protected boolean moneyReceived;
    protected Product currentRequestedProduct;
    protected Party currentRequestingParty;


    /**
     * Next party in chain of responsibility.
     */
    protected AbstractParty nextParty;

    protected String partyName;

    /* Double spending detection */
    protected boolean attemptToDoubleSpend = false;
    protected Integer attemptsNumber = 0;
    protected Strategy strategy;

    /**
     * Set next party in chain of responsibility.
     * @param next - next party in chain
     */
    @Override
    public void setNext(AbstractParty next) {
        nextParty = next;
    }

    /**
     * Get next party in chain of responsibility.
     * @return next party in chain
     */
    @Override
    public AbstractParty getNextParty() {
        return nextParty;
    }

    /**
     * Increases number of attempts to commit double-spending.
     */
    @Override
    public void increaseAttempts() {
        attemptsNumber++;
    }
    
    /**
     * Set boolean attemptToDoubleSpend to true.
     */
    @Override
    public void setDoubleSpending() {
        attemptToDoubleSpend = true;
    }
    
    /**
     * Adds recent transaction made somewhere in food chain
     * to the list of transactions of whole food chain of
     * current party.
     * @param transaction the transaction to add to the list
     */
    @Override
    public void updateTransactions(Transaction transaction) {
        int size = demoTransactionsList.size();
        if (size == 0) {
            transaction.setPreviousTransaction(null);
        }
        else {
            transaction.setPreviousTransaction(demoTransactionsList.get(size-1));
        }
        demoTransactionsList.add(transaction);
        transactionsList = ImmutableList.copyOf(demoTransactionsList);
    }
    
    /**
     * Ask next party in chain of responsibility for product
     * defined by its name.
     * @param productName name of product to make request for
     */
    @Override
    public void makeRequest(String productName) {
        nextParty.getRequest(productName, this);
    }

    /**
     * Receives money transaction from payment channel.
     * @param transaction the transaction to process
     */
    @Override
    public void receiveMoney(MoneyTransaction transaction) {
        Integer receivedMoney = transaction.getMoneyAmount();
        if (receivedMoney.equals(currentRequestedProduct.getPrice())) {
            moneyReceived = true;
            transaction.setSuccessful(true);
            System.out.println(transaction.getReceiver().getPartyName()+
                    " has received money: "+receivedMoney);
        }
        else {
            System.out.println("Not enough money!");
            transaction.setSuccessful(false);
        }
        addOwnTransaction(transaction);
        Party currentParty = currentRequestingParty;
        Party tmpParty = currentParty;
        while (currentParty != null) {
            tmpParty = currentParty;
            currentParty = currentParty.getCurrentRequestingParty();
        }
        currentParty = tmpParty;
        while (currentParty != null) {
            transaction.addParty(currentParty);
            currentParty = currentParty.getNextParty();
        }
        transaction.notifyAllParties();
    }

    /**
     * Gets request to make product defined by its name from party-sender.
     * @param productName the name of product
     * @param sender the sender who sent request
     */
    private void getRequest(String productName, Party sender) {
        if ((this.getPartyName()).equalsIgnoreCase("customer")) {
            System.out.println("Customer doesn't get requests!");
            return;
        }
        currentRequestingParty = sender;
        System.out.println("Current requested party: "+currentRequestingParty.getPartyName());
        if (productsList != null) {
            for (Product p : productsList) {
                if (p.getName().equalsIgnoreCase(productName)) {
                    if (!p.getCurrentlyProcessingParties().contains(this)) {
                        p.addCurrentlyProcessingParties(this);
                    }
                    System.out.println(this.getPartyName()+" already has "+p.getName());
                    currentRequestedProduct = p;
                    currentRequestedProduct.setIsReadyToTransmit(true);
                    return;
                }
            }
        }
        currentRequestedProduct = FoodFactory.makeProduct(productName);
    }
    
    /**
     * Transmits product to party-receiver by selling channel (only
     * if money for this product were received).
     * @param receiver the party-receiver to which the product is sent
     * @param product the product to send
     */
    protected void makeTransaction(Party receiver, Product product) {
        if (moneyReceived) {
            Transaction transaction = new ProductTransaction(receiver, this, product);
            Transaction tmpTransaction = transaction;
            SellingChannel channel = new SellingChannel(receiver);
            transaction = channel.makeTransmission(transaction);
            if (transaction == null) {
                System.out.println("Something went wrong!");
                tmpTransaction.setSuccessful(false);
                Party currentParty = currentRequestingParty;
                Party tmpParty = currentParty;
                while (currentParty != null) {
                    tmpParty = currentParty;
                    currentParty = currentParty.getCurrentRequestingParty();
                }
                currentParty = tmpParty;
                while (currentParty != null) {
                    tmpTransaction.addParty(currentParty);
                    currentParty = currentParty.getNextParty();
                }
                addOwnTransaction(tmpTransaction);
                tmpTransaction.notifyAllParties();
            }
            else {
                product.setIsCurrentlyProcessed(true);
                transaction.setSuccessful(true);
                Party currentParty = currentRequestingParty;
                while (currentParty != null) {
                    currentParty = currentParty.getCurrentRequestingParty();
                }
                while (currentParty != null) {
                    tmpTransaction.addParty(currentParty);
                    currentParty = currentParty.getNextParty();
                }
                addOwnTransaction(transaction);
                transaction.notifyAllParties();
            }
            moneyReceived = false;
        }
    }
    
    /**
     * Changes product's state to the next one in its processing.
     * @param product the product to which state is changed
     */
    public void prepareProductToNextStage(Product product) {
        State storedState = product.getState();
        storedState.prepare(product);
    }
    
    /**
     * Receives product transaction transmitted by selling channel.
     * @param transaction the transaction to process
     */
    @Override
    public void receiveProduct(ProductTransaction transaction) {
        Product product = transaction.getProduct();
        product.addCurrentlyProcessingParties(this);
    }
    
    /**
     * Send money to party which is currently requested to make
     * product by payment channel.
     * @param money value to make transaction
     */
    @Override
    public void makeTransaction(Integer money) {
        Transaction transaction = new MoneyTransaction(nextParty, this, money);
        Transaction tmpTransaction = transaction;
        PaymentChannel channel = new PaymentChannel(nextParty);
        transaction = channel.makeTransmission(transaction);
        if (transaction == null) {
            System.out.println("Something went wrong!");
            tmpTransaction.setSuccessful(false);
            addOwnTransaction(tmpTransaction);
        }
        else addOwnTransaction(transaction);
    }

    /**
     * Adds transaction to list of party's own transactions.
     * @param transaction the transaction to add to the list
     */
    private void addOwnTransaction(Transaction transaction) {
        int size = demoOwnTransactionsList.size();
        if (size == 0) {
            transaction.setPreviousTransaction(null);
        }
        else {
            transaction.setPreviousTransaction(demoOwnTransactionsList.get(size-1));
        }
        demoOwnTransactionsList.add(transaction);
        ownTransactionsList = ImmutableList.copyOf(demoOwnTransactionsList);
    }

    /**
     * Adds product in list of party's products.
     * @param product the product to add to the list
     */
    protected void addProduct(Product product) {
        demoProductsList.add(product);
        productsList = ImmutableList.copyOf(demoProductsList);
    }
    
    /**
     * Removes product from list of party's products.
     * @param product to process
     */
    @Override
    public void removeProduct(Product product) {
        demoProductsList.remove(product);
        productsList = ImmutableList.copyOf(demoProductsList);
    }

    /**
     * Gets the name of party.
     * @return name of party
     */
    @Override
    public String getPartyName() {
        return partyName;
    }

    /**
     * Gets the requesting party.
     * @return the requesting party
     */
    @Override
    public Party getCurrentRequestingParty() {
        return currentRequestingParty;
    }

    /**
     * Gets lists of all transactions.
     * @return list of all transactions in whole food chain
     */
    public ImmutableList<Transaction> getTransactionsList() {
        return transactionsList;
    }

    /**
     * Gets the list of all transactions of current party.
     * @return list of all transactions
     */
    public ImmutableList<Transaction> getOwnTransactionsList() {
        return ownTransactionsList;
    }

    /**
     * Gets lists of all products of current party.
     * @return list of all products
     */
    public ImmutableList<Product> getProductsList() {
        return productsList;
    }

    /**
     * Temporary function for simulation.
     * @param product to add in list of all products of current party
     */
    public void addProductToList(Product product) {
        addProduct(product);
    }

    /**
     * Creates iterator to walk over the parties.
     * @return new iterator of parties
     */
    public PartiesIterator iterator() {
        return new PartiesIterator(this);
    }
    
    /**
     * Choose appropriate strategy to process product.
     * @param product
     */
    protected void chooseStrategy(Product product) {
        String name = product.getName();
        if (name.equalsIgnoreCase("milk")) {
            strategy = new MilkStrategy(product);
        }
        else if (name.equalsIgnoreCase("apple")) {
            strategy = new AppleStrategy(product);
        }
        else if (name.equalsIgnoreCase("pork")) {
            strategy = new PorkStrategy(product);
        }
    }

}
