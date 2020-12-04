## JSON Flattener

Just flatten it!

json-flattener streams and flattens a JSON content into a single-leveled, dot-separated output, instead of having structured properties.

### Example
Given the following JSON:

```json
{
  "name": "User",
  "detail": {
    "address": "Test Avenue",
    "zip": 12345
  } 
}
```
The flattened version is as follows:
```json
{
  "name": "User",
  "detail.address": "Test Avenue",
  "detail.zip": 12345
} 
```
There are two modules within this project:

#### CLI
A command-line interface for reading JSON content and flattening. It should be used if you want to use in your command line using stdin (eg.: piping a content into it).

#### Core
If you want to include as a library of your application. Clone the project and run `mvn install` so you should be able to see it in your local repo.

#### Installation of CLI
Just download the dist folder [HERE](cli/dist) and, in shell, create an alias to the download binaries, for example:
- Download the dist folder as zip
- Unpack and cd into it
- `alias jf=$PWD/bin/json-flattener`

#### Usage
Assuming the last steps above were followed, you need now to stream the data into the app.
Eg.:

`cat content.json | jf` 
will print the flattened version of JSON into stout

`cat content.json | jf > flattened.json`
will generate a file containing the flattened version of `content.json`

#### Limitations
* It doesn't support arrays.  

#### Requirements
- Java 8
- Unix-compatible system

#### Building locally
In order to build locally, you need to have maven 3.x installed, as well as Java 8.

Just clone this repo and run:
```shell script
mvn package
``` 

#### Contributing
Fork and open a PR.

#### License
It is under MIT license, which can be checked [here](LICENSE)

###### TODO
- [ ] Include support for file input (instead of only piping into the program)
- [ ] Include support for arrays
- [ ] Add libraries to maven-central
- [ ] Add build-pipeline
- [ ] Create deb packages/Tap formulae/etc