(ns cljs-nlp.tokenize.simple
  (:use [clojure.string :only [split]]))

(defn space-tokenizer [str]
  (split str #"[ ]"))

(defn tab-tokenizer [str]
  (split str #"\t"))

(defn whitespace-tokenizer [str]
  (split str #"\s+"))

(defn blankline-tokenizer [str]
  (split str #"\s*\n\s*\n\s*"))

(defn char-tokenizer [str]
  (vec (seq str)))

(defn line-tokenizer [str]
  (split str #"\n"))