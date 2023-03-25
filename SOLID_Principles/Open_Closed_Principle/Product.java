package SOLID_Principles.Open_Closed_Principle;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

enum Color {
    RED, GREEN, BLUE
}

enum Size {
    SMALL, MEDIUM, LARGE, YUGE
}

enum Price {
    LOW, MEDIUM, HIGH
}


public class Product {
    public String name;
    public Color color;
    public Size size;

    public Price price;

    public Product(String name, Color color, Size size, Price price) {
        this.name = name;
        this.color = color;
        this.size = size;
        this.price = price;
    }
}

class ProductFilter {
    // three functions below break open closed principle (imagine if you had a ton of filter criteria -> would you make a function for each?
    // this violates ocp because if we want to add sort criteria and filter them we would have to enhance this class that we already made
    // we will fix this using specification pattern
    public Stream<Product> filterByColor(List<Product> products, Color color) {
        return products.stream()
                .filter(product -> product.color == color);
    }

    public Stream<Product> filterBySize(List<Product> products, Size size) {
        return products.stream()
                .filter(product -> product.size == size);
    }

    public Stream<Product> filterBySizeAndColor(List<Product> products, Size size, Color color) {
        return products.stream()
                .filter(product -> product.size == size
                        && product.color == color);
    }

    // state space explosion
    // 3 criteria = 7 methods
}

// here is where we enhance our implementation to follow open-closed principle
// we introduce two new interfaces that are open for extension
interface Specification<T> {
    boolean isSatisfied(T item);
}

interface Filter<T> {
    Stream<T> filter(List<T> items, Specification<T> spec);
}

class BetterFilter implements Filter<Product> {

    @Override
    public Stream<Product> filter(List<Product> items, Specification<Product> spec) {
        return items.stream().filter(product -> spec.isSatisfied(product));
    }
}

class ColorSpecification implements Specification<Product> {
    private Color color;

    public ColorSpecification(Color color) {
        this.color = color;
    }

    @Override
    public boolean isSatisfied(Product item) {
        return item.color == color;
    }
}

class SizeSpecification implements Specification<Product> {
    private Size size;

    public SizeSpecification(Size size) {
        this.size = size;
    }

    @Override
    public boolean isSatisfied(Product p) {
        return p.size == size;
    }
}

class PriceSpecification implements Specification<Product> {
    private Price price;

    public PriceSpecification(Price price) {
        this.price = price;
    }

    @Override
    public boolean isSatisfied(Product p) {
        return p.price == price;
    }
}

class AndSpecification<T> implements Specification<T> {
    private Specification<T> first, second;

    AndSpecification(Specification<T> first, Specification<T> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isSatisfied(T item) {
        return first.isSatisfied(item) && second.isSatisfied(item);
    }
}

class ComboSpecification<T> implements Specification<T> {
    private List<Specification<T>> specifications;

    ComboSpecification(List<Specification<T>> specifications) {
        this.specifications = specifications;
    }

    @Override
    public boolean isSatisfied(T item) {
        AtomicBoolean isSatisfied = new AtomicBoolean(true);
        specifications.forEach(spec -> {
            if(!spec.isSatisfied(item)) {
                isSatisfied.set(false);
            }
        });

        return isSatisfied.get();
    }
}

class OCPDemo {
    public static void main(String[] args) {
        Product apple = new Product("Apple", Color.GREEN, Size.SMALL, Price.LOW);
        Product tree = new Product("Tree", Color.GREEN, Size.LARGE, Price.MEDIUM);
        Product house = new Product("House", Color.BLUE, Size.LARGE, Price.HIGH);

        List<Product> products = List.of(apple, tree, house);

        ProductFilter pf = new ProductFilter();
        System.out.println("Green products (old):");
        pf.filterByColor(products, Color.GREEN)
                .forEach(product -> System.out.println(" - " + product.name + " is green"));

        BetterFilter bf = new BetterFilter();
        System.out.println("Green products (new):");
        bf.filter(products, new ColorSpecification(Color.GREEN))
                .forEach(product -> System.out.println(" - " + product.name + " is green"));

        System.out.println("Large blue items:");
        bf.filter(products, new AndSpecification<>(
                new ColorSpecification(Color.BLUE),
                new SizeSpecification(Size.LARGE)))
                .forEach(product -> System.out.println(" - " + product.name + " is blue and large"));

        System.out.println("Large blue and high price items");
        bf.filter(products, new ComboSpecification<>(
                List.of(
                       new SizeSpecification(Size.LARGE),
                       new ColorSpecification(Color.BLUE),
                        new PriceSpecification(Price.HIGH)
                )
        ))
                .forEach(product -> System.out.println(" - " + product.name + " is blue and large and high price"));
    }
}
