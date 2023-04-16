package SOLID_Principles.Interface_Segregation_Principle;

public class Document {
}

interface Machine {
    void print(Document d);
    void fax(Document d);
    void scan(Document d);
}

class MultiFunctionPrinter implements Machine {

    @Override
    public void print(Document d) {
        // implementations
    }

    @Override
    public void fax(Document d) {
        // implementations
    }

    @Override
    public void scan(Document d) {
        // implementations
    }
}

// We only want this class to PRINT since Old Fashioned Printers can only print,
// since Machine is our only interface we must now override those methods too.
// What should we do for fax and scan? leave them empty? then a user of our OldFashionedPrinter class would be very confused why it has methods that do nothing.
// ISP - It is better to break up into three separate interfaces instead of one big one.
class OldFashionedPrinter implements Machine{

    @Override
    public void print(Document d) {

    }

    @Override
    public void fax(Document d) {

    }

    @Override
    public void scan(Document d) {

    }
}

interface Printer {
    void print(Document d);
}

interface Faxxer {
    void fax(Document d);
}

interface Scanner {
    void scan(Document d);
}

// Grandmas printer is just like old fashioned printer from above, it can only print (not scan or fax)
class GrandmasPrinter implements Printer {

    @Override
    public void print(Document d) {
        System.out.println(d);
    }
}

// needs all print, fax, and scan functionality
class FancyPrinter implements Printer, Faxxer, Scanner {

    @Override
    public void print(Document d) {
        // implement methods
    }

    @Override
    public void fax(Document d) {
        // implement methods
    }

    @Override
    public void scan(Document d) {
        // implement methods
    }
}

// YAGNI = You Ain't Going to Need It
