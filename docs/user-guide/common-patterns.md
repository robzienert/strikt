# Common Assertion Patterns

This section contains some common uses of Strikt's standard assertion library.

## Assertions on elements of a collection

Some assertions on collections include sub-assertions applied to the elements of the collection.
For example, we can assert that _all_ elements conform to a repeated assertion.

```kotlin
val subject = setOf("catflap", "rubberplant", "marzipan")
expect(subject).all {
  isLowerCase()
  startsWith('c')
}
```

This produces the output:

```
▼ Expect that [catflap, rubberplant, marzipan] 
  ✗ all elements match:
    ▼ "catflap"
      ✓ starts with 'c'
      ✓ is lower case
    ▼ "rubberplant" 
      ✗ starts with 'c'
      ✓ is lower case
    ▼ "marzipan"
      ✗ starts with 'c'
      ✓ is lower case
```

The results are broken down by individual elements in the collection so it's easy to see which failed.

## Asserting exceptions are thrown

To assert that some code throws an exception you can use an assertion on a lambda `() -> Unit` that performs the operation that should throw an exception and the `throws<E>` assertion function.
For example:

```kotlin
expect { service.computeMeaning() }
  .throws<TooMuchFlaxException>()
```

The `throws<E>` function returns an `Assertion<E>` so you can chain assertions about the exception after it.

There is also a top level function `throws( () -> Unit )` that makes this even more concise.

```kotlin
throws<TooMuchFlaxException> { 
  service.computeMeaning() 
}
```
