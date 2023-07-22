package SOLID_Principles.Dependency_Inversion_Principle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.javatuples.Triplet;


public class DependencyInversionExample {
}

enum Relationship {
    PARENT,
    CHILD,
    SIBLING
}

class Person {
    // public fields are BAD! prefer private fields with a getter
    public String name;

    public Person(final String name) {
        this.name = name;
    }
}

interface RelationshipBrowser {
    List<Person> findAllChildrenOf(String name);
}

// we want to model the relationships between different people

class Relationships implements RelationshipBrowser { // low-level module, related to data storage w/ no business logic
    private List<Triplet<Person, Relationship, Person>> relations = new ArrayList<>();

    public List<Triplet<Person, Relationship, Person>> getRelations() {
        return relations;
    }

    public void addParentAndChild(Person parent, Person child) {
        relations.add(new Triplet<>(parent, Relationship.PARENT, child));
        relations.add(new Triplet<>(child, Relationship.CHILD, parent));
    }

    @Override
    public List<Person> findAllChildrenOf(String name) {
        return relations.stream()
                .filter(x -> x.getValue0().name.equals("John")
                        && x.getValue1() == Relationship.PARENT)
                .map(Triplet::getValue2)
                .collect(Collectors.toList());
    }
}

class Research { // high-level module, allows us to perform operations on low level constructs (business logic)
    /** break the dependency inversion principle -
     * here we have a high level module (Research) depending on a low level module (Relationships)
     * since we pass in the class itself rather than an interface or abstract class
     *
     * if we wanted to change structure of relations it would break our research class!
     * */
    public Research(final Relationships relationships) {
        List<Triplet<Person, Relationship, Person>> relations = relationships.getRelations();
        relations.stream()
                .filter(x -> x.getValue0().name.equals("John")
                && x.getValue1() == Relationship.PARENT)
                .forEach(ch -> System.out.println(
                        "John has a child called {}", ch.getValue2().name
                ));
    }

    public Research(final RelationshipBrowser relationshipBrowser) {
        relationshipBrowser.findAllChildrenOf("John")
                .stream()
                .forEach(ch -> System.out.println(
                        "John has a child called {}", ch.name
                ));
    }
}

class Demo {
    public static void main(String[] args) {
        Person parent1 = new Person("John");
        Person child1 = new Person("Chris");
        Person child2 = new Person("Matt");

        Relationships relationships = new Relationships();
        relationships.addParentAndChild(parent1, child1);
        relationships.addParentAndChild(parent1, child2);

        new Research(relationships);
    }
}
