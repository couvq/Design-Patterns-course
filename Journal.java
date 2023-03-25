import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.io.FileDescriptor.out;

/**
 * A Class to demo the Single Responsibility Principle (SRP),
 * allows us to keep track of our journal entries (Primary responsibility)
 */
public class Journal {
    private final List<String> entries = new ArrayList<>();
    private static int count = 0;

    public void addEntry(String text) {
        entries.add("" + (++count) + ":" + text);
    }

    public void removeEntry(int index) {
        entries.remove(index);
    }

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), entries);
    }

    // everything below violates single responsibility principle, as now the journal class has to handle
    // keeping track of journal entries as well as i/o filehandling/persistence for journal entries.
    // Let's make a new class called Persistence to handle this for us, so we do not violate the SRP
    public void save(String filename) throws FileNotFoundException {
        try (PrintStream out = new PrintStream(filename)) {
            out.println(toString());
        }
    }

    public void load(String filename) {}
    public void load(URL url) {}
}

class Persistence {
    public void saveToFile(Journal journal, String filename, boolean overwrite) throws FileNotFoundException {
        if(overwrite || new File(filename).exists()) {
            try (PrintStream out = new PrintStream(filename)) {
                out.println(journal.toString());
            }
        }
    }
}

class Demo {
    public static void main(String[] args) throws Exception {
        Journal j = new Journal();
        j.addEntry("I cried today");
        j.addEntry("I ate a bug");
        System.out.println(j);

        Persistence p = new Persistence();
        String home = System.getProperty("user.home");
        String filename = home + File.separator + "Desktop" + File.separator + "journal.txt";
        p.saveToFile(j, filename, true);
    }
}

/**
 * Follow up/summary of lesson
 * 1. We introduced a violation of the single responsibility principle when we made the Journal class handle to many concerns,
 *    introducing the functionality of saving journals
 * 2. We separated these two concerns by making the Journal class responsible for storing and manipulating journal entries,
 *    and the Persistence class responsible for Saving and loading of journals from storage. By doing this both classes
 *    now have a single responsibility.
 */
