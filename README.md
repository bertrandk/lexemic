# lexemic

## Requirements

* Node.js (v0.10.17+)

## Install

	npm install lexemic -g

## Description

Tools for working with human language data.

## Features

* Sentiment analysis
* Stemming
* Tokenizing
* Statistical analysis

## Usage

```
$ lexemic [command] [implementation] [target...]
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
Sentiment analysis attempts to determine the affective state of the
speaker or the writer. The default implementation returns an
[EDN](https://github.com/edn-format/edn) map of this analysis. The `:score`
represents the number of emotive words in the text while the
`:comparative` rates the occurrence of these words with regards to the
length of the text. The nested values (i.e. those under `:positive` and
`:negative` ) provide a list of matched `:words` and take only into account
their respective affectivity. The top level values incorporate both
affective states – returning negative values for texts with overall negative
affects and positive values for texts with overall positive affects. 

## Issues

If you need help, find a bug, want to request a feature or want to contribute, please
[create an issue](https://github.com/bertrandk/lexemic/issues/new).

## Copyright

Copyright (c) 2013 Bertrand Karerangabo

See `LICENSE.txt` for details.
