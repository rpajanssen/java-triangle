### Modules

A demo project showcasing how you can implement dependencies in classes with plain
java, using a DI framework (Guice) and using a combination of the Service Locator
pattern with a DI framework.

The service locator pattern itself is showcased in the code for OCP Chapter 17.

You can find the different examples by checking out the tags:
- M_AND_I_PLAIN             : base implementation (no modules, no injection)
- M_AND_I_WITH_MODULES      : using modules (no injection)
- M_AND_I_WITH_GUICE        : using modules and guice with injection
- M_AND_I_WITH_GUICE_AND_SL : using modules and guice with injection with service locator

#### Services with Service Locator

As seen in the OCP Chapter 17 code we can achieve great encapsulation and loose coupling
using the Service Locator pattern. We can just add/remove implementations (add/remove a jar)
and your done. No dependency on implementations is required. Not in the classes nor in the
module-info files.

#### Using a DI framework

Using a DI framework like Guice, you gain the ability to use dependency injection
but at the cost of introducing some tight coupling on implementations and 
by opening some modules for reflection (required by DI frameworks).

To make it work you need to add dependencies on the implementation in the Java classes
and dependencies on the implementing module(s).

You will also need some boilerplate configuration code to make it all work within
the DI framework. With that you will tightly couple your code to the DI framework.

#### Combining Service Locator with a DI Framework

Combining the Service Locator pattern with a DI framework gives us loose coupling with
the implementations and allows us to use dependency injection.

We still have a tight coupling with the DI framework throughout our code.

