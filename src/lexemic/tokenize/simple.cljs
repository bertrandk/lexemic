(ns lexemic.tokenize.simple
  (:use [clojure.string :only [split]]))

(defn space-tokenizer [s]
  (split s #"[ ]"))

(defn tab-tokenizer [s]
  (split s #"\t"))

(defn whitespace-tokenizer [s]
  (split s #"\s+"))

(defn blankline-tokenizer [s]
  (split s #"\s*\n\s*\n\s*"))

(defn char-tokenizer [s]
  (vec (seq s)))

(defn line-tokenizer [s]
  (split s #"\n"))