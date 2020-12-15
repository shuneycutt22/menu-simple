import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

import static java.math.BigDecimal.valueOf;


/**
 * The HuneycuttTakeOut class maintains a menu for take-out food and
 * performs cost, tax, tip, and bill-splitting calculations
 *
 * Modified June 16, 2020
 * CSC 151 (MON02), Summer 2020
 * Guilford Technical Community College
 *
 * I abide by GTCC's academic integrity policy and certify that this is my own, original work
 *
 * @author Sam Huneycutt
 * @version 2020.6.22
 */
public class HuneycuttTakeOut {
    /**
     * Prompts user to enter number of orders, select food type, and select menu item.
     * Prints cost information to console
     *
     * @param args command line arguments (not used)
     * */
    public static void main(String[] args) {
        final int MAX_ORDERS = 100;                 //maximum number of people that can order
        final int MIN_ORDERS = 10;                  //minimum number of people that must order
        final int NUM_FOOD_TYPES = 3;               //number of types of food (i.e. Chinese, Italian, etc)
        final int NUM_ITALIAN_MENU_ITEMS = 3;       //number of Italian menu options
        final int NUM_CHINESE_MENU_ITEMS = 3;       //number of Chinese menu options
        final int NUM_AMERICAN_MENU_ITEMS = 3;      //number of American menu options
        final double TAX_RATE = 0.07;               //sales tax rate (decimal)
        final double TIP_RATE = 0.15;               //calculated tip rate (decimal)

        //create Scanner object to receive input
        var input = new Scanner(System.in);

        //Prompt user for number of people to order for
        //Only accept entries between MIN_ORDERS and MAX_ORDERS
        //If invalid number entered, prompt again
        System.out.print("Enter number of people to order: ");
        var numOrders = input.nextInt();

        while ((numOrders > MAX_ORDERS) || (numOrders < MIN_ORDERS)) {
            System.out.println("Invalid number of people!");
            System.out.println();
            System.out.print("Enter number of people to order: ");
            numOrders = input.nextInt();
        }
        System.out.println();

        //Prompt user to select type of food
        //User will enter a numeric value to make choice
        //Only accept entries between 1 and NUM_FOOD_TYPES
        //If invalid number entered, prompt again
        displayMainMenu();
        System.out.print("Choose what type of food to order (Enter a number): ");
        var orderType = input.nextInt();
        while (orderType > NUM_FOOD_TYPES || orderType < 1) {
            System.out.println("Invalid type!");
            System.out.println();
            System.out.print("Choose what type of food to order (Enter a number): ");
            orderType = input.nextInt();
        }
        System.out.println();

        int itemType;
        //Prompt user to select menu item
        //Display appropriate menu based on user's choice of food type
        //User will enter a numeric value to make choice
        //If invalid number entered, prompt again
        switch(orderType) {
            //Italian food
            case 1:
                displayItalianFoodMenu();
                System.out.print("Choose item (Enter a number): ");
                itemType = input.nextInt();
                while (itemType > NUM_ITALIAN_MENU_ITEMS || itemType < 1) {
                    System.out.println("Invalid item!");
                    System.out.println();
                    System.out.print("Choose item (Enter a number): ");
                    itemType = input.nextInt();
                }
                break;
            //Chinese food
            case 2:
                displayChineseFoodMenu();
                System.out.print("Choose item (Enter a number): ");
                itemType = input.nextInt();
                while (itemType > NUM_CHINESE_MENU_ITEMS || itemType < 1) {
                    System.out.println("Invalid item!");
                    System.out.println();
                    System.out.print("Choose item (Enter a number): ");
                    itemType = input.nextInt();
                }
                break;
            //American food
            case 3:
                displayAmericanFoodMenu();
                System.out.print("Choose item (Enter a number): ");
                itemType = input.nextInt();
                while (itemType > NUM_AMERICAN_MENU_ITEMS || itemType < 1) {
                    System.out.println("Invalid item!");
                    System.out.println();
                    System.out.print("Choose item (Enter a number): ");
                    itemType = input.nextInt();
                }
                break;
            default:
                itemType = 0;
        }
        System.out.println();

        //Combine food type (orderType) and menu item (itemType) into unique identifier
        //  example: orderType = 1; itemType = 1; -> itemType = 101
        //  system allows for up to 99 menu items for each food type
        itemType += orderType * 100;


        //Set name, cost, and quantities based on selected item
        String itemName;                        //name of item
        double itemCost;                        //cost of item (dollars, decimal)
        int numFeeds;                           //number of people fed per unit
        //Set values based on itemType identifier
        //If identifier unknown, terminate program
        switch(itemType) {
            case 101:
                itemName = "Lasagna Tray";
                itemCost = 17.99;
                numFeeds = 5;
                break;
            case 102:
                itemName = "Pizza Pack";
                itemCost = 15.99;
                numFeeds = 7;
                break;
            case 103:
                itemName = "Gazpacho Soup";
                itemCost = 12.99;
                numFeeds = 4;
                break;
            case 201:
                itemName = "Chicken and Broccoli Tray";
                itemCost = 18.99;
                numFeeds = 7;
                break;
            case 202:
                itemName = "Sweet and Sour Pork Tray";
                itemCost = 18.99;
                numFeeds = 7;
                break;
            case 203:
                itemName = "Shrimp Fried Rice Tray";
                itemCost = 10.99;
                numFeeds = 5;
                break;
            case 301:
                itemName = "Hamburger and Hot Dog Tray";
                itemCost = 21.99;
                numFeeds = 8;
                break;
            case 302:
                itemName = "Grilled Chicken Sandwich and Mozzarella Sticks Tray";
                itemCost = 22.99;
                numFeeds = 5;
                break;
            case 303:
                itemName = "Barbecue Tray";
                itemCost = 26.99;
                numFeeds = 10;
                break;
            default:
                itemName = "";
                itemCost = 0;
                numFeeds = 0;
                System.out.println("Internal error. Item not in database");
                System.exit(0);
                break;
        }

        //Determine number of trays needed to feed all people
        var numTrays = determineTrays(numOrders, numFeeds);

        //Determine subtotal (price before tax)
        var subtotal = getSubtotal(itemCost, numTrays);

        //Determine sales tax
        var tax = getTax(subtotal, TAX_RATE);

        //Determine tip for delivery driver
        var tip = getTip(subtotal, TIP_RATE);

        //Determine grand total
        var total = getGrandTotal(subtotal, tax, tip);

        //Determine price per person ordering
        var pricePerPerson = pricePerPerson(total, numOrders);

        //Determine number of leftover servings
        var leftovers = determineLeftOvers(numFeeds, numTrays, numOrders);


        //Output results to console
        printOutput(numTrays, itemName, numFeeds, numOrders, subtotal, tax, tip, total, pricePerPerson, leftovers);

    }

    
    /** Prints main menu to console */
    public static void displayMainMenu() {
        System.out.println("CHOOSE TYPE OF MEAL");
        System.out.println("--------------");
        System.out.println("1. Italian");
        System.out.println("2. Chinese");
        System.out.println("3. American");
    }


