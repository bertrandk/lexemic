(ns lexemic.sentiment.simple
  (:use [clojure.string :only [lower-case split]]
        [cljs.reader :only [read-string]])
  (:require [lexemic.data.sentiment :as data]))

(defn- collect-matches [tokens lexicon]
  (vec
   (for [token tokens
         :when (contains? lexicon token)]
     token)))

(defn- rate [phrase lexicon]
  (let [tokens (split (lower-case phrase) #"\s+")
        matches (collect-matches tokens lexicon)
        hits (count matches)])
  {:score hits :comparative (/ hits (count tokens)) :words words})

(defn negative-rating [phrase]
  (rate phrase data/negative-words))

(defn positive-rating [phrase]
  (rate phrase data/positive-words))

(defn analyse [phrase]
  (let [negative (negative-rating phrase)
        positive (positive-rating phrase)]
    {:score (- (positive :score) (negative :score))
     :comparative (- (positive :comparative) (negative :comparative))
     :positive positive
     :negative negative}))
