# Release notes #

### 1.2.0 (2023-12-01) ###

* Several performance and visual improvements applied. 
* One whole API deprecated for simplicity. 
* Timeout added to Firebase's requests to avoid infinite loading in some edge cases.
* Sticking to `Result` when possible and avoid unnecessary `Flow` wrapping.
* Improved domain layer use with the creation of some `UseCase` and having some `Repository` split.

### 1.1.2 (2023-06-14) ###

* General live elections working.

### 1.1.1 (2023-06-01) ###

* Live regional and local elections fixed. El País API replaced by elDiario.es' one.

### 1.1.0 (2023-05-11) ###

* Live regional and local elections feature released.

### 1.0.1 (2023-02-27) ###

* `Firebase` called when `Retrofit` throws any kind of `Throwable`.

### 1.0.0 (2023-02-26) ###

* First release.