    /** Prints Italian food menu to console */
    public static void displayItalianFoodMenu() {
        System.out.println("CHOOSE ONE ITEM");
        System.out.println("--------------");
        System.out.println("1. Lasagna Tray - Feeds 5 - $17.99");
        System.out.println("2. Pizza Pack - Feeds 7 - $15.99");
        System.out.println("3. Gazpacho Soup, Salad, and Breadsticks Pack - Feeds 4 - $12.99");
    }


    /** Prints Chinese food menu to console */
    public static void displayChineseFoodMenu() {
        System.out.println("CHOOSE ONE ITEM");
        System.out.println("--------------");
        System.out.println("1. Chicken and Broccoli Tray (inc. 7 wonton soups, 7 egg rolls) - " +
                "Feeds 7 - $18.99");
        System.out.println("2. Sweet and Sour Pork Tray (inc. 7 hot and sour soups, 7 egg rolls) - " +
                "Feeds 7 - $18.99");
        System.out.println("3. Shrimp Fried Rice Tray (inc. 10 egg rolls) - Feeds 5 - $10.99");
    }


    /** Prints American food menu to console */
    public static void displayAmericanFoodMenu() {
        System.out.println("CHOOSE ONE ITEM");
        System.out.println("--------------");
        System.out.println("1. Hamburger and Hot Dog Tray (inc. buns and condiments) - Feeds 8 - $21.99");
        System.out.println("2. Grilled Chicken Sandwich and Mozzarella Sticks Tray (inc. dipping sauces) - "
                + "Feeds 5 - $22.99");
        System.out.println("3. Barbecue Tray (inc. buns and peach cobbler) - Feeds 10 - $26.99");
    }


