# equality-collection
Currently a HashSet implementation allowing to set custom equality and hash functions for the set.


## Usage

````
Set<String> passthrough = new EqualityHashSet<>(
  // rely on elements equals method
  (a, b) -> Objects.equals(a, b), 
  // rely on elements hashCode
  a -> Objects.hashCode(a)
);
        
Set<Integer> module = new EqualityHashSet<>(
  // even number are all equal
  (a, b) -> {
      return (a % 2 == 0 && b % 2 == 0);
  }, 
  // even number all have a hashCode of 2
  a -> {
      return (a % 2 == 0) ? 2 : a;
  }
);
````

## License

The MIT License (MIT)
