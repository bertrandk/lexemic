# lexemic

## Requirements

* Node.js (v0.10.17+)

## Install

	npm install lexemic

## Description

Tools for working with human language data.

## Features

* Sentiment analysis
* Stemming
* Tokenizing
* Statistical analysis

## Usage

```
$ lexemic [command] [implementation] [target]
```
NOTE: The `target` may be an inline string or the path to a text file encoded as UTF-8.

### Sentiment analysis

```
$ lexemic sentiment "I am mad at you." # => {  
                                             :score -1,  
                                             :comparative -0.25,  
                                             :positive {  
                                                        :score 0,  
                                                        :comparative 0,  
                                                        :words ()  
                                                       },  
                                             :negative {  
                                                        :score 1,  
                                                        :comparative 0.25,  
                                                        :words (mad)  
                                                       }  
                                             }
```

## Issues

## Copyright

See `LICENSE.txt` for details.