    /**
     * Determines number of trays needed to fulfill expected number of orders
     * @param people    number of people requesting order
     * @param feeds number of people each tray feeds
     * @return number of trays needed
     */
    public static int determineTrays(int people, int feeds) {
        //Round up to nearest int. Leftover servings will be given away
        return (int)Math.ceil((double)people / feeds);
    }


    /**
     * Determines subtotal using BigDecimal to ensure accuracy. Multiplies price by number of trays
     * @param price price per tray
     * @param trays number of trays
     * @return subtotal (price for all trays)
     */
    public static double getSubtotal(double price, int trays) {
        var subtotal = new BigDecimal(price);
        //multiply cost per tray • number of trays
        subtotal = subtotal.multiply(valueOf(trays));
        return subtotal.doubleValue();
    }


    /**
     * Determines tax using BigDecimal to ensure accuracy. Multiplies subtotal by tax rate
     * @param subtotal  subtotal (price before tax)
     * @param taxRate   sales tax rate
     * @return  total tax charged
     */
    public static double getTax(double subtotal, double taxRate) {
        var tax = new BigDecimal(subtotal);
        //multiply subtotal • TAX_RATE
        tax = tax.multiply(valueOf(taxRate));
//        return tax.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return tax.doubleValue();
    }


    /**
     * Determines tip using BigDecimal to ensure accuracy. Multiplies subtotal by tip rate
     * @param subtotal  subtotal (price before tax)
     * @param tipRate   tip rate/percentage
     * @return  total tip charged
     */
    public static double getTip(double subtotal, double tipRate) {
        var tip = new BigDecimal(subtotal);
        //multiply subtotal • TIP_RATE
        tip = tip.multiply(valueOf(tipRate));
        return tip.doubleValue();
    }


    /**
     * Determines grand total using BigDecimal to ensure accuracy. Adds together subtotal, tax, and tip
     * @param subtotal subtotal (price before tax)
     * @param tax total tax charged
     * @param tip total tip charged
     * @return grand total (final price)
     */
    public static double getGrandTotal(double subtotal, double tax, double tip) {
        var total = new BigDecimal(subtotal);
        //add subtotal, tax, and tip
        total = total.add(valueOf(tax)).add(valueOf(tip));
        return total.doubleValue();
    }


    /**
     * Determines price per person using BigDecimal to ensure accuracy. Divides grand total by number of people
     * @param grandTotal    grand total (final price)
     * @param people    number of people ordering
     * @return  price per person ordering
     */
    public static double pricePerPerson(double grandTotal, int people) {
        var price = new BigDecimal(grandTotal);
        //divide grand total by number of people
        price = price.divide(valueOf(people), 2, RoundingMode.HALF_UP);
        return price.doubleValue();
    }


    /**
     * Determines leftover servings for delivery person.
     * @param feeds number of people each tray feeds
     * @param trays number of trays ordered
     * @param people    number of people ordering
     * @return number of leftover servings
     */
    public static int determineLeftOvers(int feeds, int trays, int people) {
        return (feeds * trays) - people;
    }


    /**
     * Prints formatted output statment to console
     * @param trays number of trays ordered
     * @param itemName  name of tray ordered
     * @param feeds number of people each tray feeds
     * @param people number of people ordering
     * @param subtotal subtotal (price before tax)
     * @param tax total tax charged
     * @param tip total tip charged
     * @param total grand total (final price)
     * @param pricePerPerson price per person
     * @param leftovers number of leftover servings
     */
    public static void printOutput(int trays, String itemName, int feeds, int people, double subtotal,
                                   double tax, double tip, double total, double pricePerPerson, int leftovers) {

        System.out.println("You need " + trays + " of " + "\"" + itemName + "\"");
        System.out.println("Each tray feeds " + feeds + " people");
        System.out.print("Subtotal - Price for " + people + " people (" + trays + " trays): $");
        System.out.printf("%.2f\n", subtotal);
        System.out.printf("Tax: $%.2f\n", tax);
        System.out.printf("Tip: $%.2f\n", tip);
        System.out.printf("TOTAL (inc. food, tax, tip): $%.2f\n", total);
        System.out.printf("Price per person: $%.2f\n", pricePerPerson);
        System.out.println("Leftover servings for the delivery driver: " + leftovers);

    }
}
